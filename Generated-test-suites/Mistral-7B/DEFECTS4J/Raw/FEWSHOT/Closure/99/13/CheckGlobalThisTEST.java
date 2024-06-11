package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node root = new Node(Token.SCRIPT, "", 0);

    // Traversable function
    Node functionNode = new Node(Token.FUNCTION, "", 0, 0, 0);
    when(compiler.getRoot()).thenReturn(root);
    when(compiler.getCurrentNode()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(root);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    assertTrue(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Non-traversable function (not global)
    Node functionNode2 = new Node(Token.FUNCTION, "", 0, 0, 0);
    Node parentNode = new Node(Token.NAME, "parent", 0, 0, 0);
    functionNode2.setParent(parentNode);
    when(functionNode2.getParent()).thenReturn(parentNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode2, root));

    // Non-traversable function (annotated with @constructor)
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Non-traversable function (annotated with @this)
    jsDoc.setThisType("MyType");
    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Non-traversable function (annotated with @override)
    jsDoc.setOverride(true);
    functionNode.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Non-traversable function (inside an assignment)
    Node assignNode = new Node(Token.ASSIGN, "", 0, 0, 0);
    assignNode.setRightChild(functionNode);
    Node leftChild = new Node(Token.NAME, "variable", 0, 0, 0);
    assignNode.setLeftChild(leftChild);
    functionNode.setParent(assignNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Traversable function (inside a prototype method)
    Node prototypeNode = new Node(Token.GETPROP, "", 0, 0, 0);
    prototypeNode.setString("prototype");
    assignNode.setLeftChild(prototypeNode);
    prototypeNode.setRightChild(functionNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode, root));

    // Traversable function (inside a function with no logical place for a @this annotation)
    Node functionNode3 = new Node(Token.FUNCTION, "", 0, 0, 0);
    leftChild = new Node(Token.ASSIGN, "", 0, 0, 0);
    assignNode = new Node(Token.ASSIGN, "", 0, 0, 0);
    assignNode.setLeftChild(leftChild);
    assignNode.setRightChild(functionNode3);
    assignNode.setParent(root);
    leftChild.setLeftChild(new Node(Token.NAME, "variable", 0, 0, 0));
    leftChild.setRightChild(assignNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.UNBOUND, functionNode3, root));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);

    Node root = new Node(Token.SCRIPT, "", 0);
    Node scriptParent = new Node(Token.SCRIPT, "", 0, 0, 0);
    root.setParent(scriptParent);

    Node assignNode = new Node(Token.ASSIGN, "", 0, 0, 0);
    Node thisNode = new Node(Token.THIS, "", 0, 0, 0);
    Node leftNode = new Node(Token.NAME, "left", 0, 0, 0);
    Node rightNode = new Node(Token.NAME, "right", 0, 0, 0);
    assignNode.setLeftChild(leftNode);
    assignNode.setRightChild(thisNode);
    leftNode.setParent(assignNode);
    thisNode.setParent(assignNode);
    scriptParent.setFirstChild(assignNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    assertTrue(checker.shouldReportThis(thisNode, assignNode));
    assertTrue(checker.shouldReportThis(thisNode, root));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = new Node(Token.FUNCTION, "", 0, 0, 0);
    JSDocInfo jsDoc = new JSDocInfo();
    functionNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checker = new CheckGlobalThis(null, null);

    assertEquals(jsDoc, checker.getFunctionJsDocInfo(functionNode));
  }
}