package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode(Token.FUNCTION, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_PrototypeMethod() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parent = Node.newNameNode("Object", Node.NORMAL);
    Node node = Node.newFunctionNode(Token.FUNCTION, null, null, parent);
    node.setName("prototypeMethod");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_Constructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode(Token.FUNCTION, new Node[0], null, null);
    node.setIsConstructor(true);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    JSDocInfo jsDoc = Node.newJsDocNode("this: MyType");
    node.setJSDocInfo(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node node = Node.newFunctionNode(Token.FUNCTION, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parent = Node.newNameNode("assignParent", Node.NORMAL);
    Node left = Node.newNameNode("this", Node.NORMAL);
    Node right = Node.newNameNode("right", Node.NORMAL);
    Node assign = Node.newAssignNode(Token.ASSIGN, left, right);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    NodeTraversal.Callback callback = mock(NodeTraversal.Callback.class);
    when(callback.shouldTraverse(traversal, Node.ANY, Node.ANY)).thenReturn(true);
    when(traversal.traverse(Node.ANY, callback)).thenReturn(null);

    when(assign.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(Node.newBlockNode(Node.BLOCK, new Node[0]));

    assertTrue(checker.shouldTraverse(traversal, left, parent));
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parent = Node.newNameNode("assignParent", Node.NORMAL);
    Node left = Node.newNameNode("left", Node.NORMAL);
    Node thisNode = Node.newNameNode("this", Node.NORMAL);
    Node assign = Node.newAssignNode(Token.ASSIGN, left, thisNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    NodeTraversal.Callback callback = mock(NodeTraversal.Callback.class);
    when(callback.shouldTraverse(traversal, Node.ANY, Node.ANY)).thenReturn(true);
    when(traversal.traverse(Node.ANY, callback)).thenReturn(null);

    when(assign.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(Node.newBlockNode(Node.BLOCK, new Node[0]));

    assertFalse(checker.shouldTraverse(traversal, thisNode, parent));
  }

  @Test
  public void testShouldTraverse_GetProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parentPrototype = Node.newGetPropNode(Token.GETPROP, Node.newNameNode("Object", Node.NORMAL), "prototype");
    Node parent = Node.newNameNode("parent", Node.NORMAL);
    Node thisNode = Node.newNameNode("this", Node.NORMAL);
    Node get = Node.newGetNode(Token.GETPROP, thisNode, "property");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    NodeTraversal.Callback callback = mock(NodeTraversal.Callback.class);
    when(callback.shouldTraverse(traversal, Node.ANY, Node.ANY)).thenReturn(true);
    when(traversal.traverse(Node.ANY, callback)).thenReturn(null);

    when(get.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(parentPrototype);

    assertTrue(checker.shouldTraverse(traversal, thisNode, parentPrototype));
  }

  @Test
  public void testShouldTraverse_GetPropertyWithPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parentPrototype = Node.newGetPropNode(Token.GETPROP, Node.newNameNode("Object", Node.NORMAL), "prototype");
    Node parent = Node.newGetPropNode(Token.GETPROP, Node.newNameNode("parent", Node.NORMAL), "prototypeProperty");
    Node thisNode = Node.newNameNode("this", Node.NORMAL);
    Node get = Node.newGetNode(Token.GETPROP, thisNode, "property");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    NodeTraversal.Callback callback = mock(NodeTraversal.Callback.class);
    when(callback.shouldTraverse(traversal, Node.ANY, Node.ANY)).thenReturn(true);
    when(traversal.traverse(Node.ANY, callback)).thenReturn(null);

    when(get.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(parentPrototype);

    assertFalse(checker.shouldTraverse(traversal, thisNode, parentPrototype));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parent = Node.newNameNode("parent", Node.NORMAL);
    Node thisNode = Node.newNameNode("this", Node.NORMAL);

    assertTrue(checker.shouldReportThis(thisNode, parent));
  }

  @Test
  public void testShouldReportThis_LeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node parent = Node.newNameNode("parent", Node.NORMAL);
    Node left = Node.newNameNode("this", Node.NORMAL);
    Node right = Node.newNameNode("right", Node.NORMAL);
    Node assign = Node.newAssignNode(Token.ASSIGN, left, right);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY)).thenReturn(Node.newErrorNode(null));

    assertTrue(checker.shouldReportThis(left, parent));
  }
}