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
  @SuppressWarnings("unchecked")
  private static final Callback<NodeTraversal> MOCK_CALLBACK = mock(Callback.class);

  private static final AbstractCompiler MOCK_COMPILER = mock(AbstractCompiler.class);

  @Test
  public void testShouldTraverseGlobalFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(MOCK_COMPILER, CheckLevel.WARNING);
    Node functionNode = Node.newFunctionNode();

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);
    when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, DiagnosticType.warning(null, null))).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    Node parentNode = Node.newNameNode("ParentFunction");
    parentNode.setParent(Node.SCOPE_NODE);
    functionNode.setParent(parentNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(parentNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraversePrototypeMethod() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node objectNode = Node.newObjectLiteralNode();
    Node constructorNode = Node.newFunctionNode();
    constructorNode.setName("ConstructorFunction");
    Node propertyNode = Node.newNameNode("Method");
    constructorNode.setPrototype(objectNode);
    constructorNode.setPrototypeProperties(Node.newObjectLiteralNode()
        .setProperties(Node.newNamePropertyNode("Method", propertyNode)));

    Node functionNode = Node.newFunctionNode();
    propertyNode.setParent(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(constructorNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseConstructorFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    functionNode.setName("ConstructorFunction");
    functionNode.setIsConstructor(true);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    functionNode.setName("FunctionWithThisAnnotation");
    JSDocInfo jsDocInfo = Node.newJsDocInfo("@this {type: 'MyType'}");
    functionNode.setJSDocInfo(jsDocInfo);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithNoObviousThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    functionNode.setName("FunctionWithNoObviousThisAnnotation");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideBlock() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    Node blockNode = Node.newBlockNode();
    functionNode.setParent(blockNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(blockNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideScript() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    Node scriptNode = Node.newScriptNode();
    functionNode.setParent(scriptNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(scriptNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionInsidePrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    Node objectNode = Node.newObjectLiteralNode();
    Node propertyNode = Node.newNameNode("Method");
    Node prototypeNode = Node.newNamePropertyNode("prototype", objectNode);
    prototypeNode.setParent(Node.SCOPE_NODE);
    objectNode.setPrototypeProperties(prototypeNode);
    propertyNode.setParent(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(prototypeNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeSubproperty() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    Node objectNode = Node.newObjectLiteralNode();
    Node propertyNode1 = Node.newNameNode("Property");
    Node prototypeNode = Node.newNamePropertyNode("prototype", objectNode);
    prototypeNode.setParent(Node.SCOPE_NODE);
    objectNode.setPrototypeProperties(prototypeNode);
    Node propertyNode2 = Node.newNamePropertyNode("SubProperty");
    Node getPropNode = Node.newGetPropNode(propertyNode1, propertyNode2);
    propertyNode2.setParent(getPropNode);
    getPropNode.setParent(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(prototypeNode);

    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionOnLeftSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(Node.newNameNode("a"), Node.newNameNode("this"));
    Node leftHandSide = assignNode.getLhs();

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(NodeTraversal.ANY_NODE, CheckLevel.WARNING, DiagnosticType.warning(null, null))).thenReturn(null);

    boolean result = checker.shouldReportThis(leftHandSide, assignNode.getParent());
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionOnRightSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node assignNode = Node.newAssignNode(Node.newNameNode("a"), Node.newNameNode("this"));
    Node rightHandSide = assignNode.getRhs();

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldReportThis(rightHandSide, assignNode.getParent());
    assertThat(result).isFalse();
  }

  @Test
  public void testGetFunctionJsDocInfoReturnsJsDocInfoForFunctionWithJSDoc() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();
    JSDocInfo jsDocInfo = Node.newJsDocInfo("@constructor");
    functionNode.setJSDocInfo(jsDocInfo);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    assertThat(result).isEqualTo(jsDocInfo);
  }

  @Test
  public void testGetFunctionJsDocInfoReturnsNullForFunctionWithNoJSDoc() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    Node functionNode = Node.newFunctionNode();

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    assertNull(result);
  }
}