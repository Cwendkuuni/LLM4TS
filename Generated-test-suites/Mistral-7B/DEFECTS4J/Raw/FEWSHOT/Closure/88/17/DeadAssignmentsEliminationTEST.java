package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

/**
 * Test class for DeadAssignmentsElimination.
 */
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Node root;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    cfg = mock(ControlFlowGraph.class);
    root = new Node(Token.PROGRAM);
  }

  @org.junit.Test
  public void testEnterScope() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal traversal = new NodeTraversal(root, cfg, new Scope(root), elimination);
    elimination.enterScope(traversal);
  }

  @org.junit.Test
  public void testExitScope() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal traversal = new NodeTraversal(root, cfg, new Scope(root), elimination);
    elimination.exitScope(traversal);
  }

  @org.junit.Test
  public void testVisit() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal traversal = new NodeTraversal(root, cfg, new Scope(root), elimination);
    elimination.visit(traversal, root, null);
  }

  @org.junit.Test
  public void testTryRemoveDeadAssignments() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);
    ControlFlowGraph.Branch branch = mock(Branch.class);
    when(branch.getValue()).thenReturn(new Node(Token.IF, null, null));
    when(cfg.getDirectedGraphNodes()).thenReturn(Iterables.singletonList(new DiGraphNode<>(branch)));
    when(cfg.getAnnotation(branch)).thenReturn(new FlowState<>(null));

    elimination.tryRemoveDeadAssignments(new NodeTraversal(root, cfg, new Scope(root), elimination), cfg);
  }

  @org.junit.Test
  public void testTryRemoveAssignment() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);

    // Test empty assignment
    Node assignment = new Node(Token.ASSIGN, null, null);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, new Scope(root), elimination), assignment, null);

    // Test assignment with live variable
    String variable = "x";
    Var var = new Var(variable, false);
    Scope scope = new Scope(root);
    scope.putVar(variable, var);
    Node lhs = new Node(Token.NAME, variable);
    Node rhs = new Node(Token.NUMBER, 1.0);
    Node assignmentNode = new Node(Token.ASSIGN, lhs, rhs);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), assignmentNode, null);
    assertThat(scope.getVar(variable)).isNotNull();

    // Test assignment with dead variable
    Node deadAssignment = new Node(Token.ASSIGN, lhs, rhs);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), deadAssignment, null);
    assertNull(scope.getVar(variable));

    // Test increment operator
    Node increment = new Node(Token.INC, lhs);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), increment, null);

    // Test decrement operator
    Node decrement = new Node(Token.DEC, lhs);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), decrement, null);

    // Test assignment within expression
    Node expression = new Node(Token.ASSIGN, assignmentNode, null);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), expression, assignmentNode);

    // Test assignment within if statement
    Node ifStatement = new Node(Token.IF, null, assignmentNode);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), ifStatement, null);

    // Test assignment within for statement
    Node forStatement = new Node(Token.FOR, null, assignmentNode);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), forStatement, null);

    // Test assignment within switch statement
    Node switchStatement = new Node(Token.SWITCH, null, assignmentNode);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), switchStatement, null);

    // Test assignment within return statement
    Node returnStatement = new Node(Token.RETURN, assignmentNode, null);
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), returnStatement, null);

    // Test assignment within function call expression
    Node functionCall = new Node(Token.CALL, new Node(Token.NAME, "func"), new Node(Token.NUMBER, 1.0));
    elimination.tryRemoveAssignment(new NodeTraversal(root, cfg, scope, elimination), functionCall, null);
  }

  @org.junit.Test
  public void testIsVariableStillLiveWithinExpression() {
    DeadAssignmentsElimination elimination =
        new DeadAssignmentsElimination(compiler);

    // Test variable not live
    Node deadAssignment = new Node(Token.ASSIGN, null, null);
    Node expressionRoot = new Node(Token.ASSIGN, null, deadAssignment);
    boolean result = elimination.isVariableStillLiveWithinExpression(deadAssignment, expressionRoot, "x");
    assertFalse(result);

    // Test variable live
    Var variable = new Var("x", false);
    Scope scope = new Scope(root);
    scope.putVar("x", variable);
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.NUMBER, 1.0);
    Node assignmentNode = new Node(Token.ASSIGN, lhs, rhs);
    Node expressionRootLive = new Node(Token.ASSIGN, null, assignmentNode);
    result = elimination.isVariableStillLiveWithinExpression(assignmentNode, expressionRootLive, "x");
    assertTrue(result);

    // Test variable live and read before kill
    Node read = new Node(Token.NAME, "x");
    Node kill = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 2.0));
    Node expressionRootLiveReadBeforeKill = new Node(Token.ASSIGN, null, kill);
    result = elimination.isVariableStillLiveWithinExpression(kill, expressionRootLive, "x");
    assertTrue(result);
    result = elimination.isVariableStillLiveWithinExpression(read, expressionRootLiveReadBeforeKill, "x");
    assertTrue(result);

    // Test variable live and killed before read
    Node readDead = new Node(Token.NAME, "x");
    Node killReadDead = new Node(Token.ASSIGN, readDead, new Node(Token.NUMBER, 2.0));
    Node expressionRootLiveKilledBeforeRead = new Node(Token.ASSIGN, null, killReadDead);
    result = elimination.isVariableStillLiveWithinExpression(killReadDead, expressionRootLive, "x");
    assertFalse(result);
    result = elimination.isVariableStillLiveWithinExpression(readDead, expressionRootLiveKilledBeforeRead, "x");
    assertFalse(result);
  }
}