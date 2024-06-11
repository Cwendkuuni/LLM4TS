package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for the DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private ControlFlowGraph<Node> cfg;

  @Mock
  private DataFlowAnalysis<LiveVariableLattice> liveness;

  @Mock
  private Scope scope;

  @Mock
  private NodeTraversal nodeTraversal;

  @InjectMocks
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);
  }

  @Test
  public void testEnterScope_globalScope() {
    deadAssignmentsElimination.enterScope(nodeTraversal);
    assertThat(deadAssignmentsElimination.isProcessed).isFalse();
  }

  @Test
  public void testEnterScope_noAssignments() {
    when(cfg.getDirectedGraphNodes()).thenReturn(Graph.emptyList());
    deadAssignmentsElimination.enterScope(nodeTraversal);
    assertThat(deadAssignmentsElimination.isProcessed).isFalse();
  }

  @Test
  public void testEnterScope_simpleAssignments() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(cfg.getDirectedGraphNodes()).thenReturn(Graph.list(new NodeMock(root)));
    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));

    deadAssignmentsElimination.enterScope(nodeTraversalMock);
    deadAssignmentsElimination.process(Node.newObjectLiteral(), root);
    NodeMock node = (NodeMock) nodeTraversalMock.nodes.get(0);
    Node assignX = NodeUtil.createAssign(node, "x", Node.newNumber(1));
    Node assignY = NodeUtil.createAssign(node, "y", Node.newNumber(2));

    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX);
    nodeTraversalMock.traverse(deadAssignmentsElimination, assignY);

    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
    assertThat(node.getChildCount()).isZero();
  }

  @Test
  public void testEnterScope_loop() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(cfg.getDirectedGraphNodes()).thenReturn(Graph.list(
        new NodeMock(NodeUtil.getIfStatement("x", NodeUtil.newBooleanLiteral(true), root)),
        new NodeMock(NodeUtil.createAssign(NodeUtil.getIfStatement("x", NodeUtil.newBooleanLiteral(true), root), "x", Node.newNumber(1))),
        new NodeMock(NodeUtil.getIfStatement("x", NodeUtil.newBooleanLiteral(true), root)),
        new NodeMock(NodeUtil.createAssign(NodeUtil.getIfStatement("x", NodeUtil.newBooleanLiteral(true), root), "x", Node.newNumber(2)))
    ));
    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));

    deadAssignmentsElimination.enterScope(nodeTraversalMock);
    deadAssignmentsElimination.process(Node.newObjectLiteral(), root);

    NodeMock node = (NodeMock) nodeTraversalMock.nodes.get(0);
    Node ifStatement = (NodeMock) nodeTraversalMock.nodes.get(1);
    Node assignX1 = (NodeMock) nodeTraversalMock.nodes.get(2);
    Node ifStatement2 = (NodeMock) nodeTraversalMock.nodes.get(3);
    Node assignX2 = (NodeMock) nodeTraversalMock.nodes.get(4);

    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX1);
    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX2);

    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
    assertThat(assignX1.getChildCount()).isZero();
    assertThat(assignX2.getChildCount()).isZero();
  }

  @Test
  public void testEnterScope_function() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(cfg.getDirectedGraphNodes()).thenReturn(Graph.list(new NodeMock(root)));
    Node functionNode = NodeUtil.newFunctionStatement("function foo() { }", root);
    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    Node assignX = NodeUtil.createAssign(functionNode, "x", Node.newNumber(1));

    deadAssignmentsElimination.enterScope(nodeTraversalMock);
    deadAssignmentsElimination.process(Node.newObjectLiteral(), root);
    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX);

    NodeMock node = (NodeMock) nodeTraversalMock.nodes.get(0);
    Node function = (NodeMock) nodeTraversalMock.nodes.get(1);
    Node assignX1 = (NodeMock) nodeTraversalMock.nodes.get(2);

    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
    assertThat(assignX1.getChildCount()).isZero();
  }

  @Test(expected = AssertionError.class)
  public void testEnterScope_invalidStatement() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(cfg.getDirectedGraphNodes()).thenReturn(Graph.list(new NodeMock(root)));
    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    Node invalidStatement = Node.newExpressionStatement(Node.newIdentifier("x"));

    deadAssignmentsElimination.enterScope(nodeTraversalMock);
    deadAssignmentsElimination.process(Node.newObjectLiteral(), root);
    nodeTraversalMock.traverse(deadAssignmentsElimination, invalidStatement);

    NodeMock node = (NodeMock) nodeTraversalMock.nodes.get(0);
    NodeMock nodeMock = (NodeMock) nodeTraversalMock.nodes.get(1);

    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
    // This is an assertion error because we expect the test to fail.
  }

  @Test
  public void testTryRemoveAssignment_assignmentToDeadVariable() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    Node assignX = NodeUtil.createAssign(NodeUtil.newObjectLiteral(), "x", Node.newNumber(1));
    NodeMock node = new NodeMock(assignX);
    nodeTraversalMock.nodes.add(node);
    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX);

    NodeMock expectedNode = new NodeMock(assignX);
    NodeMock actualNode = (NodeMock) nodeTraversalMock.nodes.get(0);

    assertThat(actualNode).isEqualTo(expectedNode);
    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
  }

  @Test
  public void testTryRemoveAssignment_assignmentToLiveVariable() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());
    when(liveness.getIn()).thenReturn(new LiveVariableLattice());
    when(liveness.getOut()).thenReturn(new LiveVariableLattice());

    Node assignX = NodeUtil.createAssign(NodeUtil.newObjectLiteral(), "x", Node.newNumber(1));
    NodeMock node = new NodeMock(assignX);
    nodeTraversalMock.nodes.add(node);
    nodeTraversalMock.traverse(deadAssignmentsElimination, assignX);

    NodeMock expectedNode = new NodeMock(assignX);
    NodeMock actualNode = (NodeMock) nodeTraversalMock.nodes.get(0);

    assertThat(actualNode).isEqualTo(expectedNode);
    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
  }

  @Test
  public void testTryRemoveAssignment_conditionalStatement() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    Node ifStatement = NodeUtil.getIfStatement("x", NodeUtil.newBooleanLiteral(true), root);
    Node assignX = NodeUtil.createAssign(ifStatement, "x", Node.newNumber(1));
    NodeMock node = new NodeMock(assignX);
    nodeTraversalMock.nodes.add(node);
    nodeTraversalMock.traverse(deadAssignmentsElimination, ifStatement);

    NodeMock expectedNode = new NodeMock(assignX);
    NodeMock actualNode = (NodeMock) nodeTraversalMock.nodes.get(0);

    assertThat(actualNode).isEqualTo(expectedNode);
    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
  }

  @Test
  public void testTryRemoveAssignment_forStatement() {
    NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
    Node root = Node.newObjectLiteral();

    when(scope.isGlobal()).thenReturn(false);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new VarMock("x", "x"));
    when(liveness.getEscapedLocals()).thenReturn(new LiveVariableLattice());

    Node forStatement = NodeUtil.newForStatement("i", Node.newNumberLiteral(1),
        NodeUtil.newNumberLiteral(10), NodeUtil.newBooleanLiteral(true), root,
        NodeUtil.newExpressionStatement(NodeUtil.newAssignment("x", NodeUtil.newNumberLiteral(1))));
    NodeMock node = new NodeMock(NodeUtil.newAssignment("x", NodeUtil.newNumberLiteral(1)));
    nodeTraversalMock.nodes.add(node);
    nodeTraversalMock.traverse(deadAssignmentsElimination, forStatement);

    NodeMock expectedNode = new NodeMock(node.getParent());
    NodeMock actualNode = (NodeMock) nodeTraversalMock.nodes.get(0);

    assertThat(actualNode).isEqualTo(expectedNode);
    assertThat(deadAssignmentsElimination.isProcessed).isTrue();
  }

  private static class NodeMock extends Node {

    private NodeMock parent;
    private int index;
    private NodeMock prev;
    private NodeMock next;
    private Node[] children;
    private Node[] nodes;

    NodeMock() {
      super(Token.EMPTY, null);
    }

    NodeMock(Node node) {
      super(node.getType(), node.getParent());
      this.parent = node.getParent();
      this.index = node.getIndex();
      this.children = node.getChildren();
      this.nodes = new NodeMock[children.length + 1];
      System.arraycopy(children, 0, nodes, 0, children.length);
      nodes[nodes.length - 1] = this;
    }

    @Override
    public Node getParent() {
      return parent;
    }

    @Override
    public int getIndex() {
      return index;
    }

    @Override
    public Node[] getChildren() {
      return nodes;
    }

  }

  private static class VarMock implements Var {

    private String name;

    VarMock(String name, String declarationName) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDeclarationName() {
      return declarationName;
    }

  }

}