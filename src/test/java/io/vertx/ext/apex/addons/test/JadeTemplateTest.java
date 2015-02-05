/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.apex.addons.test;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.apex.addons.JadeTemplateEngine;
import io.vertx.ext.apex.addons.PathTemplateHandler;
import io.vertx.ext.apex.addons.TemplateEngine;
import io.vertx.ext.apex.test.ApexTestBase;
import org.junit.Test;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class JadeTemplateTest extends ApexTestBase {

  @Test
  public void testTemplateHandler() throws Exception {
    TemplateEngine engine = JadeTemplateEngine.create();
    testTemplateHandler(engine, "somedir", "test-jade-template2.jade", "<!DOCTYPE html><html><head><title>badger/test-jade-template2.jade</title></head><body></body></html>");
  }

  @Test
  public void testTemplateHandlerNoExtension() throws Exception {
    TemplateEngine engine = JadeTemplateEngine.create();
    testTemplateHandler(engine, "somedir", "test-jade-template2", "<!DOCTYPE html><html><head><title>badger/test-jade-template2</title></head><body></body></html>");
  }

  private void testTemplateHandler(TemplateEngine engine, String directoryName, String templateName,
                                   String expected) throws Exception {
    router.route().handler(context -> {
      context.put("foo", "badger");
      context.next();
    });
    router.route().handler(PathTemplateHandler.templateHandler(engine, directoryName, "text/plain"));
    testRequest(HttpMethod.GET, "/" + templateName, 200, "OK", expected);
  }

  @Test
  public void testNoSuchTemplate() throws Exception {
    TemplateEngine engine = JadeTemplateEngine.create();
    router.route().handler(PathTemplateHandler.templateHandler(engine, "nosuchtemplate.jade", "text/plain"));
    testRequest(HttpMethod.GET, "/foo.jade", 500, "Internal Server Error");
  }

}