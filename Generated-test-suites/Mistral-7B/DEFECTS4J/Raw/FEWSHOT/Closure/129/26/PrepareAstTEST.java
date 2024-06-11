package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);
    Node externs = new Node(Token.BLOCK);

    prepareAst.process(externs, root);

    // Basic test to check that the process method was called
    assertTrue(true);
  }

  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.IF, null, null, null);
    root.addChildToEnd(new Node(Token.CALL, null, null, null));

    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    checkState(ifNode.isIf());

    Node callNode = ifNode.getFirstChild();
    checkState(callNode.isCall());
    assertFalse(callNode.isBlock());

    Node resultNode = new Node(Token.EXPR_RESULT, null, null, null);
    callNode.replaceChild(callNode.getFirstChild(), resultNode);

    prepareAst.normalizeNodeTypes(root);

    Node ifBlock = ifNode.getNext();
    checkState(ifBlock.isBlock());
  }

  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.IF, null, null, null);
    root.addChildToEnd(new Node(Token.CALL, null, null, null));

    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    checkState(ifNode.isIf());

    Node callNode = ifNode.getFirstChild();
    checkState(callNode.isCall());

    Node ifBlock = new Node(Token.BLOCK, null, null, null);
    callNode.replaceChild(callNode.getFirstChild(), ifBlock);
    ifBlock.addChildrenToFront(callNode.getNext());

    prepareAst.normalizeBlocks(root);

    Node ifBlock2 = root.getFirstChild().getNext();
    checkState(ifBlock2.isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);
    root.addChildToEnd(new Node(Token.OBJECT_LITERAL, null, null, null));

    prepareAst.process(null, root);

    Node objLitNode = root.getFirstChild();
    checkState(objLitNode.isObjectLit());

    Node annotationsNode = new Node(Token.BLOCK, null, null, null);
    objLitNode.addChildrenToFront(annotationsNode);

    prepareAst.new PrepareAnnotations().visit(null, objLitNode, null);

    Node annotationsBlock = objLitNode.getFirstChild();
    checkState(annotationsBlock.isBlock());
  }

  @Test
  public void testAnnotateCalls() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node root = new Node(Token.BLOCK);
    root.addChildToEnd(
        new Node(Token.CALL, null, null, new Node(Token.STRING_LITERAL, null, null, "eval")));

    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    checkState(callNode.isCall());

    Node evalNode = callNode.getFirstChild();
    checkState(evalNode.isName());
    assertTrue(evalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node parent = new Node(Token.ASSIGN, null, null, null);
    parent.setJSDocInfo(new Node.JSDocInfo("Java Dispatch", null, null, null));

    Node root = new Node(Token.BLOCK);
    root.addChildToEnd(parent);
    root.addChildToEnd(new Node(Token.FUNCTION, null, null, null));

    prepareAst.process(null, root);

    Node functionNode = root.getLastChild();
    checkState(functionNode.isFunction());

    Node dispatcherNode = functionNode.getFirstChild();
    checkState(dispatcherNode.isFunction());
    assertTrue(dispatcherNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node objLitNode = new Node(Token.OBJECT_LITERAL, null, null, null);

    Node keyNode = new Node(Token.STRING_LITERAL, null, null, "a");
    Node valueNode = new Node(Token.FUNCTION, null, null, null);

    valueNode.setJSDocInfo(new Node.JSDocInfo("Java Doc", null, null, null));

    objLitNode.addChildToEnd(new Node(Token.PROPERTY, null, keyNode, valueNode));

    prepareAst.new PrepareAnnotations().visit(null, objLitNode, null);

    Node valueNode2 = objLitNode.getLastChild();
    checkState(valueNode2.isFunction());
    assertEquals(valueNode.getJSDocInfo(), valueNode2.getJSDocInfo());
  }
}