package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> controlFlowGraph = mock(ControlFlowGraph.class);
    Scope scope = mock(Scope.class);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        Mockito.mock(Iterable.class));

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(new Token(Token.EOF, 0));

    Node assignX = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        Node.newNumber(1));
    Node assignY = new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
        Node.newNumber(1));
    Node assignXDead = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        Node.newNumber(2));
    Node assignXAlive = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        Node.newNumber(1));
    Node assignXDeadInFunction = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        Node.newNumber(2));
    Node assignXAliveInFunction = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        Node.newNumber(1));
    Node assignYDead = new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
        Node.newNumber(2));
    Node assignYAlive = new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
        Node.newNumber(1));
    Node function = new Node(Token.FUNCTION, null, null, null, null, null,
        Node.newBlock(new Node[0]));
    Node functionCall = new Node(Token.CALL, function, Node.newList(new Node[0]));
    Node ifStatement = new Node(Token.IF, Node.newBoolean(true), null, null);
    Node ifStatementBody = new Node(Token.BLOCK, new Node[0]);
    Node forStatement = new Node(Token.FOR, null, null, null, null, null,
        Node.newBlock(new Node[0]));

    Node block = new Node(Token.BLOCK, new Node[0]);
    block.addChild(assignX);
    block.addChild(assignY);
    block.addChild(assignXDead);
    block.addChild(assignYDead);
    block.addChild(assignXAlive);
    block.addChild(assignYAlive);
    block.addChild(function);
    block.addChild(ifStatement);
    block.addChild(forStatement);

    root.addChild(block);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, controlFlowGraph,
        null, null, null, null, null, null, null, null);
    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    deadAssignmentsElimination.process(null, root);

    // Test for dead assignments that are not in functions
    AbstractPostOrderCallback callback = nodeTraversal.getCallback();
    Node actual = callback.getCallbackData();
    Node expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignXAlive, assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are in functions
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class));
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    function.setChildren(new Node[] { assignXAliveInFunction, functionCall });
    block.setChildren(new Node[] { assignXDeadInFunction, assignYAlive, function });

    root.setChildren(new Node[] { block });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are not local variables
    when(scope.isDeclared(ArgumentMatchers.eq("y"), ArgumentMatchers.eq(false)))
        .thenReturn(false);

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignXAlive, assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are killed within the expression
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    assignX.setLeftChild(assignXAlive);
    assignX.setRightChild(assignXDead);

    block.setChildren(new Node[] { assignX, assignYAlive });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are killed after a read
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    assignX.setLeftChild(assignXAlive);
    assignX.setRightChild(assignXAlive);
    assignXDead.setLeftChild(assignX);
    assignXDead.setRightChild(Node.newNumber(2));

    block.setChildren(new Node[] { assignXDead, assignYAlive });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignXAlive, assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are in an if statement
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class));
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    ifStatement.setChildren(new Node[] { assignXDead, ifStatementBody });
    block.setChildren(new Node[] { assignX, assignYAlive, ifStatement });

    root.setChildren(new Node[] { block });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are in a for statement
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class));
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    forStatement.setChildren(new Node[] { assignXDead, ifStatementBody });
    block.setChildren(new Node[] { assignX, assignYAlive, forStatement });

    root.setChildren(new Node[] { block });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are in nested functions
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class));
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    function.setChildren(new Node[] { assignXAliveInFunction, functionCall });
    assignXAliveInFunction.setChildren(new Node[] { assignX, assignXAlive });
    block.setChildren(new Node[] { assignXDeadInFunction, assignYAlive, function });

    root.setChildren(new Node[] { block });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);

    // Test for dead assignments that are in a nested function called multiple times
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Mockito.mock(Iterable.class));
    when(scope.isDeclared(ArgumentMatchers.eq("x"), ArgumentMatchers.eq(false)))
        .thenReturn(true);
    when(scope.getVar(ArgumentMatchers.eq("x"))).thenReturn(new Var("x", 0));

    function.setChildren(new Node[] { assignXAliveInFunction, functionCall });
    assignXAliveInFunction.setChildren(new Node[] { assignX, assignXAlive });
    block.setChildren(new Node[] { assignXDead, assignYAlive, function, functionCall });

    root.setChildren(new Node[] { block });

    nodeTraversal.setRoot(root);
    nodeTraversal.traverse();

    actual = callback.getCallbackData();
    expected = new Node(Token.PROGRAM, null, null, null, null, null,
        Node.newList(assignYAlive));

    assertThat(actual).isEqualTo(expected);
  }
}