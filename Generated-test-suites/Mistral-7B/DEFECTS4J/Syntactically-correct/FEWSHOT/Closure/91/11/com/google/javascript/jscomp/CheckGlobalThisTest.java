package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalContexts() {
    Node root = new Node(Token.SCRIPT);
    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, root);
    assertThat(checker.shouldTraverse(traversal, null, root)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunctions() {
    Node constructor = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setIsConstructor(true);
    constructor.setJSDocInfo(jsDocInfo);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, constructor);
    assertThat(checker.shouldTraverse(traversal, null, constructor)).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeFunctions() {
    Node prototypeFunction = new Node(Token.FUNCTION);
    Node prototype = new Node(Token.GETPROP);
    prototype.setLastChild(new Node(Token.STRING, "prototype"));
    prototypeFunction.setParent(prototype);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, prototypeFunction);
    assertThat(checker.shouldTraverse(traversal, null, prototypeFunction)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    Node functionWithThisAnnotation = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setHasThisType(true);
    functionWithThisAnnotation.setJSDocInfo(jsDocInfo);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, functionWithThisAnnotation);
    assertThat(checker.shouldTraverse(traversal, null, functionWithThisAnnotation)).isFalse();
  }

  @Test
  public void testShouldNotTraverseObjectLiterals() {
    Node objectLiteral = new Node(Token.BLOCK);
    Node key = new Node(Token.STRING, "x");
    Node value = new Node(Token.ASSIGN, null, key, new Node(Token.NUMBER, 42));
    objectLiteral.addChild(value);

    Node function = new Node(Token.FUNCTION);
    Node lhs = new Node(Token.GETPROP, null, function);
    lhs.setLastChild(new Node(Token.STRING, "prototype"));
    function.setParent(objectLiteral);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, function);
    assertThat(checker.shouldTraverse(traversal, null, function)).isFalse();
  }

  @Test
  public void testShouldNotTraverseGetters() {
    Node getter = new Node(Token.GETPROP);
    getter.setLastChild(new Node(Token.STRING, "prototype"));

    Node function = new Node(Token.FUNCTION);
    Node lhs = new Node(Token.ASSIGN, null, getter, new Node(Token.NUMBER, 42));
    function.setParent(new Node(Token.SCRIPT));
    function.setFirstChild(lhs);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, function);
    assertThat(checker.shouldTraverse(traversal, null, function)).isFalse();
  }

  @Test
  public void testShouldNotTraverseSetters() {
    Node setter = new Node(Token.SETPROP);
    setter.setLastChild(new Node(Token.STRING, "prototype"));

    Node function = new Node(Token.FUNCTION);
    Node lhs = new Node(Token.ASSIGN, null, new Node(Token.GETPROP), setter);
    function.setParent(new Node(Token.SCRIPT));
    function.setFirstChild(lhs);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, function);
    assertThat(checker.shouldTraverse(traversal, null, function)).isFalse();
  }

  @Test
  public void testShouldTraverseNonPrototypeProperties() {
    Node function = new Node(Token.FUNCTION);
    Node lhs = new Node(Token.ASSIGN, null, new Node(Token.GETPROP), new Node(Token.STRING, "x"));
    function.setParent(new Node(Token.SCRIPT));
    function.setFirstChild(lhs);

    when(compiler.getTraverser()).thenReturn(traversal);
    checker.shouldTraverse(traversal, null, function);
    assertThat(checker.shouldTraverse(traversal, null, function)).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node left = new Node(Token.THIS);
    Node right = new Node(Token.NUMBER, 42);
    Node assign = new Node(Token.ASSIGN, null, left, right);
    Node parent = new Node(Token.SCRIPT);
    parent.setFirstChild(assign);

    when(compiler.getTraverser()).thenReturn(traversal);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    checker.visit(traversal, null, parent);
    compiler.report.verify(traversal).error(errorNodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture());
    assertNull(checker.assignLhsChild);
    assertThat(errorNodeCaptor.getValue()).isSameInstanceAs(left);
    assertThat(levelCaptor.getValue()).isEqualTo(CheckLevel.WARNING);
    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node left = new Node(Token.THIS);
    Node property = new Node(Token.GETPROP, null, left);
    Node parent = new Node(Token.SCRIPT);
    parent.setFirstChild(property);

    when(compiler.getTraverser()).thenReturn(traversal);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    checker.visit(traversal, null, parent);
    compiler.report.verify(traversal).error(errorNodeCaptor.capture(), levelCaptor.capture(), diagnosticTypeCaptor.capture());
    assertNull(checker.assignLhsChild);
    assertThat(errorNodeCaptor.getValue()).isSameInstanceAs(left);
    assertThat(levelCaptor.getValue()).isEqualTo(CheckLevel.WARNING);
    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }
}