package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SimpleChecker;
import com.google.javascript.rhino.NodeOperations;

public class CheckSideEffectsTest {
  private Compiler compiler;
  private CheckSideEffects checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setCheckSideEffects(true);
    compiler = new Compiler(CheckerFactory.getChecker("none"), options);
    checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testVisitHideSideEffectCode() {
    String source = "function foo() {}";
    Node root = NodeOperations.parseCompilationUnit(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    String result = NodeOperations.printToString(root);
    String expected = "function JSCOMPILER_PROTECTED_SIDE_EFFECTS_0() {" + source + "} function foo() {}";
    assertTrue(result.equals(expected));
  }

  @Test
  public void testVisitVoidStatement() {
    String source = "var x = 1;\n;";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertFalse(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitSimpleOp() {
    String source = "var x = 1 + 2;";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitStatementWithSideEffect() {
    String source = "var x = 1;\nfor (var i = 0; i < 10; i++) { x += i; }";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertFalse(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitBlock() {
    String source = "function foo() {\nvar x = 1;\nfor (var i = 0; i < 10; i++) { x += i; }\n}";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitStatementWithMissingPlus() {
    String source = "var s = \"this string is \"\n\"continued on the next line but you forgot the +\"";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertFalse(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitFunctionCallWithParentheses() {
    String source = "function foo() {}();";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertFalse(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitFunctionCallWithoutParentheses() {
    String source = "foo();";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitFunctionCallWithEmptyParameterList() {
    String source = "function foo() {}();";
    Node root = NodeOperations.parseCompilationUnit(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    String result = NodeOperations.printToString(root);
    String expected = "function JSCOMPILER_PROTECTED_SIDE_EFFECTS_1() {" + source + "} function foo() {}();";
    assertTrue(result.equals(expected));
  }

  @Test
  public void testVisitFunctionCallWithArguments() {
    String source = "function foo(x) {}();";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitForStatement() {
    String source = "for (var i = 0; i < 10; i++) {}";
    Node root = NodeOperations.parseCompilationUnit(source);
    checker.visit(null, root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitCallToProtectedFunction() {
    String source = "JSCOMPILER_PROTECTED_SIDE_EFFECTS_0();";
    Node root = NodeOperations.parseCompilationUnit(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    String result = NodeOperations.printToString(root);
    String expected = "function JSCOMPILER_PROTECTED_SIDE_EFFECTS_0() {} JSCOMPILER_PROTECTED_SIDE_EFFECTS_0();";
    assertTrue(result.equals(expected));
  }

  @Test
  public void testRemoveProtection() {
    String source = "function JSCOMPILER_PROTECTED_SIDE_EFFECTS_0() {} JSCOMPILER_PROTECTED_SIDE_EFFECTS_0();";
    Node root = NodeOperations.parseCompilationUnit(source);
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripper);
    String result = NodeOperations.printToString(root);
    String expected = "function JSCOMPILER_PROTECTED_SIDE_EFFECTS_0() {}(); JSCOMPILER_PROTECTED_SIDE_EFFECTS_0();";
    assertTrue(result.equals(expected));
  }
}