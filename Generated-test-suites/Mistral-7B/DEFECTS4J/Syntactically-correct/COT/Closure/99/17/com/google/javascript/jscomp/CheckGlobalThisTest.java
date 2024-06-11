package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CheckGlobalThis.class })
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckerFactory checkerFactory;
  private NodeFactory nodeFactory;
  private NodeTraversal traverser;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    checkerFactory = PowerMockito.mock(CheckerFactory.class);
    nodeFactory = PowerMockito.mock(NodeFactory.class);
    traverser = new NodeTraversal(compiler, checkerFactory, nodeFactory);
    sourceFile = PowerMockito.mock(SourceFile.class);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithoutJsDocInfo() {
    String jsCode = "function evil() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDocInfo_NotConstructor() {
    String jsCode = "/** @type {Function} */\nfunction evil() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDocInfo_Constructor() {
    String jsCode = "/** @constructor */\nfunction EvilConstructor() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(compiler.getDiagnostics().contains(diagnosticType));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDocInfo_ThisAnnotation() {
    String jsCode = "/** @this {Object} */\nfunction evil() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDocInfo_PrototypeProperty() {
    String jsCode = "function EvilPrototype() {}";
    jsCode += "\nEvilPrototype.prototype.evil = function() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDocInfo_PrototypeSubProperty() {
    String jsCode = "function EvilPrototype() {}";
    jsCode += "\nEvilPrototype.prototype.useful = { evil: function() { this.subUseful = undefined; } }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment_LeftSide() {
    String jsCode = "var a = function() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(compiler.getDiagnostics().contains(diagnosticType));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment_RightSide() {
    String jsCode = "var a = function() {}; a.useful = function() { this.subUseful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment_PrototypeProperty() {
    String jsCode = "var EvilPrototype = {}; EvilPrototype.evil = function() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(compiler.getDiagnostics().contains(diagnosticType));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment_PrototypeSubProperty() {
    String jsCode = "var EvilPrototype = {}; EvilPrototype.useful = { evil: function() { this.subUseful = undefined; } }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment_Nested() {
    String jsCode = "var a = function() {}; a.useful = { evil: function() { this.subUseful = undefined; } }";
    Node ast = Parser.parse(jsCode, "Test", sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.shouldTraverse(null, null, null);
    checker.visit(traverser, null, null);

    traverser.traverse(ast);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}