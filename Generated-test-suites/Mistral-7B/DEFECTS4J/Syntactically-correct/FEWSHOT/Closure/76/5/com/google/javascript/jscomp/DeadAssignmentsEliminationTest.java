package org.example.deadassignmentselimination;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.GraphBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private ControlFlowGraph<Node> controlFlowGraph;
  private NodeTraversal nodeTraversal;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = new GraphBuilder().buildControlFlowGraph();
    nodeTraversal = new NodeTraversal(mock(Node.class), controlFlowGraph);
    scope = mock(Scope.class);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testEmptyProgram() {
    deadAssignmentsElimination.process(null, null);
    assertTrue(controlFlowGraph.isEmpty());
  }

  @Test
  public void testGlobalVariableAssignment() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    globalScope.declareVar("x", Var.VAR, null);
    root.setFirstChild(globalScope);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    globalScope.getVar("x").setValue(assignmentNode);

    deadAssignmentsElimination.process(null, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertTrue(state.getOut().isLive(globalScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignment() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope localScope = new Scope(Token.BLOCK, globalScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);
    globalScope.getLastChild().setFirstChild(localScope);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    localScope.getVar("x").setValue(assignmentNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignmentInsideFunction() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope functionScope = new Scope(Token.FUNCTION_DECLARATION, null, null);
    functionScope.setFunctionName("foo");
    functionScope.setScopeRoot(new Node(Token.VAR, null, null));
    globalScope.getLastChild().setFirstChild(functionScope);

    Scope localScope = new Scope(Token.BLOCK, functionScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    localScope.getVar("x").setValue(assignmentNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignmentInsideFunctionWithFunctionCall() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope functionScope = new Scope(Token.FUNCTION_DECLARATION, null, null);
    functionScope.setFunctionName("foo");
    functionScope.setScopeRoot(new Node(Token.VAR, null, null));
    globalScope.getLastChild().setFirstChild(functionScope);

    Scope localScope = new Scope(Token.BLOCK, functionScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);

    Node functionCallNode = new Node(Token.CALL, null, null);
    functionCallNode.setFirstChild(new Node(Token.NAME, "foo", null));
    functionCallNode.setLastChild(new Node(Token.DOT, null, null));

    functionScope.getVar("foo").setValue(functionCallNode);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    localScope.getVar("x").setValue(assignmentNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignmentInsideFunctionWithReturnStatement() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope functionScope = new Scope(Token.FUNCTION_DECLARATION, null, null);
    functionScope.setFunctionName("foo");
    functionScope.setScopeRoot(new Node(Token.VAR, null, null));
    globalScope.getLastChild().setFirstChild(functionScope);

    Scope localScope = new Scope(Token.BLOCK, functionScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    localScope.getVar("x").setValue(assignmentNode);

    Node returnStatementNode = new Node(Token.RETURN, null, null);
    localScope.getVar("x").setValue(returnStatementNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignmentInsideFunctionWithReturnStatementAndFunctionCall() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope functionScope = new Scope(Token.FUNCTION_DECLARATION, null, null);
    functionScope.setFunctionName("foo");
    functionScope.setScopeRoot(new Node(Token.VAR, null, null));
    globalScope.getLastChild().setFirstChild(functionScope);

    Scope localScope = new Scope(Token.BLOCK, functionScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);

    Node functionCallNode = new Node(Token.CALL, null, null);
    functionCallNode.setFirstChild(new Node(Token.NAME, "foo", null));
    functionCallNode.setLastChild(new Node(Token.DOT, null, null));

    functionScope.getVar("foo").setValue(functionCallNode);

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode = new Node(Token.NUMBER, null, null);
    assignmentNode.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode.setLastChild(rightHandSideNode);

    localScope.getVar("x").setValue(assignmentNode);

    Node returnStatementNode = new Node(Token.RETURN, null, null);
    returnStatementNode.setFirstChild(functionCallNode);

    localScope.getVar("x").setValue(returnStatementNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }

  @Test
  public void testDeadLocalVariableAssignmentInsideFunctionWithReturnStatementAndLocalVariableAssignment() {
    Node root = new Node(Token.PROGRAM, null, null);
    Scope globalScope = new Scope(Token.BLOCK, null, root);
    root.setFirstChild(globalScope);

    Scope functionScope = new Scope(Token.FUNCTION_DECLARATION, null, null);
    functionScope.setFunctionName("foo");
    functionScope.setScopeRoot(new Node(Token.VAR, null, null));
    globalScope.getLastChild().setFirstChild(functionScope);

    Scope localScope = new Scope(Token.BLOCK, functionScope, null);
    Node localVariableDeclarationNode = new Node(Token.VAR, null, null);
    localVariableDeclarationNode.setFirstChild(new Node(Token.NAME, "x", null));
    localScope.setLocalVariables(new Var[] { localVariableDeclarationNode.getNameChild(0) });
    localScope.setScopeRoot(localVariableDeclarationNode);

    Node assignmentNode1 = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode1 = new Node(Token.NUMBER, null, null);
    assignmentNode1.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode1.setLastChild(rightHandSideNode1);

    localScope.getVar("x").setValue(assignmentNode1);

    Node assignmentNode2 = new Node(Token.ASSIGN, null, null);
    Node rightHandSideNode2 = new Node(Token.NUMBER, null, null);
    assignmentNode2.setFirstChild(new Node(Token.NAME, "x", null));
    assignmentNode2.setLastChild(rightHandSideNode2);

    localScope.getVar("x").setValue(assignmentNode2);

    Node returnStatementNode = new Node(Token.RETURN, null, null);
    returnStatementNode.setFirstChild(new Node(Token.NAME, "x", null));

    localScope.getVar("x").setValue(returnStatementNode);

    nodeTraversal.setScope(localScope);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    DiGraphNode<Node, Branch> node = controlFlowGraph.getBeginNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();
    assertFalse(state.getOut().isLive(localScope.getVar("x")));
  }
}