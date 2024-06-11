package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * Test cases for PrepareAst class.
 */
public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    PrepareAst prepareAst = new PrepareAst(null, false);

    // Test empty nodes
    Node emptyNode = IR.literal(0);
    Node emptyBlock = IR.block().srcref(emptyNode);
    prepareAst.process(null, null);
    assertFalse(emptyNode.isBlock());
    assertFalse(emptyBlock.isBlock());

    // Test normal nodes
    Node normalNode = IR.identifier("x");
    prepareAst.process(null, null);
    assertFalse(normalNode.isBlock());

    // Test blocks
    Node blockNode = IR.block().srcref(emptyNode);
    prepareAst.process(null, null);
    assertTrue(blockNode.isBlock());

    // Test control structures
    Node ifNode = IR.ifStatement(IR.literal(true), null, null);
    prepareAst.process(null, null);
    assertTrue(ifNode.isBlock());

    Node whileNode = IR.whileStatement(IR.literal(true), null, null);
    prepareAst.process(null, null);
    assertTrue(whileNode.isBlock());

    Node switchNode = IR.switchStatement(
        null, null, IR.switchCase(IR.literal(1), null), null);
    prepareAst.process(null, null);
    assertTrue(switchNode.isBlock());

    // Test object literals
    Node objLiteral = IR.objectLiterals().get(0);
    prepareAst.process(null, null);
    assertFalse(objLiteral.isBlock());

    // Test annotations
    Node callNode = IR.callExpression(IR.identifier("f"), null);
    Node funcNode = IR.functionExpression(
        null, null, null, null, null, null, null, null);
    funcNode.putBooleanProp(Node.IS_DISPATCHER, true);
    Node objLitNode = IR.objectLiterals().get(0);
    Node keyNode1 = IR.identifier("a");
    Node keyNode2 = IR.identifier("c");
    Node valueNode1 = IR.identifier("b");
    Node valueNode2 = IR.functionExpression(
        null, null, null, null, null, null, null, null);
    Node propNode1 = IR.property(keyNode1, valueNode1);
    Node propNode2 = IR.property(keyNode2, valueNode2);
    objLitNode.addChildToEnd(propNode1);
    objLitNode.addChildToEnd(propNode2);
    Node parentNode = IR.assign(IR.identifier("x"), objLitNode);

    Node oldCallNode = callNode;
    Node oldFuncNode = funcNode;
    Node oldObjLitNode = objLitNode;
    Node oldParentNode = parentNode;

    NodeTraversal.traverse(
        prepareAst.compiler, oldParentNode, new PrepareAnnotations());

    // Check annotations on call node
    Node newCallNode = callNode;
    assertTrue(newCallNode.isFreeCall());
    assertFalse(newCallNode.isDirectEval());

    // Check annotations on function node
    Node newFuncNode = funcNode;
    assertTrue(newFuncNode.isDispatcher());

    // Check annotations on object literal node
    Node newObjLitNode = objLitNode;
    assertNull(newObjLitNode.getJSDocInfo());
    Node newPropNode1 = propNode1;
    assertEquals(newObjLitNode.getJSDocInfo(), newPropNode1.getJSDocInfo());
    Node newPropNode2 = propNode2;
    assertEquals(newObjLitNode.getJSDocInfo(), newPropNode2.getJSDocInfo());

    // Check blocks
    Node newBlockNode = blockNode;
    assertTrue(newBlockNode.isBlock());
    Node oldBlockChild = oldBlockNode.getFirstChild();
    Node newBlockChild = newBlockNode.getFirstChild();
    assertEquals(oldBlockChild, newBlockChild);

    // Check parent node
    Node newParentNode = parentNode;
    assertTrue(newParentNode.isBlock());
    Node oldParentChild = oldParentNode.getLastChild();
    Node newParentChild = newParentNode.getLastChild();
    assertEquals(oldParentChild, newParentChild);
  }
}

class PrepareAnnotations implements NodeTraversal.Callback {
  // Existing code for PrepareAnnotations class
}