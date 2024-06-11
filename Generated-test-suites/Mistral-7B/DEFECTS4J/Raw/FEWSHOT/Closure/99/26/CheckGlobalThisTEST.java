package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    Node parent = Node.newInstance(Token.IF, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertTrue(checker.shouldTraverse(traversal, node, parent));

    parent = Node.newInstance(Token.SCRIPT, null);

    assertTrue(checker.shouldTraverse(traversal, node, parent));

    parent = Node.newInstance(Token.ASSIGN, null);
    Node lhs = Node.newInstance(Token.NAME, "x", null);
    Node rhs = Node.newInstance(Token.STRING_LITERAL, "string", null);

    parent.setFirstChild(lhs);
    parent.setLastChild(rhs);

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldTraverseFunction() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldNotTraverseFunctionWithConstructorAnnotation() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    node.setJSDocInfo(JSDocInfo.newBuilder()
        .isConstructor(true)
        .build());
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldNotTraverseFunctionWithThisAnnotation() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    node.setJSDocInfo(JSDocInfo.newBuilder()
        .hasThisType(true)
        .build());
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldNotTraverseFunctionWithOverrideAnnotation() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    node.setJSDocInfo(JSDocInfo.newBuilder()
        .isOverride(true)
        .build());
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldTraverseFunctionWithoutAnnotation() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node node = Node.newInstance(Token.FUNCTION, "func1", null);
    Node parent = Node.newInstance(Token.SCRIPT, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldNotTraverseAssignmentToPrototypeProperty() {
    Node parent = Node.newInstance(Token.ASSIGN, null);
    Node lhs = Node.newInstance(Token.GETPROP, null, null);
    Node prototype = Node.newInstance(Token.STRING_LITERAL, "prototype", null);
    Node rhs = Node.newInstance(Token.NAME, "x", null);

    lhs.setLastChild(prototype);

    parent.setFirstChild(lhs);
    parent.setLastChild(rhs);

    Node node = Node.newInstance(Token.NAME, "x", null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldTraverseAssignmentToNonPrototypeProperty() {
    Node parent = Node.newInstance(Token.ASSIGN, null);
    Node lhs = Node.newInstance(Token.NAME, "x", null);
    Node rhs = Node.newInstance(Token.NAME, "y", null);

    parent.setFirstChild(lhs);
    parent.setLastChild(rhs);

    Node node = Node.newInstance(Token.NAME, "x", null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void shouldReportGlobalThis() {
    Node parent = Node.newInstance(Token.ASSIGN, null);
    Node lhs = Node.newInstance(Token.THIS, null, null);
    Node rhs = Node.newInstance(Token.NAME, "x", null);

    parent.setFirstChild(lhs);
    parent.setLastChild(rhs);

    Node node = Node.newInstance(Token.THIS, null, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    checker.visit(traversal, node, parent);

    assertTrue(compiler.reportedErrorWithType(traversal.getCurrentError().getType(), "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void shouldNotReportThisInPrototypeMethod() {
    Node parent = Node.newInstance(Token.GETPROTOTYPEOF, null, null);
    Node constructor = Node.newInstance(Token.FUNCTION, null, null);
    Node prototype = Node.newInstance(Token.GETPROP, null, null);
    Node thisNode = Node.newInstance(Token.THIS, null, null);

    constructor.setJSDocInfo(JSDocInfo.newBuilder()
        .isConstructor(true)
        .build());

    prototype.setFirstChild(thisNode);
    prototype.setLastChild(Node.newInstance(Token.RETURN, null));

    parent.setFirstChild(constructor);
    parent.setLastChild(prototype);

    Node node = Node.newInstance(Token.THIS, null, null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.WARNING));

    checker.visit(traversal, node, parent);

    assertFalse(compiler.reportedErrorWithType(traversal.getCurrentError().getType(), "JSC_USED_GLOBAL_THIS"));
  }
}