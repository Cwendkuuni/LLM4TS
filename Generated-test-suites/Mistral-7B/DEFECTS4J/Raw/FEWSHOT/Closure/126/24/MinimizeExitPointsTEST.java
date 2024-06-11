package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Token;

/**
 * Tests for MinimizeExitPoints CompilerPass.
 */
public class MinimizeExitPointsTest {
  private AbstractCompiler compiler;

  @Test
  public void testMinimizeExitPoints() {
    compiler = new TestCompilerPassSettingUp();
    Node root = createTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test simple return statement.
    assertEquals(Token.RETURN, root.getLastChild().getType());
    assertEquals(Token.NULL, root.getLastChild().getLastChild().getType());

    // Test simple continue statement.
    Node continueStatement = Node.newContinueStatement();
    root.addChildToBack(continueStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test simple break statement.
    Node breakStatement = Node.newBreakStatement();
    root.addChildToBack(breakStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test label statement.
    Node labelNode = Node.newLabelStatement("testLabel");
    root.addChildToBack(labelNode);
    Node labelBlock = Node.newBlock();
    root.addChildToBack(labelBlock);
    Node breakLabelStatement = Node.newBreakStatement("testLabel");
    labelBlock.addChildToBack(breakLabelStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test label statement with function.
    Node labelFunction = Node.newFunctionStatement("testFunction", new IR());
    labelBlock.addChildToBack(labelFunction);
    labelFunction.getLastChild().addChildToBack(breakLabelStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test if statement with return statement.
    Node ifStatement = Node.newIfStatement();
    Node ifBlock = Node.newBlock();
    Node elseBlock = Node.newBlock();
    Node returnStatement = Node.newReturnStatement();
    ifStatement.setIfCond(Node.newBooleanLiteral(true));
    ifStatement.addChildToBack(ifBlock);
    ifStatement.addChildToBack(elseBlock);
    ifBlock.addChildToBack(new IR());
    ifBlock.addChildToBack(returnStatement);
    root.addChildToBack(ifStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test if statement with continue statement.
    Node continueIfStatement = Node.newIfStatement();
    Node continueIfBlock = Node.newBlock();
    Node continueStatement2 = Node.newContinueStatement();
    continueIfStatement.setIfCond(Node.newBooleanLiteral(true));
    continueIfStatement.addChildToBack(continueIfBlock);
    continueIfBlock.addChildToBack(new IR());
    continueIfBlock.addChildToBack(continueStatement2);
    root.addChildToBack(continueIfStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test if statement with break statement.
    Node breakIfStatement = Node.newIfStatement();
    Node breakIfBlock = Node.newBlock();
    Node breakStatement3 = Node.newBreakStatement();
    Node label2 = Node.newLabelStatement("testLabel2");
    breakIfStatement.setIfCond(Node.newBooleanLiteral(true));
    breakIfStatement.addChildToBack(breakIfBlock);
    breakIfBlock.addChildToBack(new IR());
    breakIfBlock.addChildToBack(label2);
    labelBlock.addChildToBack(breakLabelStatement);
    root.addChildToBack(breakIfStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test if statement with label statement and return statement.
    Node returnIfStatement = Node.newIfStatement();
    Node returnIfBlock = Node.newBlock();
    Node returnStatement2 = Node.newReturnStatement();
    returnIfStatement.setIfCond(Node.newBooleanLiteral(true));
    returnIfStatement.addChildToBack(returnIfBlock);
    returnIfBlock.addChildToBack(returnStatement2);
    root.addChildToBack(returnIfStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test if statement with label statement and label statement.
    Node labelFunction2 = Node.newFunctionStatement("testFunction2", new IR());
    Node label3 = Node.newLabelStatement("testLabel3");
    Node returnIfStatement2 = Node.newIfStatement();
    Node returnIfBlock2 = Node.newBlock();
    Node labelBlock2 = Node.newBlock();
    Node breakLabelStatement2 = Node.newBreakStatement("testLabel3");
    returnIfStatement2.setIfCond(Node.newBooleanLiteral(true));
    returnIfStatement2.addChildToBack(returnIfBlock2);
    returnIfBlock2.addChildToBack(labelBlock2);
    returnIfBlock2.addChildToBack(returnStatement2);
    labelBlock2.addChildToBack(label3);
    labelBlock2.addChildToBack(breakLabelStatement2);
    labelFunction2.getLastChild().addChildToBack(returnIfStatement2);
    root.addChildToBack(labelFunction2);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test switch statement.
    Node switchStatement = Node.newSwitchStatement();
    Node switchBlock = Node.newBlock();
    Node caseStatement1 = Node.newCaseStatement(Node.newNumberLiteral(1));
    Node caseBlock1 = Node.newBlock();
    Node returnStatement3 = Node.newReturnStatement();
    switchBlock.addChildToBack(caseStatement1);
    switchBlock.addChildToBack(caseBlock1);
    caseBlock1.addChildToBack(returnStatement3);
    switchStatement.setSwitchExp(Node.newNumberLiteral(1));
    switchStatement.addChildToBack(switchBlock);
    root.addChildToBack(switchStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test switch statement with default statement.
    Node defaultStatement = Node.newDefaultStatement();
    Node defaultBlock = Node.newBlock();
    Node returnStatement4 = Node.newReturnStatement();
    defaultBlock.addChildToBack(returnStatement4);
    switchBlock.addChildToBack(defaultStatement);
    switchBlock.addChildToBack(defaultBlock);
    switchStatement.setSwitchExp(Node.newNumberLiteral(1));
    switchStatement.addChildToBack(switchBlock);
    root.addChildToBack(switchStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test while statement.
    Node whileStatement = Node.newWhileStatement();
    Node whileBlock = Node.newBlock();
    Node returnStatement5 = Node.newReturnStatement();
    whileStatement.setTestExp(Node.newBooleanLiteral(true));
    whileStatement.addChildToBack(whileBlock);
    whileBlock.addChildToBack(returnStatement5);
    root.addChildToBack(whileStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test do-while statement.
    Node doWhileStatement = Node.newDoWhileStatement();
    Node doWhileBlock = Node.newBlock();
    Node returnStatement6 = Node.newReturnStatement();
    doWhileStatement.setTestExp(Node.newBooleanLiteral(true));
    doWhileBlock.addChildToBack(returnStatement6);
    doWhileStatement.addChildToBack(doWhileBlock);
    root.addChildToBack(doWhileStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);

    // Test for statement.
    Node forStatement = Node.newForStatement();
    Node forInitializer = Node.newEmptyStatement();
    Node forCondition = Node.newBooleanLiteral(true);
    Node forIncrement = Node.newEmptyStatement();
    forStatement.setInitExp(forInitializer);
    forStatement.setConditionExp(forCondition);
    forStatement.setIncrementExp(forIncrement);
    Node forBlock = Node.newBlock();
    Node returnStatement7 = Node.newReturnStatement();
    forBlock.addChildToBack(returnStatement7);
    forBlock.addChildToBack(Node.newEmptyStatement());
    forStatement.addChildToBack(forBlock);
    root.addChildToBack(forStatement);
    minimizeExitPoints.process(null, root);
    printNode("Minimized Exit Points: ", root);
  }

  private Node createTestNode() {
    Node root = Node.newBlock();
    return root;
  }

  private void printNode(String message, Node node) {
    System.out.println(message + ":");
    System.out.println(NodeUtil.toString(node));
    System.out.println();
  }

  private static class TestCompilerPassSettingUp extends CompilerOptions {
    TestCompilerPassSettingUp() {
      // Empty for testing.
    }

    @Override
    public CompilerPass getPass(Compiler compiler) {
      return new MinimizeExitPoints(compiler);
    }
  }
}