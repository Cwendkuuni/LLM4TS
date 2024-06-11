package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    

    // Test the constructor
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new AbstractCompiler(cfg, null, null, null);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope,
        compiler);
    assertNotNull(analysis);

    // Test getEscapedLocals()
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());

    // Test a simple case with no live variables
    Node rootNode = Node.newNode(Token.SCRIPT, 0, null);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    LiveVariableLattice result = analysis.getResult();
    assertTrue(result.isLive(0)); // false by default
    assertFalse(result.isLive(1)); // false by default

    // Test a simple case with live variables
    Node assignmentNode = Node.newName("x");
    rootNode.addChild(assignmentNode);
    Node valueNode = Node.newNumber(1);
    assignmentNode.addChild(valueNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0));

    // Test a case with a conditional statement
    Node ifNode = Node.newToken(Token.IF, 0);
    rootNode.addChild(ifNode);
    Node conditionNode = Node.newBoolean(true);
    ifNode.addChild(conditionNode);
    Node assignmentNode2 = Node.newName("x");
    ifNode.addChild(assignmentNode2);
    valueNode = Node.newNumber(2);
    assignmentNode2.addChild(valueNode);
    Node elseNode = Node.newToken(Token.ELSE, 0);
    ifNode.addChild(elseNode);
    Node elseAssignmentNode = Node.newName("x");
    elseNode.addChild(elseAssignmentNode);
    valueNode = Node.newNumber(3);
    elseAssignmentNode.addChild(valueNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0)); // x is live in both branches

    // Test a case with a loop
    Node whileNode = Node.newToken(Token.WHILE, 0);
    rootNode.addChild(whileNode);
    Node conditionNode2 = Node.newBoolean(true);
    whileNode.addChild(conditionNode2);
    Node assignmentNode3 = Node.newName("x");
    whileNode.addChild(assignmentNode3);
    valueNode = Node.newNumber(4);
    assignmentNode3.addChild(valueNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0)); // x is live inside the loop

    // Test a case with a function
    Node functionNode = Node.newToken(Token.FUNCTION, 0, null);
    rootNode.addChild(functionNode);
    Node paramNode = Node.newName("x");
    functionNode.addChild(paramNode);
    Node returnNode = Node.newReturnStatement();
    functionNode.addChild(returnNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(functionNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertFalse(result.isLive(0)); // x is dead since it's a formal parameter

    // Test a case with a for loop
    Node forNode = Node.newToken(Token.FOR, 0);
    rootNode.addChild(forNode);
    Node initializationNode = Node.newName("x");
    forNode.addChild(initializationNode);
    Node conditionNode3 = Node.newBoolean(true);
    forNode.addChild(conditionNode3);
    Node incrementNode = Node.newName("x");
    forNode.addChild(incrementNode);
    Node loopBodyNode = Node.newBlock();
    forNode.addChild(loopBodyNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0)); // x is live inside the loop

    // Test a case with a break statement
    Node breakNode = Node.newToken(Token.BREAK, 0);
    loopBodyNode.addChild(breakNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertFalse(result.isLive(0)); // x is dead after the break statement

    // Test a case with a try-catch statement
    Node tryNode = Node.newTokenToken(Token.TRY, Token.TRY, 0);
    rootNode.addChild(tryNode);
    Node assignmentNode4 = Node.newName("x");
    tryNode.addChild(assignmentNode4);
    valueNode = Node.newNumber(5);
    assignmentNode4.addChild(valueNode);
    Node catchNode = Node.newTokenToken(Token.CATCH, Token.TRY, 0);
    tryNode.addChild(catchNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0)); // x is live since the try block might be executed

    // Test a case with a throw statement
    Node throwNode = Node.newToken(Token.THROW, 0);
    tryNode.addChild(throwNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertFalse(result.isLive(0)); // x is dead after the throw statement

    // Test a case with exceptions
    Node throwNode2 = Node.newToken(Token.THROW, 0);
    Node throwExpressionNode = Node.newName("e");
    throwNode2.addChild(throwExpressionNode);
    tryNode.addChild(throwNode2);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertFalse(result.isLive(0)); // x is dead after the throw statement
    assertFalse(result.isLive(1)); // e is dead after the throw statement

    // Test a case with a try-finally statement
    Node finallyNode = Node.newTokenToken(Token.FINALLY, Token.TRY, 0);
    tryNode.addChild(finallyNode);
    Node assignmentNode5 = Node.newName("x");
    finallyNode.addChild(assignmentNode5);
    valueNode = Node.newNumber(6);
    assignmentNode5.addChild(valueNode);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.compute(rootNode, analysis.createEntryLattice());
    result = analysis.getResult();
    assertTrue(result.isLive(0)); // x is live since the try block might be executed

    
  }
}