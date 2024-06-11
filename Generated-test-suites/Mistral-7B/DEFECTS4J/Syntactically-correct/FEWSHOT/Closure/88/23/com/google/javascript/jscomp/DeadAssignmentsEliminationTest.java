package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  private static final Node DUMMY_EXterns = Node.newText("dummy externs");
  private static final Node DUMMY_ROOT = Node.newText("dummy root");
  private static final AbstractCompiler COMPILER = mock(AbstractCompiler.class);
  private static final ControlFlowGraph<Node> CFG = mock(ControlFlowGraph.class);
  private static final LiveVariablesAnalysis LIVENESS = mock(LiveVariablesAnalysis.class);
  private static final NodeTraversal TRAVERSAL = mock(NodeTraversal.class);
  private static final Scope SCOPE = mock(Scope.class);
  private static final DiGraphNode<Node, Branch> NODE = mock(DiGraphNode.class);

  @Test
  public void testEnterScope() {
    when(NODE.getValue()).thenReturn(DUMMY_ROOT);
    when(SCOPE.getVar(anyString())).thenReturn(mock(Var.class));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(COMPILER);

    deadAssignmentsElimination.enterScope(TRAVERSAL);

    // Global scope check
    verify(TRAVERSAL, never()).getScope();

    // Check inner function presence
    when(NODE.getValue()).thenReturn(Node.newFunction("dummy", null, null, null));
    deadAssignmentsElimination.enterScope(TRAVERSAL);
    verify(TRAVERSAL).getScopeRoot().getLastChild();
    verify(TRAVERSAL, never()).process(DUMMY_EXterns, DUMMY_ROOT);

    // Check no assigns presence
    when(NODE.getValue()).thenReturn(Node.newText(""));
    deadAssignmentsElimination.enterScope(TRAVERSAL);
    verify(TRAVERSAL, never()).process(DUMMY_EXterns, DUMMY_ROOT);
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    when(NODE.getValue()).thenReturn(DUMMY_ROOT);
    when(CFG.getDirectedGraphNodes()).thenReturn(Collections.singletonList(NODE).iterator());
    when(LIVENESS.getEscapedLocals()).thenReturn(Collections.emptyList());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(COMPILER);

    deadAssignmentsElimination.tryRemoveDeadAssignments(TRAVERSAL, CFG);

    // Check no nodes processed
    verify(TRAVERSAL, never()).process(any(Node.class), any(Node.class));
  }

  @Test
  public void testTryRemoveAssignment() {
    String lhs = "x";
    String rhs = "y";
    Node lhsNode = Node.newName(lhs);
    Node rhsNode = Node.newName(rhs);
    Node assignment = new Node(Token.ASSIGN, lhsNode, rhsNode);
    Node parent = Node.newText("parent");
    Node exprRoot = Node.newText("exprRoot");

    when(NODE.getValue()).thenReturn(exprRoot);
    when(CFG.getDirectedGraphNodes()).thenReturn(Arrays.asList(NODE).iterator());
    when(LIVENESS.getEscapedLocals()).thenReturn(Collections.emptyList());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(COMPILER);

    deadAssignmentsElimination.tryRemoveAssignment(TRAVERSAL, assignment, exprRoot, LIVENESS);

    // Check no children removed
    verify(assignment, never()).removeChild(any(Node.class));
    verify(parent, never()).replaceChild(assignment, any(Node.class));

    // Test dead assignment elimination
    when(LIVENESS.getOut().isLive(eq(lhs))).thenReturn(false);
    deadAssignmentsElimination.tryRemoveAssignment(TRAVERSAL, assignment, exprRoot, LIVENESS);

    verify(assignment).removeChild(rhsNode);
    verify(parent).replaceChild(assignment, rhsNode);
    verify(COMPILER).reportCodeChange();

    // Test dead assignment elimination with increment/decrement operator
    Node increment = new Node(Token.INC, lhsNode);
    Node decrement = new Node(Token.DEC, lhsNode);
    deadAssignmentsElimination.tryRemoveAssignment(TRAVERSAL, increment, exprRoot, LIVENESS);

    verify(lhsNode).getParent().replaceChild(increment, Node.newNumber(0).copyInformationFrom(increment));
    verify(COMPILER).reportCodeChange();

    deadAssignmentsElimination.tryRemoveAssignment(TRAVERSAL, decrement, exprRoot, LIVENESS);

    verify(lhsNode).getParent().replaceChild(decrement, Node.newNumber(0).copyInformationFrom(decrement));
    verify(COMPILER).reportCodeChange();
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    String variable = "x";
    Node lhs = Node.newName(variable);
    Node xAssign = Node.newAssignment(Token.ASSIGN, lhs, Node.newNumber(1));
    Node yAssign = Node.newAssignment(Token.ASSIGN, lhs, Node.newNumber(2));
    Node print = new Node(Token.CALL, Node.newName("print"), Node.newText("x"));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(COMPILER);

    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(TRAVERSAL, xAssign, exprRoot(xAssign), LIVENESS, variable);

    assertFalse(result);

    result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(TRAVERSAL, print, exprRoot(print), LIVENESS, variable);

    assertTrue(result);

    // Test variable still alive within expression when there is a kill before the read
    Node killingAssignment = Node.newAssignment(Token.ASSIGN, lhs, Node.newNumber(3));
    result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(TRAVERSAL, xAssign, exprRoot(xAssign), LIVENESS, variable);

    assertFalse(result);

    // Test variable still alive within expression when there is a read before the kill
    Node readingAssignment = Node.newAssignment(Token.ASSIGN, lhs, Node.newName("y"));
    result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(TRAVERSAL, yAssign, exprRoot(yAssign), LIVENESS, variable);

    assertFalse(result);

    result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(TRAVERSAL, readingAssignment, exprRoot(readingAssignment), LIVENESS, variable);

    assertTrue(result);
  }

  private Node exprRoot(Node node) {
    EquivalenceNode equivalenceNode = mock(EquivalenceNode.class);
    when(equivalenceNode.getValue()).thenReturn(node);
    return equivalenceNode;
  }
}