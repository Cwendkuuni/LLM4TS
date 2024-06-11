package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

import org.junit.Assert;

public class PrepareAstTest {

  @Test
  public void testPrepareAst_NormalizeNodeTypes() {
    Node root = new Node(Token.BLOCK);
    Node n1 = new Node(Token.IF, null);
    Node n2 = new Node(Token.BLOCK, null);
    Node n3 = new Node(Token.WHILE, null);

    n1.addChild(n2);
    root.addChild(n1);
    root.addChild(n3);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    checkState(n1.isBlock(), "Expected IF to be a block");
    checkState(n1.getFirstChild().isControlStructure(), "Expected control structure as first child of IF");
    checkState(n2.isBlock(), "Expected control structure code block to be a block");
    checkState(n3.isControlStructure(), "Expected WHILE to be a control structure");

    // Test empty block
    Node emptyBlock = new Node(Token.BLOCK, null);
    root.addChild(emptyBlock);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(null, root, prepareAnnotations);
    checkState(emptyBlock.isEmpty(), "Expected empty block to be empty");
  }

  @Test
  public void testPrepareAst_NormalizeBlocks() {
    Node root = new Node(Token.BLOCK);
    Node ifNode = new Node(Token.IF, null);
    Node ifBlock = new Node(Token.BLOCK, null);
    Node whileNode = new Node(Token.WHILE, null);

    ifBlock.addChild(new Node(Token.ASSIGN, null, new Node(Token.NUMBER, 0), new Node(Token.NUMBER, 2)));

    ifNode.addChild(ifBlock);
    whileNode.addChild(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 1)));

    root.addChild(ifNode);
    root.addChild(whileNode);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node ifControlStructure = ifNode.getFirstChild();
    Node ifBlockWithAnnotations = ifControlStructure.getFirstChild();
    Node whileControlStructure = whileNode.getFirstChild();

    assertTrue(ifBlockWithAnnotations.isBlock());
    assertTrue(whileControlStructure.isControlStructure());

    checkState(ifBlockWithAnnotations.getFirstChild() == ifBlock, "Expected ifBlock to be first child of ifBlockWithAnnotations");
    checkState(whileControlStructure.getFirstChild() == null, "Expected whileControlStructure to have no children");
  }

  @Test
  public void testPrepareAst_PrepareAnnotations() {
    Node root = new Node(Token.BLOCK);
    Node objLit = new Node(Token.OBJECT_LITERAL, null);
    Node key1 = new Node(Token.NAME, "a");
    Node value1 = new Node(Token.STRING_LITERAL, "b");
    Node key2 = new Node(Token.NAME, "c");
    Node value2 = new Node(Token.FUNCTION, null);
    Node funcBody = new Node(Token.BLOCK, null);

    value2.addChild(funcBody);
    funcBody.addChild(new Node(Token.RETURN, null, new Node(Token.STRING_LITERAL, "d")));

    objLit.addChild(key1);
    objLit.addChild(value1);
    objLit.addChild(key2);
    objLit.addChild(value2);

    root.addChild(objLit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node functionValue = value2.getFirstChild();
    Node functionBody = functionValue.getFirstChild();

    Assert.assertNotNull(objLit.getJSDocInfo());
    Assert.assertEquals("a", objLit.getJSDocInfo().getName());
    Assert.assertEquals("b", value1.getJSDocInfo().getValue());
    Assert.assertNull(value1.getJSDocInfo());
    Assert.assertEquals("c", key2.getJSDocInfo().getName());
    Assert.assertEquals("d", functionBody.getLastChild().getString());
    Assert.assertEquals("d", functionValue.getJSDocInfo().getValue());
  }

  @Test
  public void testPrepareAst_AnnotateCalls() {
    Node root = new Node(Token.BLOCK);
    Node callNode = new Node(Token.CALL, null);
    Node funcNode = new Node(Token.FUNCTION, null);

    funcNode.addChild(new Node(Token.RETURN, null, new Node(Token.NAME, "eval")));
    callNode.addChild(funcNode);

    root.addChild(callNode);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node functionNode = funcNode.getFirstChild();
    checkState(functionNode.isFunction(), "Expected function node");
    checkState(functionNode.isAnonymousFunction(), "Expected anonymous function");
    checkState(callNode.isCall(), "Expected call node");
    checkState(callNode.getFirstChild() == funcNode, "Expected function node as first child of call node");
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAst_AnnotateDispatchers() {
    Node root = new Node(Token.BLOCK);
    Node assignNode = new Node(Token.ASSIGN, null);
    Node funcNode = new Node(Token.FUNCTION, null);
    Node dispatchNode = new Node(Token.NAME, "dispatch");

    funcNode.addChild(new Node(Token.BLOCK, null));

    assignNode.addChild(funcNode);
    assignNode.addChild(dispatchNode);

    root.addChild(assignNode);

    Node dispatchInfo = new Node(Token.JSDOC_INFO, null);
    dispatchInfo.putBooleanProp(Node.IS_DISPATCHER, true);

    dispatchNode.setJSDocInfo(dispatchInfo);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    checkState(funcNode.getBooleanProp(Node.IS_DISPATCHER), "Expected function node to be dispatcher");
  }

  @Test
  public void testPrepareAst_NormalizeObjectLiteralKeyAnnotations() {
    Node root = new Node(Token.BLOCK);
    Node objLit = new Node(Token.OBJECT_LITERAL, null);
    Node key1 = new Node(Token.NAME, "a");
    Node value1 = new Node(Token.FUNCTION, null);
    Node funcBody = new Node(Token.BLOCK, null);

    value1.addChild(funcBody);
    funcBody.addChild(new Node(Token.RETURN, null, new Node(Token.STRING_LITERAL, "b")));

    objLit.addChild(key1);
    objLit.addChild(value1);

    root.addChild(objLit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node functionValue = value1.getFirstChild();
    Node functionBody = functionValue.getFirstChild();

    Node objLitValue = objLit.getFirstChild();
    Node keyValue = objLitValue.getFirstChild();

    value1.setJSDocInfo(keyValue.getJSDocInfo());

    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(null, root, prepareAnnotations);

    checkState(value1.getJSDocInfo() != null, "Expected value1 to have JSDoc info after traversal");
    assertEquals("b", value1.getJSDocInfo().getValue());
  }

  @Test
  public void testPrepareAst_CheckOnly() {
    Node root = new Node(Token.BLOCK);

    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);

    // The checkOnly flag should prevent any changes to the tree
    assertFalse(root.isEmpty());
  }
}