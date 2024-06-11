package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(new CheckGlobalThis(compiler, CheckLevel.WARNING), null);
  }

  @Test
  public void testShouldTraverse_function() {
    Node functionNode = Node.newFunctionNode("test", null, ImmutableList.of());
    Node parent = Node.newBlockNode();
    Node block = Node.newBlockNode();
    parent.addChildToEnd(functionNode);
    parent.addChildToEnd(block);

    assertThat(checker.shouldTraverse(traversal, functionNode, parent)).isTrue();
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithAtConstructor() {
    Node functionNode = Node.newFunctionNode("test", Node.newThisNode(), ImmutableList.of());
    Node parent = Node.newBlockNode();

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(traversal, functionNode, parent)).isFalse();
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithAtThis() {
    Node functionNode = Node.newFunctionNode("test", Node.newThisNode(), ImmutableList.of());
    Node parent = Node.newBlockNode();

    JSDocInfo jsDocInfo = Node.newJSDocInfo();
    jsDocInfo.setThisType(Node.newThisTypeNode());
    functionNode.setJSDocInfo(jsDocInfo);

    assertThat(checker.shouldTraverse(traversal, functionNode, parent)).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunctionNode("test", null, ImmutableList.of());
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, functionNode, parent)).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithPrototypeMethod() {
    Node functionNode = Node.newFunctionNode("test", null, ImmutableList.of());
    Node prototypeNode = Node.newObjectLiteralNode("proto", ImmutableList.of());
    Node prototypeMethodNode = Node.newFunctionNode("method", Node.newThisNode(), ImmutableList.of());
    Node prototypeMethodPropertyNode = Node.newGetPropertyNode("method", prototypeMethodNode);
    Node prototypePropertyNode = Node.newObjectLiteralNode("__proto__", ImmutableList.of(prototypeMethodPropertyNode));
    Node parent = Node.newObjectLiteralNode("obj", ImmutableList.of(prototypePropertyNode));

    functionNode.addChildToEnd(prototypeMethodNode);
    prototypeNode.addChildToEnd(prototypePropertyNode);
    parent.addChildToEnd(prototypeNode);

    assertThat(checker.shouldTraverse(traversal, functionNode, parent)).isTrue();
  }

  @Test
  public void testShouldTraverse_leftSideOfAssignment() {
    Node leftSide = Node.newGetPropertyNode("a", Node.newThisNode());
    Node assignNode = Node.newAssignNode(leftSide, Node.newNumberLiteral(1));
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, assignNode, parent)).isTrue();
  }

  @Test
  public void testShouldTraverse_leftSideOfAssignment_nested() {
    Node leftSide = Node.newGetPropertyNode("a.b", Node.newThisNode());
    Node assignNode = Node.newAssignNode(leftSide, Node.newNumberLiteral(1));
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, assignNode, parent)).isTrue();
  }

  @Testpublic void testShouldNotTraverse_rightSideOfAssignment() {
    Node leftSide = Node.newGetPropertyNode("a", Node.newThisNode());
    Node rightSide = Node.newGetPropertyNode("b", Node.newThisNode());
    Node assignNode = Node.newAssignNode(leftSide, rightSide);
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, assignNode, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverse_rightSideOfAssignment_prototypeProperty() {
    Node leftSide = Node.newGetPropertyNode("a", Node.newThisNode());
    Node rightSide = Node.newGetPropertyNode("prototype", Node.newObjectLiteralNode("__proto__", ImmutableList.of()));
    Node assignNode = Node.newAssignNode(leftSide, rightSide);
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, assignNode, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverse_rightSideOfAssignment_prototypeSubproperty() {
    Node leftSide = Node.newGetPropertyNode("a", Node.newThisNode());
    Node prototypeNode = Node.newObjectLiteralNode("__proto__", ImmutableList.of());
    Node prototypeMethodNode = Node.newFunctionNode("method", Node.newThisNode(), ImmutableList.of());
    Node prototypeMethodPropertyNode = Node.newGetPropertyNode("method", prototypeMethodNode);
    Node rightSide = Node.newGetPropertyNode("prototype.method", prototypeMethodPropertyNode);
    Node assignNode = Node.newAssignNode(leftSide, rightSide);
    Node parent = Node.newBlockNode();

    assertThat(checker.shouldTraverse(traversal, assignNode, parent)).isFalse();
  }

  @Test
  public void testReportThis() {
    Node thisNode = Node.newThisNode();
    Node parent = Node.newBlockNode();

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    Node assignLhsChild = Node.newGetPropertyNode("a", Node.newThisNode());
    checker.visit(traversal, thisNode, parent);
    checker.visit(traversal, assignLhsChild, parent);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, thisNode, parent);

    Node expectedThisNode = nodeCaptor.getValue();
    CheckLevel expectedLevel = levelCaptor.getValue();
    DiagnosticType expectedType = typeCaptor.getValue();

    compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture());

    Node actualThisNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedThisNode).isSameInstanceAs(thisNode);
    assertThat(expectedLevel).isEqualTo(CheckLevel.WARNING);
    assertThat(expectedType).isEqualTo(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object"));

    assertThat(actualThisNode).isSameInstanceAs(thisNode);
    assertThat(actualLevel).isEqualTo(expectedLevel);
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testReportThis_onPropertyAccess() {
    Node thisNode = Node.newThisNode();
    Node parent = Node.newBlockNode();
    Node getPropertyNode = Node.newGetPropertyNode("a", thisNode);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, thisNode, parent);
    checker.visit(traversal, getPropertyNode, parent);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, thisNode, parent);

    Node expectedThisNode = nodeCaptor.getValue();
    CheckLevel expectedLevel = levelCaptor.getValue();
    DiagnosticType expectedType = typeCaptor.getValue();

    Node actualThisNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedThisNode).isSameInstanceAs(thisNode);
    assertThat(expectedLevel).isEqualTo(CheckLevel.WARNING);
    assertThat(expectedType).isEqualTo(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object"));

    assertThat(actualThisNode).isSameInstanceAs(thisNode);
    assertThat(actualLevel).isEqualTo(expectedLevel);
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testNullParent() {
    Node thisNode = Node.newThisNode();

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, thisNode, null);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, thisNode, null);

    Node expectedThisNode = nodeCaptor.getValue();
    CheckLevel expectedLevel = levelCaptor.getValue();
    DiagnosticType expectedType = typeCaptor.getValue();

    Node actualThisNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedThisNode).isSameInstanceAs(thisNode);
    assertThat(expectedLevel).isEqualTo(CheckLevel.WARNING);
    assertThat(expectedType).isEqualTo(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object"));

    assertThat(actualThisNode).isSameInstanceAs(thisNode);
    assertThat(actualLevel).isEqualTo(expectedLevel);
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testNullChecker() {
    Node thisNode = Node.newThisNode();
    Node parent = Node.newBlockNode();

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    CheckGlobalThis checker = null;
    NodeTraversal traversal = NodeTraversal.create(checker, null);

    checker.visit(traversal, thisNode, parent);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, thisNode, parent);

    Node expectedThisNode = null;
    CheckLevel expectedLevel = null;
    DiagnosticType expectedType = null;

    Node actualThisNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedThisNode).isNull();
    assertThat(expectedLevel).isNull();
    assertThat(expectedType).isNull();

    assertThat(actualThisNode).isNull();
    assertThat(actualLevel).isNull();
    assertThat(actualType).isNull();
  }

  @Test
  public void testNullNode() {
    Node checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, null, Node.newBlockNode());

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, null, Node.newBlockNode());

    Node expectedNode = null;
    CheckLevel expectedLevel = null;
    DiagnosticType expectedType = null;

    Node actualNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedNode).isNull();
    assertThat(expectedLevel).isNull();
    assertThat(expectedType).isNull();

    assertThat(actualNode).isNull();
    assertThat(actualLevel).isNull();
    assertThat(actualType).isNull();
  }

  @Test
  public void testNullParentAndNode() {
    Node checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, null, null);

    when(compiler.report(nodeCaptor.capture(), levelCaptor.capture(), typeCaptor.capture()))
        .thenReturn(null);

    checker.visit(traversal, null, null);

    Node expectedNode = null;
    CheckLevel expectedLevel = null;
    DiagnosticType expectedType = null;

    Node actualNode = nodeCaptor.getValue();
    CheckLevel actualLevel = levelCaptor.getValue();
    DiagnosticType actualType = typeCaptor.getValue();

    assertThat(expectedNode).isNull();
    assertThat(expectedLevel).isNull();
    assertThat(expectedType).isNull();

    assertThat(actualNode).isNull();
    assertThat(actualLevel).isNull();
    assertThat(actualType).isNull();
  }
}