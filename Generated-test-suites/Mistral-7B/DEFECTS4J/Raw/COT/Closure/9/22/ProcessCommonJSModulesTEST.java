package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    nodeFactory = compiler.getOptions().nodeFactory;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    String input = "// Input file\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "var module = {\n" +
        "  module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "module.exports = module.module$exports;\n" +
        "exports = module.module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('dependency');\n";

    String expectedOutput = "// Output file\n" +
        "var my$module = {\n" +
        "  my$module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "my$module.my$module$exports = my$module.my$module$exports;\n" +
        "goog.provide('my.module');\n" +
        "";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".", false);

    CompilerResult result = new CompilerResult();
    Node externs = null;
    Node root = null;

    try {
      externs = nodeFactory.createTopLevel(nodeFactory.createText(""));
      root = nodeFactory.parseTree(input, null, null);

      processor.process(externs, root);

      assertEquals(expectedOutput, result.getSourceCode());
      assertNull(result.getErrors());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModules_reportDependencies()
      throws IOException {
    String input = "// Input file\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "var module = {\n" +
        "  module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "module.exports = module.module$exports;\n" +
        "exports = module.module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('dependency');\n";

    String expectedOutput = "// Output file\n" +
        "var my$module = {\n" +
        "  my$module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "my$module.my$module$exports = my$module.my$module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('dependency');\n";

    String expectedDepends = "// Dependencies file\n" +
        "['dependency']\n";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".", true);

    CompilerResult result = new CompilerResult();
    Node externs = null;
    Node root = null;

    try {
      externs = nodeFactory.createTopLevel(nodeFactory.createText(""));
      root = nodeFactory.parseTree(input, null, null);

      processor.process(externs, root);

      assertEquals(expectedOutput, result.getSourceCode());
      assertEquals( Sets.newHashSet("dependency"),
          result.getInput().getRequireNames());
      assertNull(result.getErrors());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModules_relativePaths()
      throws IOException {
    String input = "// Input file\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "var module = {\n" +
        "  module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "module.exports = module.module$exports;\n" +
        "exports = module.module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('./dependency');\n";

    String expectedOutput = "// Output file\n" +
        "var my$module = {\n" +
        "  my$module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "my$module.my$module$exports = my$module.my$module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('dependency');\n";

    String expectedDepends = "// Dependencies file\n" +
        "['dependency']\n";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".", true);

    CompilerResult result = new CompilerResult();
    Node externs = null;
    Node root = null;

    try {
      externs = nodeFactory.createTopLevel(nodeFactory.createText(""));
      root = nodeFactory.parseTree(input, null, null);

      processor.process(externs, root);

      assertEquals(expectedOutput, result.getSourceCode());
      assertEquals( Sets.newHashSet("dependency"),
          result.getInput().getRequireNames());
      assertNull(result.getErrors());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModules_errors()
      throws IOException {
    String input = "// Input file\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "var module = {\n" +
        "  module$exports: {\n" +
        "    foo: 42\n" +
        "  }\n" +
        "};\n" +
        "module.exports = module.module$exports;\n" +
        "exports = module.module$exports;\n" +
        "goog.provide('my.module');\n" +
        "goog.require('dependency');\n" +
        "goog.provide('my.module2');\n";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".", false);

    CompilerResult result = new CompilerResult();
    Node externs = nodeFactory.createTopLevel(nodeFactory.createText(""));
    Node root = nodeFactory.parseTree(input, null, null);

    try {
      processor.process(externs, root);
      fail();
    } catch (JSError e) {
      assertEquals(1, result.getErrors().size());
      assertEquals("Error: circular dependency: my.module, my.module2",
          e.getMessage());
    }
  }
}