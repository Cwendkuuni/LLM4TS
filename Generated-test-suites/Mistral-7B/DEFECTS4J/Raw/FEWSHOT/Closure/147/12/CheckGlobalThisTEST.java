package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node globalContext = Node.newScopeNode(nodeFactory);
    assertThat(checker.shouldTraverse(null, null, globalContext)).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node nonGlobalContext = Node.newScopeNode(nodeFactory);
    assertThat(checker.shouldTraverse(null, null, nonGlobalContext)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node parentNode = Node.newNameNode("a", nodeFactory);
    Node blockNode = Node.newBlockNode(nodeFactory);
    Node scriptNode = Node.newScriptNode(nodeFactory);

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(blockNode);
    when(blockNode.getParent()).thenReturn(scriptNode);

    assertThat(checker.shouldTraverse(null, null, functionNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node constructorAnnotation = Node.newIdentifierNode("constructor", nodeFactory);
    Node constructorAnnotationValue = Node.newBooleanLiteral(true, nodeFactory);
    Node constructorAnnotationNode = Node.newExpressionStatement(
        Node.newAssignment(
            functionNode, Node.newMemberPropertyAccess(functionNode, "constructor", null), constructorAnnotation),
        nodeFactory);

    when(functionNode.getParent()).thenReturn(null);
    when(functionNode.getJSDocInfo()).thenReturn(new JSDocInfo(constructorAnnotationNode, null));

    assertThat(checker.shouldTraverse(null, null, functionNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node thisAnnotation = Node.newIdentifierNode("this", nodeFactory);
    Node thisAnnotationValue = Node.newObjectLiteralNode(nodeFactory);
    Node thisAnnotationNode = Node.newExpressionStatement(
        Node.newAssignment(
            functionNode, Node.newMemberPropertyAccess(functionNode, "this", null), thisAnnotation),
        nodeFactory);

    when(functionNode.getParent()).thenReturn(null);
    when(functionNode.getJSDocInfo()).thenReturn(new JSDocInfo(null, thisAnnotationNode));

    assertThat(checker.shouldTraverse(null, null, functionNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node overrideAnnotation = Node.newIdentifierNode("override", nodeFactory);
    Node overrideAnnotationValue = Node.newBooleanLiteral(true, nodeFactory);
    Node overrideAnnotationNode = Node.newExpressionStatement(
        Node.newAssignment(
            functionNode, Node.newMemberPropertyAccess(functionNode, "override", null), overrideAnnotation),
        nodeFactory);

    when(functionNode.getParent()).thenReturn(null);
    when(functionNode.getJSDocInfo()).thenReturn(new JSDocInfo(null, overrideAnnotationNode));

    assertThat(checker.shouldTraverse(null, null, functionNode)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node blockNode = Node.newBlockNode(nodeFactory);
    Node assignNode = Node.newAssignment(Node.newNameNode("a", nodeFactory), Node.newIdentifierNode("b", nodeFactory), nodeFactory);
    Node scriptNode = Node.newScriptNode(nodeFactory);

    when(functionNode.getParent()).thenReturn(blockNode);
    when(blockNode.getParent()).thenReturn(scriptNode);

    assertThat(checker.shouldTraverse(null, null, functionNode)).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeProperty() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node prototypeProperty = Node.newGetPropertyAccessNode(Node.newIdentifierNode("this", nodeFactory), "prototype", null);
    Node prototypePropertyValue = Node.newObjectLiteralNode(nodeFactory);

    when(functionNode.getParent()).thenReturn(null);
    when(functionNode.getJSDocInfo()).thenReturn(null);

    Node functionBody = Node.newBlockNode(nodeFactory);
    Node prototypePropertyAssignment = Node.newAssignment(prototypeProperty, prototypePropertyValue, nodeFactory);
    functionBody.addChildToEnd(prototypePropertyAssignment);
    functionNode.setBody(functionBody);

    assertThat(checker.shouldTraverse(null, null, functionNode)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubProperty() {
    Node functionNode = Node.newFunctionNode(nodeFactory);
    Node prototypeProperty = Node.newGetPropertyAccessNode(Node.newIdentifierNode("this", nodeFactory), "prototype", null);
    Node prototypeSubProperty = Node.newGetPropertyAccessNode(prototypeProperty, "subProperty", null);
    Node prototypeSubPropertyValue = Node.newObjectLiteralNode(nodeFactory);

    when(functionNode.getParent()).thenReturn(null);
    when(functionNode.getJSDocInfo()).thenReturn(null);

    Node functionBody = Node.newBlockNode(nodeFactory);
    Node prototypeSubPropertyAssignment = Node.newAssignment(prototypeSubProperty, prototypeSubPropertyValue, nodeFactory);
    functionBody.addChildToEnd(prototypeSubPropertyAssignment);
    functionNode.setBody(functionBody);

    assertThat(checker.shouldTraverse(null, null, functionNode)).isFalse();
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node leftSideOfAssignment = Node.newIdentifierNode("this", nodeFactory);
    Node rightSideOfAssignment = Node.newIdentifierNode("a", nodeFactory);
    Node assignNode = Node.newAssignment(leftSideOfAssignment, rightSideOfAssignment, nodeFactory);

    Node parentNode = Node.newNameNode("a", nodeFactory);

    assertThat(checker.shouldTraverse(null, parentNode, leftSideOfAssignment)).isTrue();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node leftSideOfAssignment = Node.newIdentifierNode("this", nodeFactory);
    Node rightSideOfAssignment = Node.newGetPropertyAccessNode(Node.newIdentifierNode("a", nodeFactory), "prototype", null);

    Node assignNode = Node.newAssignment(leftSideOfAssignment, rightSideOfAssignment, nodeFactory);

    Node parentNode = Node.newNameNode("a", nodeFactory);

    assertThat(checker.shouldTraverse(null, parentNode, rightSideOfAssignment)).isFalse();
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubProperty() {
    Node leftSideOfAssignment = Node.newIdentifierNode("this", nodeFactory);
    Node prototypeProperty = Node.newGetPropertyAccessNode(Node.newIdentifierNode("a", nodeFactory), "prototype", null);
    Node prototypeSubProperty = Node.newGetPropertyAccessNode(prototypeProperty, "subProperty", null);

    Node rightSideOfAssignment = Node.newIdentifierNode("b", nodeFactory);
    Node assignNode = Node.newAssignment(leftSideOfAssignment, rightSideOfAssignment, nodeFactory);

    Node parentNode = Node.newNameNode("a", nodeFactory);

    assertThat(checker.shouldTraverse(null, parentNode, prototypeSubProperty)).isFalse();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalContext = Node.newScopeNode(nodeFactory);
    Node thisNode = Node.newIdentifierNode("this", nodeFactory);

    Node parentNode = Node.newNameNode("a", nodeFactory);

    Node traverser = NodeTraversal.newEmptyTraverser(nodeFactory, checker);

    traverser.traverse(globalContext);
    traverser.traverse(thisNode);

    assertThat(checker.compiler).calledOnce();
    assertThat(checker.compiler.report)
        .calledOn(traverser)
        .withArgument(equalTo(new DiagnosticPosition(thisNode.getStartPosition())))
        .withArgument(equalTo(level))
        .withArgument(equalTo(GLOBAL_THIS));
  }

  private DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");
}