package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Test empty statement
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = new Node(Token.BLOCK);
    Node emptyStatement = new Node(Token.VOID);
    root.addChildToBack(emptyStatement);
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test comment
    root.addChildToBack(new Node(Token.COMMENT, "// This is a comment"));
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test simple assignment
    root.removeChild(emptyStatement);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    root.addChildToBack(assignment);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test function call with result used
    root.removeChild(assignment);
    Node functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test function call without result used
    Node unusedVariable = new Node(Token.VAR, new Node(Token.NAME, "y"), null);
    root.addChildToBack(unusedVariable);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test expression statement
    root.removeChild(unusedVariable);
    Node expressionStatement = new Node(Token.EXPR_STATEMENT, functionCall);
    root.addChildToBack(expressionStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test simple arithmetic expression
    root.removeChild(expressionStatement);
    Node arithmeticExpression = new Node(Token.BINARY_MINUS, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1));
    root.addChildToBack(arithmeticExpression);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test if statement
    root.removeChild(arithmeticExpression);
    Node ifStatement = new Node(Token.IF, new Node(Token.COMPARISON, Token.EQUALS, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42)), new Node(Token.BLOCK));
    root.addChildToBack(ifStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test while statement
    root.removeChild(ifStatement);
    Node whileStatement = new Node(Token.WHILE, new Node(Token.COMPARISON, Token.LESS, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 10)), new Node(Token.BLOCK));
    root.addChildToBack(whileStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement
    root.removeChild(whileStatement);
    Node forStatement = new Node(Token.FOR, new Node(Token.VAR_DECLARATION, new Node(Token.NAME, "i"), new Node(Token.INITIALIZER, new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 0)))), new Node(Token.EXPR, new Node(Token.COMPARISON, Token.LESS, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 10))), new Node(Token.BLOCK));
    root.addChildToBack(forStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test switch statement
    root.removeChild(forStatement);
    Node switchStatement = new Node(Token.SWITCH, new Node(Token.EXPR, new Node(Token.NAME, "x")), new Node(Token.BLOCK));
    root.addChildToBack(switchStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test label statement
    root.removeChild(switchStatement);
    Node labelStatement = new Node(Token.LABEL, new Node(Token.NAME, "label"));
    root.addChildToBack(labelStatement);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test break statement
    root.removeChild(labelStatement);
    Node breakStatement = new Node(Token.BREAK, new Node(Token.NAME, "label"));
    root.addChildToBack(breakStatement);
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test continue statement
    root.removeChild(breakStatement);
    Node continueStatement = new Node(Token.CONTINUE, null);
    root.addChildToBack(continueStatement);
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test return statement
    root.removeChild(continueStatement);
    Node returnStatement = new Node(Token.RETURN, new Node(Token.NAME, "x"));
    root.addChildToBack(returnStatement);
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test empty block
    root.removeChild(returnStatement);
    Node emptyBlock = new Node(Token.BLOCK);
    root.addChildToBack(emptyBlock);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test expression block
    root.removeChild(emptyBlock);
    Node expressionBlock = new Node(Token.BLOCK, new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"))));
    root.addChildToBack(expressionBlock);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test statement block
    root.removeChild(expressionBlock);
    Node statementBlock = new Node(Token.BLOCK, new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"))), new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.INT_LITERAL, 42))));
    root.addChildToBack(statementBlock);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test statement list
    root.removeChild(statementBlock);
    Node statementList = new Node(Token.BLOCK, new Node[]{
        new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"))),
        new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.INT_LITERAL, 42)))
    });
    root.addChildToBack(statementList);
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    // Test protectSideEffects method with no problem nodes
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    checkSideEffects.protectSideEffects();
    assertNull(checkSideEffects.problemNodes);

    // Test protectSideEffects method with problem nodes
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = new Node(Token.BLOCK);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    Node functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(assignment);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    assertNull(checkSideEffects.problemNodes);

    // Test protectSideEffects method with problem nodes and protectSideEffectFreeCode set to true
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    root = new Node(Token.BLOCK);
    assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(assignment);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    assertNotNull(checkSideEffects.problemNodes);
  }

  @Test
  public void testStripProtection() {
    // Test stripProtection method with no problem nodes
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = new Node(Token.BLOCK);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    Node functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(assignment);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    new StripProtection(null).process(null, root);
    assertNull(checkSideEffects.problemNodes);

    // Test stripProtection method with problem nodes and protectSideEffectFreeCode set to true
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    root = new Node(Token.BLOCK);
    assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(assignment);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    new StripProtection(null).process(null, root);
    assertNull(checkSideEffects.problemNodes);

    // Test stripProtection method with problem nodes and protectSideEffectFreeCode set to false
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = new Node(Token.BLOCK);
    assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 42));
    functionCall = new Node(Token.CALL, new Node(Token.NAME, "foo"), Lists.newArrayList());
    root.addChildToBack(assignment);
    root.addChildToBack(functionCall);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    new StripProtection(null).process(null, root);
    assertNotNull(checkSideEffects.problemNodes);
  }
}