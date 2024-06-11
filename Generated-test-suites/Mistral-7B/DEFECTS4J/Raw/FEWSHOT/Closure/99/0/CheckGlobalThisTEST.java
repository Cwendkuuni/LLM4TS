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
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNameNode("foo");
    Node grandParent = Node.newNameNode("bar");

    when(traversal.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    boolean result = checker.shouldTraverse(traversal, node, parent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo jsDoc = Node.newJsDocInfo();
    jsDoc.setIsConstructor(true);
    node.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo jsDoc = Node.newJsDocInfo();
    jsDoc.setThisType(true);
    node.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo jsDoc = Node.newJsDocInfo();
    jsDoc.setIsOverride(true);
    node.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionNotInBlockOrScriptOrNameOrAssign() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newBlockNode();
    parent.setType(Token.BLOCK);

    when(traversal.getParent()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignParent = Node.newAssignNode();
    assignParent.setType(Token.ASSIGN);
    Node lhs = Node.newNameNode("x");

    when(traversal.getParent()).thenReturn(assignParent);
    when(assignParent.getFirstChild()).thenReturn(lhs);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignParent = Node.newAssignNode();
    assignParent.setType(Token.ASSIGN);
    Node lhs = Node.newGetPropNode();
    lhs.setPropertyName("prototype");
    lhs.setLastChild(Node.newNameNode("x"));

    when(traversal.getParent()).thenReturn(assignParent);
    when(assignParent.getFirstChild()).thenReturn(lhs);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypePropertySubProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignParent = Node.newAssignNode();
    assignParent.setType(Token.ASSIGN);
    Node lhs = Node.newGetPropNode();
    lhs.setPropertyName("prototype");
    lhs.setLastChild(Node.newGetPropNode());
    lhs.getLastChild().setPropertyName("x");

    when(traversal.getParent()).thenReturn(assignParent);
    when(assignParent.getFirstChild()).thenReturn(lhs);

    boolean result = checker.shouldTraverse(traversal, node, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignParent = Node.newAssignNode();
    assignParent.setType(Token.ASSIGN);
    Node lhs = Node.newNameNode("this");

    when(traversal.getParent()).thenReturn(assignParent);
    when(assignParent.getFirstChild()).thenReturn(lhs);

    checker.visit(traversal, null, null);

    DiagnosticType diagnosticType = checker.GLOBAL_THIS;
    Node errorNode = assignParent;

    compiler.report(traversal.makeError(errorNode, checker.level, diagnosticType));

    assertNull(node.getJSDocInfo());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getParent = Node.newGetPropNode();
    getParent.setType(Token.GETPROP);
    Node lhs = Node.newNameNode("this");

    when(traversal.getParent()).thenReturn(getParent);
    when(getParent.getFirstChild()).thenReturn(lhs);

    checker.visit(traversal, null, null);

    DiagnosticType diagnosticType = checker.GLOBAL_THIS;
    Node errorNode = getParent;

    compiler.report(traversal.makeError(errorNode, checker.level, diagnosticType));

    assertNull(node.getJSDocInfo());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignParent = Node.newAssignNode();
    assignParent.setType(Token.ASSIGN);
    Node rhs = Node.newNameNode("this");

    when(traversal.getParent()).thenReturn(assignParent);
    when(assignParent.getLastChild()).thenReturn(rhs);

    checker.visit(traversal, null, null);

    DiagnosticType diagnosticType = checker.GLOBAL_THIS;
    Node errorNode = node;

    assertNull(compiler.report(traversal.makeError(errorNode, checker.level, diagnosticType)));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newNameNode("this");

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getParent = Node.newGetPropNode();
    getParent.setType(Token.GETPROP);
    Node rhs = Node.newNameNode("this");

    when(traversal.getParent()).thenReturn(getParent);
    when(getParent.getLastChild()).thenReturn(rhs);

    checker.visit(traversal, null, null);

    DiagnosticType diagnosticType = checker.GLOBAL_THIS;
    Node errorNode = node;

    assertNull(compiler.report(traversal.makeError(errorNode, checker.level, diagnosticType)));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo jsDoc = Node.newJsDocInfo();
    jsDoc.setIsConstructor(true);
    node.setJSDocInfo(jsDoc);

    JSDocInfo result = checker.getFunctionJsDocInfo(node);

    assertThat(result).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithoutAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo result = checker.getFunctionJsDocInfo(node);

    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithJsDoc() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode();

    JSDocInfo jsDoc = Node.newJsDocInfo();
    jsDoc.setIsConstructor(false);
    node.setJSDocInfo(jsDoc);

    JSDocInfo result = checker.getFunctionJsDocInfo(node);

    assertThat(result).isEqualTo(jsDoc);
  }

  @Test
  public void testIsGetPropertyAccessNode() {
    boolean result = CheckGlobalThis.NodeUtil.isGet(Node.newAssignNode());

    assertFalse(result);
  }

  @Test
  public void testIsGetPropertyAccessNodeGetProp() {
    boolean result = CheckGlobalThis.NodeUtil.isGet(Node.newGetPropNode());

    assertTrue(result);
  }
}