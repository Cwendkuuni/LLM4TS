package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractPostOrderTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    

    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    Scope scope = new Scope();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    when(compiler.getControlFlowGraph()).thenReturn(cfg);

    // When
    Node root = Node.newProgram();
    deadAssignmentsElimination.process(null, root);

    // Then
    AbstractPostOrderTraversal traversal = new AbstractPostOrderTraversal();
    NodeTraversal nodeTraversal = new NodeTraversal(traversal);
    NodeTraversal t = nodeTraversal.setCurrentScope(scope, root);
    t.traverse();

    // Basic functionality test
    Predicate<Node> matchAssignments =
        new Predicate<Node>() {
          @Override
          public boolean apply(Node n) {
            return NodeUtil.isAssignmentOp(n) ||
                NodeUtil.isAssignmentOp(n) ||
                NodeUtil.isIncrementOperator(n) ||
                NodeUtil.isDecrementOperator(n);
          }
        };

    int initialAssignmentCount = nodeTraversal.count(matchAssignments);
    assertEquals(
        "Methods in DeadAssignmentsElimination should remove dead assignments.",
        initialAssignmentCount > 0,
        "Dead assignments exist in the original code.");

    // Test edge cases and exception handling

    // Test global variables
    scope = new Scope();
    root = Node.newProgram();
    scope.setIsGlobal(true);
    t = new NodeTraversal(traversal).setCurrentScope(scope, root);
    t.traverse();
    int globalVariablesCount = nodeTraversal.count(matchAssignments);
    assertEquals(
        "Dead assignments elimination should not remove global variable assignments.",
        globalVariablesCount == initialAssignmentCount,
        "Global variables should not be removed.");

    // Test function variables
    scope = new Scope();
    root = Node.newFunctionDeclaration("test", Node.newEmptyStatementList(),
        Node.newEmptyStatementList());
    scope.setIsGlobal(false);
    t = new NodeTraversal(traversal).setCurrentScope(scope, root);
    t.traverse();
    int functionVariablesCount = nodeTraversal.count(matchAssignments);
    assertEquals(
        "Dead assignments elimination should not remove function variable assignments.",
        functionVariablesCount == initialAssignmentCount,
        "Function variables should not be removed.");

    // Test variables that are assigned multiple times
    scope = new Scope();
    root = Node.newFunctionDeclaration("test", Node.newEmptyStatementList(),
        Node.newEmptyStatementList());
    scope.setIsGlobal(false);
    Node varNode = Node.newName("var");
    Node assign1 = Node.newAssignment(varNode, Node.newNumber(1));
    Node assign2 = Node.newAssignment(varNode, Node.newNumber(2));
    Node funcBody = Node.newExpressionStatement(assign1);
    funcBody.addChild(assign2);
    root.setBody(funcBody);
    t = new NodeTraversal(traversal).setCurrentScope(scope, root);
    t.traverse();
    int multipleAssignmentsCount = nodeTraversal.count(matchAssignments);
    assertEquals(
        "Dead assignments elimination should remove redundant assignments.",
        multipleAssignmentsCount < initialAssignmentCount,
        "Redundant assignments should be removed.");

    
  }
}