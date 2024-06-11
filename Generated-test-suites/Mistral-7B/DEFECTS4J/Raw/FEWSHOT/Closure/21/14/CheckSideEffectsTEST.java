package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler() {
    };
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    // Empty block
    Node root = Node.newBlock();
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Void statement
    root = Node.newBlock().addChild(Node.newExpressionStatement(Node.newExpression(Node.VOID_KEYWORD)));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Simple statement
    root = Node.newBlock().addChild(Node.newExpressionStatement(Node.newExpression(new NumberLiteral(1))));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Assignment statement with side effect
    root = Node.newBlock().addChild(Node.newAssignment(Node.newName("x"), Node.newNumberLiteral(1)));
    NodeTraversal.traverse(compiler, root, checker);
    // Since 'x' is assigned a value, it is considered as having a side effect
    assertFalse(checker.problemNodes.isEmpty());

    // Assignment statement with no side effect
    root = Node.newBlock().addChild(Node.newAssignment(Node.newName("x"), Node.newIdentifier("y")));
    NodeTraversal.traverse(compiler, root, checker);
    // Since 'x' is assigned a reference to 'y', it is not considered as having a side effect
    assertTrue(checker.problemNodes.isEmpty());

    // condition statement with no side effect
    root = Node.newBlock()
        .addChild(Node.newIfStatement(
            Node.newExpression(new LogicalOperator(Token.LOGICAL_OR, Node.newExpression(
                new ComparisonOperator(Token.LESS_EQUALS, Node.newName("x"), Node.newNumberLiteral(1))),
                Node.newExpression(new ComparisonOperator(Token.LESS_EQUALS, Node.newName("y"), Node.newNumberLiteral(2))))),
            Node.newBlock().addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("z").getInitStatement()))),
            Node.newBlock()));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Loop statement with no side effect
    root = Node.newBlock()
        .addChild(Node.newForStatement(
            Node.newName("i"),
            Node.newExpression(Node.newNumberLiteral(0)),
            Node.newExpression(Node.newNumberLiteral(1)),
            Node.newBlock().addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("i").getInitStatement()))),
            Node.newBlock().addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("i").getIncrementStatement()))),
            Node.newBlock()))
        .addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("z").getInitStatement())));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Expression statement with no side effect
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(Node.newExpression(new ArithmeticOperator(Token.MINUS, Node.newName("x"), Node.newNumberLiteral(1)))));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Expression statement with side effect
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(Node.newExpression(new Assignment(Node.newName("x"), Node.newIdentifier("y")))));
    NodeTraversal.traverse(compiler, root, checker);
    // Since 'x' is assigned a value, it is considered as having a side effect
    assertFalse(checker.problemNodes.isEmpty());

    // Expression statement with side effect and protected by PROTECTOR_FN
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(
            Node.newCall(
                Node.newName(PROTECTOR_FN),
                Lists.newArrayList(
                    Node.newExpression(new Assignment(Node.newName("x"), Node.newIdentifier("y")))))));
    NodeTraversal.traverse(compiler, root, checker);
    // Since the expression is protected by PROTECTOR_FN, it is not considered as having a side effect
    assertTrue(checker.problemNodes.isEmpty());

    // Block statement with no side effect
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("z").getInitStatement())))
        .addChild(Node.newExpressionStatement(Node.newExpression(new ArithmeticOperator(Token.MINUS, Node.newName("x"), Node.newNumberLiteral(1)))));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());

    // Block statement with side effect
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(Node.newExpression(Node.newAssignment(Node.newName("x"), Node.newIdentifier("y")))))
        .addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("z").getInitStatement())));
    NodeTraversal.traverse(compiler, root, checker);
    // Since 'x' is assigned a value, it is considered as having a side effect
    assertFalse(checker.problemNodes.isEmpty());

    // Block statement with side effect and protected by PROTECTOR_FN
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(
            Node.newCall(
                Node.newName(PROTECTOR_FN),
                Lists.newArrayList(
                    Node.newExpressionStatement(Node.newExpression(Node.newAssignment(Node.newName("x"), Node.newIdentifier("y"))))),
                    Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("z").getInitStatement())))));
    NodeTraversal.traverse(compiler, root, checker);
    // Since the expressions are protected by PROTECTOR_FN, they are not considered as having side effects
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testRemoveProtection() {
    AbstractCompiler compiler = new AbstractCompiler() {
    };
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

    // Test with no protection
    Node root = Node.newBlock()
        .addChild(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("x").getInitStatement())));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
    NodeTraversal.traverse(compiler, root, stripper);
    assertTrue(checker.problemNodes.isEmpty());

    // Test with protection
    root = Node.newBlock()
        .addChild(Node.newExpressionStatement(
            Node.newCall(
                Node.newName(PROTECTOR_FN),
                Lists.newArrayList(Node.newExpressionStatement(Node.newExpression(Node.newIdentifier("x").getInitStatement()))))));
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
    NodeTraversal.traverse(compiler, root, stripper);
    assertTrue(checker.problemNodes.isEmpty());
  }
}