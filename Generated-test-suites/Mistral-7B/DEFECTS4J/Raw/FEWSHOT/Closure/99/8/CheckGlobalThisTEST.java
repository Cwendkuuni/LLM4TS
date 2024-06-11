package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private Diagnostics diagnostics;
  private NodeTraversal traversal;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    diagnostics = mock(Diagnostics.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node scriptNode = Node.newScriptNode("function foo() { this.bar = 1; }");
    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithAnnotations() {
    Node constructorNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        "Constructor",
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node thisAnnotatedNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        "@this",
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node overrideAnnotatedNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        "@override",
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node annotatedFunctionNode = Node.newBlockNode();
    annotatedFunctionNode.appendChild(constructorNode);
    annotatedFunctionNode.appendChild(thisAnnotatedNode);
    annotatedFunctionNode.appendChild(overrideAnnotatedNode);

    when(traversal.getCurrentScope()).thenReturn(Node.SCOPE_GLOBAL);
    when(traversal.getParent()).thenReturn(Node.nullNode());

    assertFalse(checker.shouldTraverse(traversal, thisAnnotatedNode, null));
    assertFalse(checker.shouldTraverse(traversal, overrideAnnotatedNode, null));
    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsNotInGlobalContext() {
    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        "foo",
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(functionNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);
    when(traversal.getParent()).thenReturn(Node.SCOPE_GLOBAL);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseAssignments() {
    Node assignNode = Node.newAssignmentNode(
        Token.ASSIGN,
        Node.newThisNode(),
        Node.newPropertyAccessNode("bar"));

    Node scriptNode = Node.newScriptNode("var a = this; a.bar = 1;");

    Node leftSide = Node.newAssignmentTargetNode(Node.getFirstChild(scriptNode));
    Node rightSide = Node.newPropertyAccessNode("bar");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);
    when(traversal.getParent()).thenReturn(leftSide);

    assertTrue(checker.shouldTraverse(traversal, assignNode, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
    Node assignmentNode = Node.newAssignmentNode(
        Token.ASSIGN,
        Node.newThisNode(),
        Node.newPropertyAccessNode("prototype.bar"));

    Node scriptNode = Node.newScriptNode("var a = this; a.prototype.bar = 1;");

    Node leftSide = Node.newAssignmentTargetNode(Node.getFirstChild(scriptNode));
    Node rightSide = Node.newPropertyAccessNode("prototype.bar");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);
    when(traversal.getParent()).thenReturn(leftSide);

    assertFalse(checker.shouldTraverse(traversal, assignmentNode, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeSubproperties() {
    Node assignmentNode = Node.newAssignmentNode(
        Token.ASSIGN,
        Node.newThisNode(),
        Node.newPropertyAccessNode("prototype.foo.bar"));

    Node scriptNode = Node.newScriptNode("var a = this; a.prototype.foo.bar = 1;");

    Node leftSide = Node.newAssignmentTargetNode(Node.getFirstChild(scriptNode));
    Node rightSide = Node.newPropertyAccessNode("prototype.foo.bar");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);
    when(traversal.getParent()).thenReturn(leftSide);

    assertFalse(checker.shouldTraverse(traversal, assignmentNode, null));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = Node.newThisNode();

    Node scriptNode = Node.newScriptNode("this.bar = 1;");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(Node.SCOPE_GLOBAL);
    when(traversal.getParent()).thenReturn(Node.nullNode());

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testShouldNotReportThisInsideFunction() {
    Node thisNode = Node.newThisNode();

    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        "foo",
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node scriptNode = Node.newScriptNode("function foo() { this.bar = 1; }");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(blockNode);
    when(traversal.getParent()).thenReturn(functionNode);

    checker.visit(traversal, thisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeMethod() {
    Node thisNode = Node.newThisNode();

    Node prototypeNode = Node.newPropertyAccessNode("prototype");
    Node constructorNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node prototypeFunctionNode = Node.newBlockNode();
    prototypeFunctionNode.appendChild(constructorNode);

    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node scriptNode = Node.newScriptNode("function foo() { this.bar = 1; }");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(prototypeFunctionNode);
    when(traversal.getParent()).thenReturn(prototypeNode);

    checker.visit(traversal, thisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldReportThisInsideGetPropertyAccess() {
    Node globalThisNode = Node.newThisNode();

    Node propertyNode = Node.newIdentifierNode("bar");

    Node scriptNode = Node.newScriptNode("this.bar = 1; var x = this.bar;");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(Node.SCOPE_GLOBAL);
    when(traversal.getParent()).thenReturn(propertyNode);

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testShouldReportThisInsideFunctionPropertyAccess() {
    Node globalThisNode = Node.newThisNode();

    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node propertyNode = Node.newIdentifierNode("bar");

    Node scriptNode = Node.newScriptNode("function foo() { this.bar = 1; var x = this.bar; }");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(functionNode);
    when(traversal.getParent()).thenReturn(propertyNode);

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testShouldNotReportThisInsideFunctionCall() {
    Node globalThisNode = Node.newThisNode();

    Node functionCallNode = Node.newCallNode(
        Node.newIdentifierNode("foo"),
        ImmutableList.of(),
        ImmutableList.of(),
        null);

    Node scriptNode = Node.newScriptNode("this.foo();");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(Node.SCOPE_GLOBAL);
    when(traversal.getParent()).thenReturn(functionCallNode);

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldReportThisInsidePrototypeFunctionCall() {
    Node globalThisNode = Node.newThisNode();

    Node prototypeNode = Node.newPropertyAccessNode("prototype");
    Node constructorNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    Node prototypeFunctionNode = Node.newBlockNode();
    prototypeFunctionNode.appendChild(constructorNode);

    Node functionCallNode = Node.newCallNode(
        Node.newIdentifierNode("foo"),
        ImmutableList.of(),
        ImmutableList.of(),
        null);

    Node scriptNode = Node.newScriptNode("this.prototype.foo();");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(prototypeFunctionNode);
    when(traversal.getParent()).thenReturn(prototypeNode);

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testShouldReportThisInsideVariableDeclaration() {
    Node globalThisNode = Node.newThisNode();

    Node varNode = Node.newVarNode(
        Token.VAR,
        "x",
        null,
        Node.newThisNode());

    Node scriptNode = Node.newScriptNode("var x = this;");

    Node blockNode = Node.newBlockNode();
    blockNode.appendChild(scriptNode);

    when(traversal.getCurrentScope()).thenReturn(Node.SCOPE_GLOBAL);
    when(traversal.getParent()).thenReturn(varNode);

    checker.visit(traversal, globalThisNode, null);

    assertTrue(diagnostics.getDiagnostics().isEmpty());
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getDiagnostics().get(0).getCode());
  }

  private ImmutableList<Node> getFunctionJsDocInfo(Node n) {
    return checker.getFunctionJsDocInfo(n);
  }

  @Test
  public void testGetFunctionJsDocInfoReturnsNullIfNoJsDocPresent() {
    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    assertNull(getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testGetFunctionJsDocInfoReturnsJsDocWhenPresent() {
    JSDocInfo jsDoc = Node.newJsDocNode();

    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        ImmutableList.of(),
        null,
        null,
        null,
        null,
        null,
        ImmutableList.of(),
        null,
        null,
        null,
        null);

    functionNode.setJSDocInfo(jsDoc);

    assertEquals(jsDoc, getFunctionJsDocInfo(functionNode));
  }
}