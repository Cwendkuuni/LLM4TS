package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionsWithConstructors() {
    String jsSource = "function Foo() {}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionsWithThisAnnotation() {
    String jsSource = "/** @this */ function Foo() {}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionsWithoutThisAnnotation() {
    String jsSource = "function Foo() {}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionsOutsideAssignments() {
    String jsSource = "function Foo() {}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    Node parent = Node.newScopeNode();
    Node assignNode = Node.newAssign(parent, Node.newName("x"), node);
    parent.addChildToEnd(assignNode);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.setParent(parent);
    traversal.traverse();
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionsInsideAssignmentsToPrototypeProperties() {
    String jsSource = "function Foo() {\n" +
        "  Object.prototype.prototype.x = this;\n" +
        "}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsInsideAssignmentsToSubProperties() {
    String jsSource = "function Foo() {\n" +
        "  Object.prototype.x.prototype.y = this;\n" +
        "}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfAssignment() {
    String jsSource = "var x = this;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, node, null);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    String jsSource = "var x = {}; x = this;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, node);
    assertFalse(checker.shouldReportThis(node, node));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    String jsSource = "var x = this.y;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, node, null);
    assertTrue(checker.shouldReportThis(node, null));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    String jsSource = "var x = {}; x.y = this;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, node);
    assertFalse(checker.shouldReportThis(node, node));
  }

  @Test
  public void testShouldReportThisInsidePrototypeMethod() {
    String jsSource = "var Proto = function() {}; Proto.prototype.m = function() { this.x = 1; };";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, Node.newName("m"));
    assertFalse(checker.shouldReportThis(Node.newName("m"), null));
  }

  @Test
  public void testShouldReportThisInsideFunctionAnnotatedWithThis() {
    String jsSource = "/** @this */ function Foo() { this.x = 1; }";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, Node.newName("Foo"));
    assertTrue(checker.shouldReportThis(Node.newName("Foo"), null));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisInsideFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    String jsSource = "function Foo() { this.x = 1; }";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, Node.newName("Foo"));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisInsideFunctionWithParentFunctionAnnotatedWithThis() {
    String jsSource = "/** @this */ function Parent() {\n" +
        "  function Child() { this.x = 1; } // should not report this\n" +
        "}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, Node.newName("Child"));
  }

  @Test
  public void testShouldIgnoreGlobalThis() {
    String jsSource = "var x = this;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, null);
    assertFalse(checker.hasDiagnostics());
  }

  @Test
  public void testShouldReportDiagnosticsForMultipleGlobalThisUses() {
    String jsSource = "var x = this; var y = this;";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, null);
    assertTrue(checker.hasDiagnostics());
    assertEquals(1, checker.getDiagnostics().length);
    assertEquals(DiagnosticType.WARNING, checker.getDiagnostics()[0].getType());
  }

  @Test
  public void testShouldIgnoreDiagnosticsForNonGlobalThisUses() {
    String jsSource = "function Foo() {\n" +
        "  function Bar() {\n" +
        "    this.x = 1;\n" +
        "  }\n" +
        "}";
    Node node = AbstractCompiler.compile(jsSource, null, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    checker.visit(traversal, null, Node.newName("Bar"));
    assertFalse(checker.hasDiagnostics());
  }

  private boolean hasDiagnostics(CheckGlobalThis checker) {
    return checker.getDiagnostics().length > 0;
  }
}