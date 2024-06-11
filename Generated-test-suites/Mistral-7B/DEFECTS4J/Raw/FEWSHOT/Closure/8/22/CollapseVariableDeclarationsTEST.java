package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;
import java.util.Set;

/**
 * Test cases for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {

  /**
   * Mock JS Compiler.
   */
  private AbstractCompiler mockCompiler;

  /**
   * Mock Scope.
   */
  private Scope mockScope;

  /**
   * Setup test environment.
   */
  @Before
  public void setUp() {
    mockCompiler = mock(AbstractCompiler.class);
    mockScope = mock(Scope.class);
  }

  /**
   * Test collapsing single variable declaration.
   */
  @Test
  public void testSingleVariableDeclaration() {
    Node root = Node.newTextNode("var a;");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild().getString());
    assertEquals("var a;", root.toString());
  }

  /**
   * Test collapsing multiple variable declarations.
   */
  @Test
  public void testMultipleVariableDeclarations() {
    Node root = Node.newTextNode("var a; var b = 1; var c = 2;");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild().getString());
    assertEquals("var a, b = 1, c = 2;", root.toString());
  }

  /**
   * Test collapsing multiple assignments.
   */
  @Test
  public void testMultipleAssignments() {
    Node root = Node.newTextNode("a = true; b = true; var c = true;");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild().getString());
    assertEquals("var c = b = a = true;", root.toString());
  }

  /**
   * Test collapsing variable declarations inside a function.
   */
  @Test
  public void testVariableDeclarationsInsideFunction() {
    Node root = Node.newTextNode(
        "function foo() {"
            + "var a;"
            + "var b = 1;"
            + "var c = 2;"
            + "}");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild());
    assertEquals("function foo() { var a, b = 1, c = 2; }", root.toString());
  }

  /**
   * Test collapsing variable declarations with JSDoc.
   */
  @Test
  public void testVariableDeclarationsWithJSDoc() {
    Node root = Node.newTextNode(
        "/** @type {number} */"
        + "var a;"
        + "/** @type {string} */"
        + "var b;"
        + "var c = 1;");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild());
    assertEquals(
        "/** @type {number} */ var a;"
        + "/** @type {string} */ var b = c;",
        root.toString());
  }

  /**
   * Test collapsing nested variable declarations.
   */
  @Test
  public void testNestedVariableDeclarations() {
    Node root = Node.newTextNode(
        "function foo() {"
            + "var a;"
            + "function bar() {"
            + "var b;"
            + "var c = 1;"
            + "}"
            + "var d = 2;"
            + "}");
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(mockCompiler);

    collapseVariableDeclarations.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getFirstChild());
    assertEquals(
        "function foo() {"
            + "var a;"
            + "function bar() {"
            + "var b, c = 1;"
            + "}"
            + "var d = 2;"
            + "}",
        root.toString());
  }
}