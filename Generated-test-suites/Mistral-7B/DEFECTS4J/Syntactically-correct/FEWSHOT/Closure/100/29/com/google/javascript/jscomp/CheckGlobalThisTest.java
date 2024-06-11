package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.DiagnosticType.warning;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.TestUtils;

public class CheckGlobalThisTest {
  private Checker checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @org.junit.Before
  public void setUp() {
    compiler = new TestCompiler();
    checker = new Checker(compiler);
    traversal = new NodeTraversal(checker);
  }

  @org.junit.Test
  public void testNoGlobalThis_Function() {
    Node node = TestUtils.createFunctionNode("function foo() {}");
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testNoGlobalThis_FunctionWithThisAnnotation() {
    Node node = TestUtils.createFunctionNode("@this class Foo {\n  constructor() {}\n}");
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testNoGlobalThis_FunctionWithPrototypeMethod() {
    Node node = TestUtils.createFunctionNode("function Foo() {\n this.bar = 1;\n}");
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testNoGlobalThis_PropertyAccess() {
    Node left = TestUtils.createGetPropertyNode("obj.prototype");
    Node node = TestUtils.createBinaryOpNode("=", TestUtils.createThisNode(), left);
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testNoGlobalThis_Assignment() {
    Node left = TestUtils.createThisNode();
    Node right = TestUtils.createNumberNode(2);
    Node node = TestUtils.createBinaryOpNode("=", left, right);
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testGlobalThis_Function() {
    Node node = TestUtils.createFunctionNode("function foo() { this = 1; }");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), type);
  }

  @org.junit.Test
  public void testGlobalThis_FunctionWithPrototypeMethodAndAssignment() {
    Node node = TestUtils.createFunctionNode("function Foo() {\n this.bar = 1;\n this = 2;\n }");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), type);
  }

  @org.junit.Test
  public void testGlobalThis_FunctionWithAssignment() {
    Node node = TestUtils.createFunctionNode("function foo() { this = 1; }");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }

  @org.junit.Test
  public void testGlobalThis_PropertyAccessAndAssignment() {
    Node left = TestUtils.createGetPropertyNode("obj.prototype");
    Node assign = TestUtils.createThisNode();
    Node right = TestUtils.createNumberNode(2);
    Node node = TestUtils.createBinaryOpNode("=", assign, left);
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }

  @org.junit.Test
  public void testGlobalThis_AssignmentToPrototype() {
    Node left = TestUtils.createGetPropertyNode("obj.prototype.bar");
    Node right = TestUtils.createThisNode();
    Node node = TestUtils.createBinaryOpNode("=", right, left);
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testGlobalThis_VariableDeclaration() {
    Node node = TestUtils.createStatementNode("var this = 1;");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnosticCount());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }

  @org.junit.Test
  public void testGlobalThis_Constructor() {
    Node node = TestUtils.createFunctionNode("function Foo() {\n this = 1;\n}");
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testGlobalThis_ConstructorWithThisAnnotation() {
    Node node = TestUtils.createFunctionNode("@this class Foo {\n constructor() {}\n}");
    traversal.traverse(node, null);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testGlobalThis_FunctionWithVarDeclaration() {
    Node node = TestUtils.createFunctionNode("function foo() {\n var this = 1;\n}");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }

  @org.junit.Test
  public void testGlobalThis_FunctionWithVarDeclarationAndPrototypeMethod() {
    Node node = TestUtils.createFunctionNode("function foo() {\n var this = 1;\n this.bar = 1;\n}");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }

  @org.junit.Test
  public void testGlobalThis_FunctionWithNoLogicalPlaceForThisAnnotation() {
    Node node = TestUtils.createFunctionNode("function foo() {\n // no place to put this annotation here\n this = 1;\n}");
    traversal.traverse(node, null);
    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType type = checker.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_ Global This"), type);
  }
}