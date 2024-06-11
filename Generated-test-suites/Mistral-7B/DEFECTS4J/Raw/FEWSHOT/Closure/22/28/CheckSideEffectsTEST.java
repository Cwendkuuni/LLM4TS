package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.List;

/**
 * Test class for {@link CheckSideEffects}.
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Test initialization
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

    // Test visit method with simple statements
    Node simpleStatementNode = Node.newLiteral(Token.STRING, "foo");
    Node parentNode = Node.newBlock(1);
    parentNode.addChildToBack(simpleStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), simpleStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a void statement
    Node voidStatementNode = Node.newExpressionStatement(Node.newExpression(Node.VOID, null));
    parentNode.addChildToBack(voidStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), voidStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a comment statement
    Node commentStatementNode = Node.newExpressionStatement(Node.newExpression(Node.COMMENT, "// This is a comment"));
    parentNode.addChildToBack(commentStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), commentStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a simple expression statement
    Node expressionStatementNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "x"));
    parentNode.addChildToBack(expressionStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), expressionStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a void expression statement
    Node voidExpressionStatementNode = Node.newExpressionStatement(Node.newExpression(Node.VOID, Node.newName("console.log")));
    parentNode.addChildToBack(voidExpressionStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), voidExpressionStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a conditional statement
    Node ifStatementNode = Node.newBlock(1);
    Node conditionNode = Node.newExpression(Node.LOGICAL_OR, Node.newExpression(Node.LOGICAL_AND, Node.newExpression(Node.NAME, "x"), Node.newLiteral(Token.INT_LITERAL, 1)), Node.newLiteral(Token.FALSE));
    Node ifBodyNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "y"));
    Node elseBodyNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "z"));
    Node elseIFStatementNode = Node.newIfStatement(conditionNode, ifBodyNode, elseBodyNode);
    ifStatementNode.addChildToBack(elseIFStatementNode);

    parentNode.addChildToBack(ifStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), ifStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a loop statement
    Node forStatementNode = Node.newBlock(1);
    Node initNode = Node.newExpressionStatement(Node.newExpression(Node.ASSIGN, Node.newName("i"), Node.newLiteral(Token.INT_LITERAL, 0)));
    Node conditionNode2 = Node.newExpression(Node.LOGICAL_AND, Node.newExpression(Node.NAME, "i"), Node.newLiteral(Token.INT_LITERAL, 10));
    Node incrementNode = Node.newExpressionStatement(Node.newExpression(Node.ASSIGN, Node.newName("i"), Node.newExpression(Node.ADD, Node.newName("i"), Node.newLiteral(Token.INT_LITERAL, 1))));
    Node forBodyNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "console.log(i)"));
    Node forStatement = Node.newForStatement(initNode, conditionNode2, incrementNode, forBodyNode);
    forStatementNode.addChildToBack(forStatement);

    parentNode.addChildToBack(forStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), forStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a return statement
    Node returnStatementNode = Node.newReturnStatement(Node.newName("result"));
    parentNode.addChildToBack(returnStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), returnStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a function call statement
    Node functionCallStatementNode = Node.newExpressionStatement(Node.newExpression(Node.CALL, Node.newName("console.log"), Node.newArray(Node.newStringLiteral("message"))));
    parentNode.addChildToBack(functionCallStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), functionCallStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a statement expression
    Node statementExpressionNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "console.log(x)"));
    parentNode.addChildToBack(statementExpressionNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), statementExpressionNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test visit method with a statement with side-effects
    Node sideEffectStatementNode = Node.newExpressionStatement(Node.newExpression(Node.ASSIGN, Node.newName("x"), Node.newName("y")));
    parentNode.addChildToBack(sideEffectStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), sideEffectStatementNode, parentNode);
    assertEquals(1, checkSideEffects.level.getNumWarnings());
    assertEquals("Suspicious code. The result of the '=' operator is not being used.", checkSideEffects.level.getWarning(0).getMessage());

    // Test visit method with a statement with hidden side-effects
    Node hiddenSideEffectStatementNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "x.offsetWidth"));
    parentNode.addChildToBack(hiddenSideEffectStatementNode);

    checkSideEffects.visit(NodeTraversal.create(compiler), hiddenSideEffectStatementNode, parentNode);
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    // Test process method
    parentNode = Node.newBlock(1);
    for (Node testNode : new Node[] { simpleStatementNode, voidStatementNode, commentStatementNode, expressionStatementNode, voidExpressionStatementNode,
        ifStatementNode, forStatementNode, returnStatementNode, functionCallStatementNode, statementExpressionNode, sideEffectStatementNode, hiddenSideEffectStatementNode }) {
      parentNode.addChildToBack(testNode);
    }

    checkSideEffects.process(Node.newNull(), parentNode);
    assertEquals(1, checkSideEffects.level.getNumWarnings());
    assertEquals("Suspicious code. The result of the '=' operator is not being used.", checkSideEffects.level.getWarning(0).getMessage());
  }

  @Test
  public void testProtectSideEffects() {
    // Test protectSideEffects method
    CheckLevel level = CheckLevel.ERROR;
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

    Node sideEffectFreeNode = Node.newExpressionStatement(Node.newExpression(Node.NAME, "console.log"));

    checkSideEffects.process(Node.newNull(), Node.newBlock(1));
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());

    checkSideEffects.protectSideEffects();
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node protectedNode = checkSideEffects.problemNodes.get(0);
    assertEquals(Node.CALL, protectedNode.getType());
    Node targetNode = protectedNode.getFirstChild();
    assertEquals(Node.NAME, targetNode.getType());
    assertEquals("JSCOMPILER_PRESERVE", targetNode.getString());

    checkSideEffects.level.clearWarnings();
    checkSideEffects.process(Node.newNull(), Node.newBlock(1));
    assertFalse("Expected no warning message.", checkSideEffects.level.hasWarnings());
  }

  @Test
  public void testStripProtection() {
    // Test stripProtection method
    AbstractCompiler compiler = new Compiler();
    StripProtection stripProtection = new StripProtection(compiler);

    Node protectedNode = Node.newCall(Node.newName("JSCOMPILER_PRESERVE"), Node.newArray());

    stripProtection.process(Node.newNull(), Node.newBlock(1));
    assertFalse("Expected no warning message.", stripProtection.compiler.getDiagnostics().isEmpty());
    assertEquals(1, stripProtection.compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = stripProtection.compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.error, diagnosticType);
    assertEquals("JSC_UNDEFINED_NAME", diagnosticType.getCode());

    Node protectedExpressionNode = Node.newExpression(Node.CALL, Node.newName("JSCOMPILER_PRESERVE"), Node.newArray());
    Node protectedStatementNode = Node.newExpressionStatement(protectedExpressionNode);
    Node parentNode = Node.newBlock(1);
    parentNode.addChildToBack(protectedStatementNode);

    stripProtection.process(Node.newNull(), parentNode);
    assertFalse("Expected no warning message.", stripProtection.compiler.getDiagnostics().isEmpty());
    assertEquals(1, stripProtection.compiler.getDiagnostics().size());
    diagnosticType = stripProtection.compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.error, diagnosticType);
    assertEquals("JSC_UNDEFINED_NAME", diagnosticType.getCode());

    stripProtection.stripProtection();
    assertNull("Expected no protected nodes.", parentNode.getFirstChild());
  }
}