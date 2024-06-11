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
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
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
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = createNode(Token.FUNCTION);
    JSDocInfo jsDoc = createJsDocInfo("@this");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(createNode(Token.PROGRAM));

    assertThat(checker.shouldTraverse(traversal, null, null)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = createNode(Token.FUNCTION);
    JSDocInfo jsDoc = createJsDocInfo("@constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(createNode(Token.PROGRAM));

    assertThat(checker.shouldTraverse(traversal, null, null)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    Node functionNode = createNode(Token.FUNCTION);
    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(createNode(Token.PROGRAM));

    assertThat(checker.shouldTraverse(traversal, null, null)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithNoPlausiblePlaceForThisAnnotation() {
    Node functionNode = createNode(Token.FUNCTION);
    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(createNode(Token.PROGRAM));

    assertThat(checker.shouldTraverse(traversal, null, null)).isTrue();
  }

  @Test
  public void testShouldNotTraversePrototypeMethod() {
    Node functionNode = createNode(Token.FUNCTION);
    Node prototypeNode = createNode(Token.GETPROP, "prototype");
    Node assignNode = createNode(Token.ASSIGN, createNode(Token.IDENT, "a"), functionNode);
    Node assignPropertyNode = createNode(Token.GETPROP, createNode(Token.IDENT, "prototype"), functionNode);
    Node rightSideNode = createNode(Token.IDENT, "b");

    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(assignNode);
    when(assignNode.getFirstChild()).thenReturn(assignLhsChild(assignNode));
    when(assignLhsChild.getNext()).thenReturn(rightSideNode);

    assertThat(checker.shouldTraverse(traversal, rightSideNode, assignNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseGetterWithPrototypeProperty() {
    Node functionNode = createNode(Token.FUNCTION);
    Node prototypeNode = createNode(Token.GETPROP, "prototype");
    Node getterNode = createNode(Token.GETPROP, createNode(Token.IDENT, "a"), functionNode);

    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getCurrentParent()).thenReturn(prototypeNode);

    assertThat(checker.shouldTraverse(traversal, getterNode, prototypeNode)).isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = createNode(Token.ASSIGN, createNode(Token.IDENT, "a"), createNode(Token.THIS));

    when(compiler.getReportingPosition()).thenReturn(assignNode.getPosition());
    when(compiler.getReportingErrorPosition()).thenReturn(assignNode.getPosition());

    checker.visit(traversal, null, null);

    Node errorNode = assignNode;
    AbstractCompiler.ReportedError error =
        (AbstractCompiler.ReportedError) compiler.getLastReportedError();
    assertThat(error).isNotNull();
    assertThat(error.getDiagnosticType()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
    assertThat(error.getErrorCode()).isEqualTo("JSC_USED_GLOBAL_THIS");
    assertThat(error.getMessage()).isEqualTo("dangerous use of the global 'this' object");
    assertThat(error.getNode()).isEqualTo(errorNode);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = createNode(Token.ASSIGN, createNode(Token.IDENT, "a"), createNode(Token.NUMBER, "1"));

    checker.visit(traversal, null, null);

    assertNull(compiler.getLastReportedError());
  }

  @Test
  public void testShouldNotReportThisWithPropertyAccess() {
    Node assignNode = createNode(Token.ASSIGN, createNode(Token.IDENT, "a"), createNode(Token.GETPROP, createNode(Token.IDENT, "this"), null));

    checker.visit(traversal, null, null);

    assertNull(compiler.getLastReportedError());
  }

  private Node assignLhsChild(Node node) {
    Node lhs = node.getFirstChild();
    return lhs;
  }

  private Node createNode(int type, Object... children) {
    Node node = new Node();
    node.setType(type);
    node.setChildren(children);
    return node;
  }

  private JSDocInfo createJsDocInfo(String annotation) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setAnnotation(annotation);
    return jsDoc;
  }
}