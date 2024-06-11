package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects_SimpleStatements() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node node1 = IR.i(1);
    Node node2 = IR.i(2);

    Node statement1 = IR.assign(IR.name("x"), node1);
    Node statement2 = IR.assign(IR.name("y"), node2);

    Node block = IR.block(statement1, statement2);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_VoidStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node statement = IR.voidStmt();

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_CommentedOutStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node commentedStatement = IR.comment("// This statement is commented out");

    Node block = IR.block(commentedStatement);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node statement = IR.assign(IR.name("document.body.style"), IR.string("background-color", "red"));

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffectFreeSubExpression() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node statement = IR.assign(IR.name("x"), IR.add(IR.i(1), IR.i(2)));

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithMultipleSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node statement1 = IR.assign(IR.name("document.body.style"), IR.string("background-color", "red"));
    Node statement2 = IR.assign(IR.name("document.body.style"), IR.string("color", "blue"));

    Node block = IR.block(statement1, statement2);

    checkSideEffects.process(null, block);

    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffectFreeSubExpressionAndSideEffect() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node statement1 = IR.assign(IR.name("x"), IR.add(IR.i(1), IR.i(2)));
    Node statement2 = IR.assign(IR.name("document.body.style"), IR.string("background-color", "red"));

    Node block = IR.block(statement1, statement2);

    checkSideEffects.process(null, block);

    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSimpleOperator() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node expression = IR.binaryOp(Token.PLUS, IR.name("x"), IR.name("y"));
    Node statement = IR.assign(IR.name("z"), expression);

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSimpleOperatorAndSideEffect() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node expression = IR.binaryOp(Token.PLUS, IR.name("x"), IR.name("y"));
    Node statement = IR.assign(IR.name("document.body.style"), expression);

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSimpleOperatorAndSideEffectFreeSubExpression() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node expression = IR.binaryOp(Token.PLUS, IR.name("x"), IR.name("y"));
    Node statement = IR.assign(IR.name("z"), IR.add(IR.i(1), expression));

    Node block = IR.block(statement);

    checkSideEffects.process(null, block);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}