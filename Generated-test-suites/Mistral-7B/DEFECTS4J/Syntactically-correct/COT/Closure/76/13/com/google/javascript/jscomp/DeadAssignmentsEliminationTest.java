package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractScopedCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for DeadAssignmentsElimination.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    

    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // When
    Node externs = new Node(Token.EMPTY);
    Node root = new Node(Token.PROGRAM, externs);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    deadAssignmentsElimination.process(externs, root);

    // Then
    DiGraph<Node, Branch> controlFlowGraph = nodeTraversal.getControlFlowGraph();
    assertNotNull(controlFlowGraph);

    // Basic tests for all methods

    // enterScope
    // This method is not tested directly as it is an abstract method.

    // exitScope
    // This method is not tested directly as it is an abstract method.

    // process
    // This method is tested indirectly through the test case for tryRemoveDeadAssignments.

    // tryRemoveDeadAssignments
    Node loopCondition = new Node(Token.WHILE, new Node(Token.IDENT, "i"),
        new Node(Token.LES, new Node(Token.IDENT, "i"), new Node(Token.NUMBER, 10)));

    Node loopBody = new Node(Token.ASSIGN, new Node(Token.IDENT, "i"),
        new Node(Token.INC, new Node(Token.IDENT, "i")));

    Node loopStatement = new Node(Token.START, new Node(Token.WHILE, loopCondition,
        new Node(Token.STATEMENT, loopBody)));

    Node functionBody = new Node(Token.BLOCK, new Node(Token.EMPTY));
    functionBody.addChild(new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 1)));
    functionBody.addChild(new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 2)));
    functionBody.addChild(loopStatement);

    Node functionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENT, "func"),
        null, functionBody);

    root.addChild(functionDeclaration);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignments = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignments,
        controlFlowGraph);

    assertFalse(functionBody.hasChild(loopBody));
    assertFalse(functionBody.hasChild(loopCondition));
    assertFalse(functionBody.hasChild(new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 1))));

    // tryRemoveAssignment
    // This method is tested indirectly through the test cases for
    // tryRemoveAssignmentWithinExpression and tryRemoveDeadAssignments.

    // tryRemoveAssignmentWithinExpression
    Node expression = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.IDENT, "y"));
    Node expressionRoot = new Node(Token.ASSIGN, new Node(Token.IDENT, "z"), expression);

    NodeTraversal nodeTraversalForTryRemoveAssignmentWithinExpression = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveAssignment(nodeTraversalForTryRemoveAssignmentWithinExpression,
        expressionRoot, controlFlowGraph.getEntryNode());

    assertFalse(expression.getParent().hasChild(expression));

    // tryRemoveDeadAssignmentsWithFunctionReturn
    Node functionReturnStatement = new Node(Token.RETURN, new Node(Token.IDENT, "x"));
    functionBody.addChild(functionReturnStatement);
    root.addChild(functionDeclaration);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithFunctionReturn = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithFunctionReturn,
        controlFlowGraph);

    assertTrue(functionBody.hasChild(functionReturnStatement));

    // tryRemoveDeadAssignmentsWithFunctionCall
    Node functionCall = new Node(Token.CALL, new Node(Token.IDENT, "func"));
    functionBody.addChild(functionCall);
    root.addChild(functionDeclaration);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithFunctionCall = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithFunctionCall,
        controlFlowGraph);

    assertFalse(functionBody.hasChild(functionCall));

    // tryRemoveDeadAssignmentsWithVariableDeclaration
    Node variableDeclaration = new Node(Token.VAR, new Node(Token.IDENT, "x"),
        null, new Node(Token.NUMBER, 1));
    functionBody.addChild(variableDeclaration);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableDeclaration = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableDeclaration,
        controlFlowGraph);

    assertFalse(functionBody.hasChild(variableDeclaration));

    // tryRemoveDeadAssignmentsWithVariableAssignment
    Node variableAssignment = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.NUMBER, 1));
    functionBody.addChild(variableAssignment);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableAssignment = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableAssignment,
        controlFlowGraph);

    assertFalse(functionBody.hasChild(variableAssignment));

    // tryRemoveDeadAssignmentsWithVariableRead
    Node variableRead = new Node(Token.GETPROP, new Node(Token.IDENT, "x"), new Node(Token.IDENT, "length"));
    functionBody.addChild(variableRead);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableRead = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableRead,
        controlFlowGraph);

    assertTrue(functionBody.hasChild(variableRead));

    // tryRemoveDeadAssignmentsWithVariableWrite
    Node variableWrite = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.IDENT, "y"));
    functionBody.addChild(variableWrite);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableWrite = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableWrite,
        controlFlowGraph);

    assertFalse(functionBody.hasChild(variableWrite));

    // tryRemoveDeadAssignmentsWithVariableWriteWithinExpression
    Node expressionWithVariableWrite = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"),
        new Node(Token.PLUS, new Node(Token.IDENT, "y"), new Node(Token.NUMBER, 1)));
    functionBody.addChild(expressionWithVariableWrite);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinExpression = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinExpression,
        controlFlowGraph);

    assertFalse(expressionWithVariableWrite.getParent().hasChild(expressionWithVariableWrite));

    // tryRemoveDeadAssignmentsWithVariableWriteWithinFunctionCall
    Node functionCallWithVariableWrite = new Node(Token.CALL, new Node(Token.IDENT, "func"),
        new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 1)));
    functionBody.addChild(functionCallWithVariableWrite);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinFunctionCall = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinFunctionCall,
        controlFlowGraph);

    assertFalse(functionCallWithVariableWrite.getParent().hasChild(functionCallWithVariableWrite));

    // tryRemoveDeadAssignmentsWithVariableWriteWithinVariableDeclaration
    Node variableDeclarationWithVariableWrite = new Node(Token.VAR, new Node(Token.IDENT, "x"),
        new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 1)),
        new Node(Token.NUMBER, 1));
    functionBody.addChild(variableDeclarationWithVariableWrite);

    NodeTraversal nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinVariableDeclaration = new NodeTraversal(
        compiler, root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversalForTryRemoveDeadAssignmentsWithVariableWriteWithinVariableDeclaration,
        controlFlowGraph);

    assertFalse(variableDeclarationWithVariableWrite.getParent().hasChild(variableDeclarationWithVariableWrite));

    
  }

  @Test(expected = NullPointerException.class)
  public void testNullPointerException() {
    

    // Given
    AbstractCompiler compiler = null;

    // When
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // Then
    // Should throw NullPointerException

    
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException() {
    

    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // When
    Node externs = null;
    Node root = new Node(Token.PROGRAM, externs);

    // Then
    // Should throw IllegalArgumentException

    
  }
}