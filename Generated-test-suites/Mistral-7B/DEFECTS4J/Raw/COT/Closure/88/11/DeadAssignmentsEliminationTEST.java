package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    rootNode = Node.objectLiteral();
  }

  @Test
  public void testNoAssignments() {
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
    ControlFlowGraph<Node> controlFlowGraph = Mockito.mock(ControlFlowGraph.class);
    Preconditions.checkNotNull(controlFlowGraph);
    DeadAssignmentsElimination.FlowState<DataFlowAnalysis.LiveVariableLattice> state =
        Mockito.mock(DeadAssignmentsElimination.FlowState.class);

    Mockito.when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class).iterator());

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(Node.nullLiteral(), rootNode);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    Mockito.verify(compiler, Mockito.times(0)).reportCodeChange();
  }

  @Test
  public void testDeadVariableWithinControlStructure() {
    Node condition = Node.newBinaryOp(Token.LOGICAL_AND, Node.booleanLiteral(true),
        Node.newIdentifier("x"));
    Node statementX = Node.newAssignment(Token.ASSIGN, Node.newIdentifier("x"),
        Node.newNumber(1));
    Node statementY = Node.newAssignment(Token.ASSIGN, Node.newIdentifier("y"),
        Node.newNumber(2));

    Node ifNode = Node.newIfStatement(condition, statementX, statementY);

    rootNode.setFirstChild(ifNode);

    DeadAssignmentsElimination.FlowState<DataFlowAnalysis.LiveVariableLattice> stateX =
        Mockito.mock(DeadAssignmentsElimination.FlowState.class);
    DeadAssignmentsElimination.FlowState<DataFlowAnalysis.LiveVariableLattice> stateY =
        Mockito.mock(DeadAssignmentsElimination.FlowState.class);

    Mockito.when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class).iterator());
    Mockito.when(controlFlowGraph.getAnnotation(Mockito.any()))
        .thenReturn(stateX);
    Mockito.when(stateX.getOut())
        .thenReturn(Mockito.mock(DataFlowAnalysis.LiveVariableLattice.class)
            .setLive(Mockito.mock(DataFlowAnalysis.VariableReference.class), true));

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(Node.nullLiteral(), rootNode);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(statementX, Mockito.times(1)).removeChild(Mockito.any());
  }

  @Test
  public void testDeadVariableWithinExpression() {
    Node x = Node.newIdentifier("x");
    Node y = Node.newIdentifier("y");
    Node statementX = Node.newAssignment(Token.ASSIGN, x, Node.newIdentifier("y"));
    Node expression = Node.newBinaryOp(Token.MINUS, Node.newIdentifier("z"), x);

    rootNode.setFirstChild(expression);

    DeadAssignmentsElimination.FlowState<DataFlowAnalysis.LiveVariableLattice> stateX =
        Mockito.mock(DeadAssignmentsElimination.FlowState.class);
    DeadAssignmentsElimination.FlowState<DataFlowAnalysis.LiveVariableLattice> stateY =
        Mockito.mock(DeadAssignmentsElimination.FlowState.class);

    Mockito.when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class).iterator());
    Mockito.when(controlFlowGraph.getAnnotation(Mockito.any()))
        .thenReturn(stateX);
    Mockito.when(stateX.getOut())
        .thenReturn(Mockito.mock(DataFlowAnalysis.LiveVariableLattice.class)
            .setLive(Mockito.mock(DataFlowAnalysis.VariableReference.class), false));

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(Node.nullLiteral(), rootNode);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(statementX, Mockito.times(1)).removeChild(Mockito.any());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(null);
  }

  @Test
  public void testVariableAssignmentWithinFunction() {
    Node functionNode = Node.newFunction(Node.newIdentifier("f"), new Node[0],
        Node.newBlock(), Node.nullLiteral(), Node.nullLiteral());
    Node x = Node.newIdentifier("x");
    Node statementX = Node.newAssignment(Token.ASSIGN, x, Node.newNumber(1));
    Node statementY = Node.newAssignment(Token.ASSIGN, Node.newIdentifier("y"),
        Node.newNumber(2));

    Node ifCondition = Node.newBinaryOp(Token.LOGICAL_AND, Node.booleanLiteral(true),
        Node.newIdentifier("x"));
    Node ifStatement = Node.newIfStatement(ifCondition, statementX, statementY);

    functionNode.setFirstChild(ifStatement);

    Scope scope = new Scope();
    Scope.Var xVar = new Scope.Var(x.getString());
    Scope.Var yVar = new Scope.Var(Node.newIdentifier("y").getString());

    scope.declare(xVar);
    scope.declare(yVar);

    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
    ControlFlowGraph<Node> controlFlowGraph = Mockito.mock(ControlFlowGraph.class);

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(Node.nullLiteral(), functionNode);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    ArgumentCaptor<Node> deletedNodeCaptor = ArgumentCaptor.forClass(Node.class);
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(statementX, Mockito.times(1)).removeChild(Mockito.any());
    Mockito.verify(nodeTraversal, Mockito.times(1)).replaceChild(
        Mockito.eq(functionNode), deletedNodeCaptor.capture());
  }
}