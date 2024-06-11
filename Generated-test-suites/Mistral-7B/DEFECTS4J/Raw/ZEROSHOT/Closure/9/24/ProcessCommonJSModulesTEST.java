package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CommonJSModuleTestUtils.assertModuleProcessedCorrectly;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private JSErrorReporter errorReporter;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    errorReporter = new JSErrorReporter();
    compiler = new TestCompiler(options, errorReporter);
  }

  @Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerInput input = new CompilerInput(
        new TestSourceFile("commonJsModuleA.js", "var a = require('commonJsModuleB');"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), input.getRootNode());
    assertModuleProcessedCorrectly(input, "commonJsModuleA.js", "moduleA",
        "commonJsModuleB.js", "moduleB");
  }

  @Test
  public void testProcessCommonJSModulesWithExports() throws IOException {
    CompilerInput input = new CompilerInput(
        new TestSourceFile("commonJsModuleAWithExports.js",
            "var a = module.exports = require('commonJsModuleBWithExports');"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), input.getRootNode());
    assertModuleProcessedCorrectly(input, "commonJsModuleAWithExports.js",
        "moduleA", "commonJsModuleBWithExports.js", "moduleB");
  }

  @Test
  public void testProcessCommonJSModulesWithDependentModules()
      throws IOException {
    CompilerInput input = new CompilerInput(
        new TestSourceFile("commonJsModuleA.js",
            "var a = require('commonJsModuleB').foo;"),
        new TestSourceFile("commonJsModuleB.js", "exports.foo = 'bar';"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), input.getRootNode());
    assertModuleProcessedCorrectly(input, "commonJsModuleA.js", "moduleA",
        "commonJsModuleB.js", "moduleB");
    assertEquals("bar", input.getExterns().getGlobalVariableValue("a"));
  }

  @Test
  public void testProcessCommonJSModulesWithCircularDependencies()
      throws IOException {
    CompilerInput input = new CompilerInput(
        new TestSourceFile("commonJsModuleA.js",
            "var a = require('commonJsModuleB'); var b = require('commonJsModuleA');"),
        new TestSourceFile("commonJsModuleB.js", "exports.foo = 'bar';"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    try {
      processor.process(input.getExterns(), input.getRootNode());
      throw new AssertionError("Expected error but none was thrown.");
    } catch (IllegalStateException e) {
      assertEquals("Circular dependency between modules: commonJsModuleA and commonJsModuleB",
          e.getMessage());
    }
  }

  @Test
  public void testProcessCommonJSModulesWithNonExistentModules()
      throws IOException {
    CompilerInput input = new CompilerInput(
        new TestSourceFile("commonJsModuleA.js", "var a = require('nonExistentModule');"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), input.getRootNode());
    Set<String> errors = Sets.newHashSet(errorReporter.getErrors());
    assertEquals("Error: Module 'nonExistentModule' not found", errors.toString());
  }
}

class TestCompiler extends AbstractCompiler {

  TestCompiler(CompilerOptions options, JSErrorReporter errorReporter) {
    super(options, errorReporter);
  }

  @Override
  public Traversal createTraversal(Node root, Node externs, PrintStream err) {
    return new NodeTraversal(root, externs, null, this) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 &&
            "require".equals(n.getChildAtIndex(0).getQualifiedName())) {
          t.getInput().addModuleDependency(n.getChildAtIndex(1).getString());
        }
        super.visit(t, n, parent);
      }
    };
  }
}