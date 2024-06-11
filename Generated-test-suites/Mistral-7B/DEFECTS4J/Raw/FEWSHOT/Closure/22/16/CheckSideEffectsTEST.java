package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffectsVoidNode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node voidNode = Node.newVoid();
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(voidNode);

    checkSideEffects.visit(null, voidNode, parentNode);
  }

  @Test
  public void testCheckSideEffectsNoParentNode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newNumber(1);

    checkSideEffects.visit(null, nodeToTest, null);
  }

  @Test
  public void testCheckSideEffectsResultUsed() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newName("x");
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(assignNode);
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsSimpleOperatorType() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newBinaryOp(Token.PLUS, Node.newNumber(1), Node.newNumber(2));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsMayHaveSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newCall(Node.newName("console.log"));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsNoSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newBinaryOp(Token.MULTIPLY, Node.newNumber(1), Node.newNumber(2));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsString() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newString("foo bar");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsMissingPlus() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newName("s");
    Node stringLiteral1 = Node.newString("this string is ");
    Node stringLiteral2 = Node.newString("continued on the next line but you forgot the +");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(stringLiteral1);
    parentNode.addChildToBack(nodeToTest);
    parentNode.addChildToBack(stringLiteral2);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsCallToFunctionWithNoArguments() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node nodeToTest = Node.newCall(Node.newName("foo"));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsCallToFunctionWithArguments() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node functionCallNode = Node.newCall(Node.newName("foo"), Node.newNumber(1), Node.newNumber(2));
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(functionCallNode);

    checkSideEffects.visit(null, functionCallNode, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideIfCondition() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node ifStatementNode = Node.newIf(Node.newBinop(Token.LESSEQ, Node.newName("x"), Node.newNumber(1)), Node.newBlock(), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside if condition");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(ifStatementNode);
    ifStatementNode.getIfCondition().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideForLoop() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node forLoopNode = Node.newFor(Node.newName("i"), Node.newNumber(0), Node.newNumber(1), new Node[0], Node.newBlock(), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside for loop");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(forLoopNode);
    forLoopNode.getBody().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideWhileLoop() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node whileLoopNode = Node.newWhile(Node.newBinop(Token.LESSEQ, Node.newName("i"), Node.newNumber(10)), Node.newBlock(), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside while loop");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(whileLoopNode);
    whileLoopNode.getBody().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideSwitch() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node switchStatementNode = Node.newSwitch(Node.newName("x"), Node.newBlock(), new List[0]);
    Node caseNode = Node.newSwitchCase(Node.newInt(1), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside switch statement");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(switchStatementNode);
    switchStatementNode.getCases().add(caseNode);
    caseNode.getBody().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideWithStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node withStatementNode = Node.newWith(Node.newName("x"), Node.newObject(), Node.newBlock(), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside with statement");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(withStatementNode);
    withStatementNode.getBody().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testCheckSideEffectsStatementInsideTryCatchStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node tryCatchStatementNode = Node.newTryCatchBlock(Node.newBlock(), Node.newBlock(), Node.newBlock());
    Node statementNodeToTest = Node.newPrint("Statement inside try-catch statement");
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(tryCatchStatementNode);
    tryCatchStatementNode.getTryBlock().addChildToBack(statementNodeToTest);

    checkSideEffects.visit(null, statementNodeToTest, parentNode);
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Node nodeToTest = Node.newNumber(1);
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(nodeToTest);

    checkSideEffects.visit(null, nodeToTest, parentNode);
    checkSideEffects.protectSideEffects();

    Node protectedNode = null;
    for (Node n : parentNode.getChildren()) {
      if (n.isName() && n.getString().equals(CheckSideEffects.PROTECTOR_FN)) {
        protectedNode = n;
        break;
      }
    }

    assertSame(protectedNode, nodeToTest.getParent());
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Node protectedNode = Node.newCall(Node.newName(CheckSideEffects.PROTECTOR_FN), Node.newArray());
    Node parentNode = Node.newBlock();
    parentNode.addChildToBack(protectedNode);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
    stripProtection.process(null, root);

    assertFalse(parentNode.hasChild(protectedNode));
  }
}