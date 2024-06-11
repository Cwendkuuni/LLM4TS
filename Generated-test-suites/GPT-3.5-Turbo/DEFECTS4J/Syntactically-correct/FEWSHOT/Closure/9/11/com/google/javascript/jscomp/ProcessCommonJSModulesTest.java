/*
 * Copyright 2011 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setLanguageOut(LanguageMode.ECMASCRIPT5);
    compiler.initOptions(options);
  }

  @Test
  public void testProcessCommonJSModules() {
    String original =
        "require('foo');" +
        "require('bar');" +
        "module.exports = 42;";
    Node root = parse(original);
    compiler.processForTesting(root, new ProcessCommonJSModules(compiler, ""));
    String expected =
        "var module$foo = {};" +
        "goog.require('foo');" +
        "var module$bar = {};" +
        "goog.require('bar');" +
        "var module$module = {};" +
        "module$module.module$exports = 42;";
    assertNode(expected, root);
  }

  private Node parse(String js) {
    Node root = compiler.parse(JSSourceFile.fromCode("input", js));
    assertNotNull(root);
    assertFalse(compiler.hasErrors());
    return root;
  }

  private void assertNode(String js, Node root) {
    assertEquals(js, compiler.toSource(root));
    assertFalse(compiler.hasErrors());
  }
}