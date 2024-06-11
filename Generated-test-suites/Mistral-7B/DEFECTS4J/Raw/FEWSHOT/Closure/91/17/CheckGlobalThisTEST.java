package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node node = new Node(Token.FUNCTION, "function() {}");
    assertTraversesNode(node);
  }

  @Test
  public void testShouldTraverseFunctionWithParentBlock() {
    Node block = new Node(Token.BLOCK, "{}");
    Node function = new Node(Token.FUNCTION, "function() {}", block);
    assertTraversesNode(function);
  }

  @Test
  public void testShouldTraverseFunctionWithParentVariable() {
    Node variable = new Node(Token.VAR, "var a = function() {}", null);
    Node function = variable.getFirstChild();
    assertTraversesNode(function);
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssign() {
    Node assign = new Node(Token.ASSIGN, "a = {}", null);
    Node function = new Node(Token.FUNCTION, "function() {}", assign);
    assertTraversesNode(function);
  }

  @Test
  public void testShouldTraverseFunctionWithParentPrototypeProperty() {
    Node prototype = new Node(Token.GETPROP, "prototype", null);
    Node object = new Node(Token.NAME, "Object", null);
    Node function = new Node(Token.FUNCTION, "function() {}", object);
    Node assign = new Node(Token.ASSIGN, "a = {}", null);
    Node lhs = NodeUtil.createPropertyAccess(assign, "prototype");
    Node rhs = function;

    Node tree = new Node(Token.ASSIGN, "a = {}", assign);
    tree.setFirstChild(lhs);
    tree.setLastChild(rhs);

    assertTraversesNode(rhs);
  }

  @Test
  public void testShouldTraverseFunctionWithParentPrototypeSubProperty() {
    Node prototype = new Node(Token.GETPROP, "prototype", null);
    Node object = new Node(Token.NAME, "Object", null);
    Node property = new Node(Token.IDENT, "property", null);
    Node function = new Node(Token.FUNCTION, "function() {}", object);
    Node assign = new Node(Token.ASSIGN, "a.property = {}", null);
    Node lhs = NodeUtil.createPropertyAccess(assign, "prototype");
    Node rhs = function;

    Node tree = new Node(Token.ASSIGN, "a.property = {}", assign);
    tree.setFirstChild(lhs);
    tree.setLastChild(rhs);

    assertTraversesNode(rhs);
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentGet() {
    Node get = new Node(Token.GETPROP, "get", null);
    Node function = new Node(Token.FUNCTION, "function() {}", get);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentGetter() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setGetter(true);
    Node getter = new Node(Token.NAME, "getter", jsDocInfo);
    Node function = new Node(Token.FUNCTION, "function() {}", getter);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldTraverseFunctionWithParentSet() {
    Node set = new Node(Token.SETPROP, "", null);
    Node function = new Node(Token.FUNCTION, "function() {}", set);
    assertTraversesNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentSetter() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setSetter(true);
    Node setter = new Node(Token.NAME, "setter", jsDocInfo);
    Node function = new Node(Token.FUNCTION, "function() {}", setter);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    Node function = new Node(Token.FUNCTION, "function() {}", jsDocInfo);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(new Node(Token.THIS, null, null));
    Node function = new Node(Token.FUNCTION, "function() {}", jsDocInfo);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setInterface(true);
    Node function = new Node(Token.FUNCTION, "function() {}", jsDocInfo);
    assertDoesNotTraverseNode(function);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    Node function = new Node(Token.FUNCTION, "function() {}", jsDocInfo);
    assertDoesNotTraverseNode(function);
  }

  private void assertTraversesNode(Node node) {
    NodeTraversal traversal = NodeTraversal.create(checker);
    traversal.traverse(node, null);
    assertThat(checker.assignLhsChild).isNull();
  }

  private void assertDoesNotTraverseNode(Node node) {
    NodeTraversal traversal = NodeTraversal.create(checker);
    traversal.traverse(node, null);
    assertThat(checker.assignLhsChild).isNull();
  }
}