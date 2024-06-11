package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = IR.block().toNode();
    PrepareAst prepareAst = new PrepareAst(null, false);

    Node ifNode = IR.if_(IR.lit(Token.BOOLEAN_LITERAL, true), IR.block(), root);
    Node whileNode = IR.while_(IR.lit(Token.BOOLEAN_LITERAL, true), IR.block(), root);
    Node doWhileNode = IR.doWhile_(IR.lit(Token.BOOLEAN_LITERAL, true), IR.block(), root);

    root.addChildrenToFront(ifNode, whileNode, doWhileNode);

    prepareAst.process(null, root);

    Node ifBlock = ifNode.getFirstChild();
    Node whileBlock = whileNode.getFirstChild();
    Node doWhileBlock = doWhileNode.getFirstChild();

    assertTrue(ifBlock.isBlock());
    assertTrue(whileBlock.isBlock());
    assertTrue(doWhileBlock.isBlock());
  }

  @Test
  public void testAnnotateCalls() {
    Node root = IR.block().toNode();
    Node funcNode = IR.function_(null, null, null, null, IR.block(), root);
    Node callNode = IR.call(IR.name("eval"), IR.lit(Token.NUMBER_LITERAL, 0), null, null, null, funcNode);

    PrepareAst prepareAst = new PrepareAst(null, false);

    prepareAst.process(null, root);

    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    Node root = IR.block().toNode();
    Node objNode = IR.name("obj");
    Node funcNode = IR.function_(null, null, null, null, IR.block(), root);
    Node assignNode = IR.assign(objNode, funcNode);

    Node dispatcherNode = IR.name("JavaDispatch");
    Node jsDocNode = IR.jsDocInfo("JavaDispatch", null, null, null, null);

    assignNode.putJSDocInfo(jsDocNode);
    dispatcherNode.putBooleanProp(Node.IS_DISPATCHER, true);

    PrepareAst prepareAst = new PrepareAst(null, false);

    prepareAst.process(null, root);

    Node dispatcherBlock = dispatcherNode.getFirstChild();
    Node funcBlock = funcNode.getFirstChild();

    assertTrue(dispatcherBlock.isEmpty());
    assertTrue(funcBlock.isBlock());
    assertFalse(funcBlock.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node root = IR.block().toNode();
    Node objlitNode = IR.objectLit(null, null, null);
    Node keyNode1 = IR.name("a");
    Node valueNode1 = IR.stringLiteral("b");
    Node keyNode2 = IR.name("c");
    Node valueNode2 = IR.function_(null, null, null, null, IR.block(), root);
    Node jsDocNode = IR.jsDocInfo("MyAnnotation", null, null, null, null);

    objlitNode.addChildrenToFront(keyNode1, valueNode1);
    keyNode1.setJSDocInfo(jsDocNode);
    objlitNode.addChildrenToFront(keyNode2, valueNode2);

    PrepareAst prepareAst = new PrepareAst(null, false);

    prepareAst.process(null, root);

    Node valueNode1Annotated = valueNode1.getFirstChild();
    Node valueNode2Annotated = valueNode2.getFirstChild();

    assertEquals(jsDocNode, valueNode1Annotated.getJSDocInfo());
    assertNull(valueNode2Annotated.getJSDocInfo());
  }
}