package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("function foo() {}");
    Node parentNode = createBlockNode("var x = function foo() {}");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(level), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    assertTrue(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("@constructor function foo() {}");
    Node parentNode = createBlockNode("var x = function foo() {}");

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("function foo() { @this }");
    Node parentNode = createBlockNode("var x = function foo() {}");

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("function foo() { @override }");
    Node parentNode = createBlockNode("var x = function foo() {}");

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("function foo() {}");
    Node prototypeNode = createIdentifierNode("prototype");
    Node parentNode = createGetPropertyNode(prototypeNode, functionNode);

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccessMultipleLevelsDeep() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node functionNode = createFunctionNode("function foo() {}");
    Node prototypeNode1 = createIdentifierNode("prototype");
    Node prototypeNode2 = createIdentifierNode("prototype");
    Node parentNode = createGetPropertyNode(prototypeNode1, createGetPropertyNode(prototypeNode2, functionNode));

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignLeftSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node assignNode = createAssignNode("a = this");
    Node functionNode = createFunctionNode("function foo() {}");
    Node parentNode = createBlockNode("var a = function foo() {}");

    assertTrue(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithAssignRightSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node assignNode = createAssignNode("a = function foo() {}");
    Node parentNode = createBlockNode("var a = {}");

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccessOnLeftSideOfAssign() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node assignNode = createAssignNode("a.prototype = this");
    Node functionNode = createFunctionNode("function foo() {}");
    Node parentNode = createBlockNode("var a = {}");

    assertFalse(callback.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldReportThisWithPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.VERBOSE;
    NodeTraversal traversal = mock(NodeTraversal.class);
    Callback callback = new CheckGlobalThis(compiler, level);

    Node thisNode = createThisNode();
    Node propertyNode = createIdentifierNode("property");
    Node parentNode = createBlockNode("var x = function() { this.property }");

    assertTrue(callback.shouldReportThis(thisNode, parentNode));
  }

  private Node createFunctionNode(String code) {
    Node node = new Node();
    node.setType(Token.FUNCTION);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    node.setCode(code);
    return node;
  }

  private Node createAssignNode(String code) {
    Node node = new Node();
    node.setType(Token.ASSIGN);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    node.setCode(code);
    return node;
  }

  private Node createBlockNode(String code) {
    Node node = new Node();
    node.setType(Token.BLOCK);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    node.setCode(code);
    return node;
  }

  private Node createGetPropertyNode(Node propertyNode, Node objectNode) {
    Node node = new Node();
    node.setType(Token.GETPROP);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    node.setFirstChild(propertyNode);
    node.setLastChild(objectNode);
    return node;
  }

  private Node createIdentifierNode(String name) {
    Node node = new Node();
    node.setType(Token.NAME);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    node.setString(name);
    return node;
  }

  private Node createThisNode() {
    Node node = new Node();
    node.setType(Token.THIS);
    node.setSourcePositionForTesting(new SourcePosition(1, 1));
    return node;
  }
}