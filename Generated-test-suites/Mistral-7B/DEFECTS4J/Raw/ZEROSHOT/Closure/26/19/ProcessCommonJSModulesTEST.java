package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilerFlag;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private CompilerPass processCommonJSModules;
  private NodeFactory nodeFactory;
  private Compiler compiler;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() throws Exception {
    nodeFactory = new NodeFactory();
    compiler = mock(Compiler.class);
    options = new CompilerOptions();
    processCommonJSModules = new ProcessCommonJSModules(compiler,
        ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX, true);
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    String input = "// Test file\n" +
        "var a = require('./module1');\n" +
        "exports.b = a.c;\n";

    SourceFile sourceFile = new TestSourceFile(new File("test.js"), input);
    Node root = nodeFactory.compile(sourceFile, options);

    NodeTraversal nodeTraversal = new NodeTraversal(root, options, null);
    nodeTraversal.setVisitor(processCommonJSModules);
    nodeTraversal.traverse();

    Node expectedOutput = nodeFactory.compile(
        getExpectedOutput("test_expected.js"), options).getTree();

    assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithMultipleRequireCalls()
      throws IOException {
    String input = "// Test file\n" +
        "var a = require('./module1');\n" +
        "var b = require('./module2');\n" +
        "exports.c = a.d;\n" +
        "exports.d = b.e;\n";

    SourceFile sourceFile = new TestSourceFile(new File("test.js"), input);
    Node root = nodeFactory.compile(sourceFile, options);

    NodeTraversal nodeTraversal = new NodeTraversal(root, options, null);
    nodeTraversal.setVisitor(processCommonJSModules);
    nodeTraversal.traverse();

    Node expectedOutput = nodeFactory.compile(
        getExpectedOutput("test_expected_multiple.js"), options).getTree();

    assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleNameCollision()
      throws IOException {
    String input = "// Test file\n" +
        "var a = require('./module1');\n" +
        "exports.a = a.a;\n" +
        "var b = require('./module1');\n" +
        "exports.b = b.a;\n";

    SourceFile sourceFile = new TestSourceFile(new File("test.js"), input);
    Node root = nodeFactory.compile(sourceFile, options);

    NodeTraversal nodeTraversal = new NodeTraversal(root, options, null);
    nodeTraversal.setVisitor(processCommonJSModules);
    nodeTraversal.traverse();

    Node expectedOutput = nodeFactory.compile(
        getExpectedOutput("test_expected_module_collision.js"), options).getTree();

    assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws IOException {
    String input = "// Test file\n" +
        "var a = require('./module1');\n" +
        "exports.b = a.c;\n" +
        "module.exports = {a: a};\n";

    SourceFile sourceFile = new TestSourceFile(new File("test.js"), input);
    Node root = nodeFactory.compile(sourceFile, options);

    NodeTraversal nodeTraversal = new NodeTraversal(root, options, null);
    nodeTraversal.setVisitor(processCommonJSModules);
    nodeTraversal.traverse();

    Node expectedOutput = nodeFactory.compile(
        getExpectedOutput("test_expected_module_exports.js"), options).getTree();

    assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
  }

  private String getExpectedOutput(String fileName) throws IOException {
    String expectedOutput = "";
    switch (fileName) {
      case "test_expected.js":
        expectedOutput = "// Test file\n" +
            "goog.provide('module1');\n" +
            "goog.provide('test');\n" +
            "goog.require('module1');\n" +
            "exports.c = module1.c;\n";
        break;
      case "test_expected_multiple.js":
        expectedOutput = "// Test file\n" +
            "goog.provide('module1');\n" +
            "goog.provide('module2');\n" +
            "goog.provide('test');\n" +
            "goog.require('module1');\n" +
            "goog.require('module2');\n" +
            "exports.c = module1.d;\n" +
            "exports.d = module2.e;\n";
        break;
      case "test_expected_module_collision.js":
        expectedOutput = "// Test file\n" +
            "goog.provide('module1');\n" +
            "goog.provide('testA');\n" +
            "goog.provide('testB');\n" +
            "goog.require('module1');\n" +
            "exports.a = testA.a;\n" +
            "exports.b = testB.a;\n";
        break;
      case "test_expected_module_exports.js":
        expectedOutput = "// Test file\n" +
            "goog.provide('module1');\n" +
            "goog.provide('test');\n" +
            "goog.require('module1');\n" +
            "exports.a = module1.a;\n" +
            "goog.provide('test.a');\n" +
            "test.a = module1.a;\n";
        break;
      default:
        throw new RuntimeException("Unexpected fileName: " + fileName);
    }

    return expectedOutput;
  }

  @org.junit.After
  public void tearDown() throws Exception {
    if (options.get(CompilerFlag.DEEP_CLOSURES)) {
      compiler.disposeClosures();
    }
  }
}