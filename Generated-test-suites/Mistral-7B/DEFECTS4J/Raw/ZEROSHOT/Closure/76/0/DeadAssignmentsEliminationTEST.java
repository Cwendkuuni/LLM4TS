package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentElimination() {
    Node root = NodeFactory.createBlock(Token.EOF);
    Node n1 = NodeFactory.createName("x");
    Node n2 = NodeFactory.createName("y");
    Node n3 = NodeFactory.createName("z");
    Node assign1 = NodeFactory.createAssign(Token.ASSIGN, n1, n1);
    Node assign2 = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createNumber(1));
    Node assign3 = NodeFactory.createAssign(Token.ASSIGN, n2, NodeFactory.createNumber(1));
    Node ifStatement = NodeFactory.createConditional(Token.IF, assign3, NodeFactory.createBlock(Token.EOF), NodeFactory.createBlock(Token.EOF));
    Node assign4 = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createName("z"));
    Node assign5 = NodeFactory.createAssign(Token.ASSIGN, n2, n1);
    Node assign6 = NodeFactory.createAssign(Token.ASSIGN, n3, NodeFactory.createName("x"));

    Node block = NodeFactory.createBlock(
        ImmutableList.of(
            assign1,
            assign2,
            ifStatement,
            assign4,
            assign5,
            assign6
        )
    );

    root.setFirstChild(block);

    Scope scope = compiler.getFactory().makeScope(NodeFactory.SCOPE_GLOBAL);
    scope.declareIdentificator("x", Var.VarType.VAR, NodeFactory.makeName("x"));
    scope.declareIdentificator("y", Var.VarType.VAR, NodeFactory.makeName("y"));
    scope.declareIdentificator("z", Var.VarType.VAR, NodeFactory.makeName("z"));

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    deadAssignmentsElimination.process(compiler.getFactory().getExterns(), root);
    nodeTraversal.traverseAllChildren();

    DiGraphNode<Node, Branch> node = nodeTraversal.getControlFlowGraph().getRootNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();

    assertFalse(NodeUtil.has(assign2, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));
    assertFalse(NodeUtil.has(assign4, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));
    assertFalse(NodeUtil.has(assign5, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));
    assertFalse(NodeUtil.has(assign6, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    assertTrue(NodeUtil.has(assign1, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));
    assertTrue(NodeUtil.has(assign3, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    assertFalse(state.getOut().isLive(scope.getVar("x")));
    assertFalse(state.getOut().isLive(scope.getVar("y")));
    assertFalse(state.getOut().isLive(scope.getVar("z")));

    assertFalse(state.getIn().isLive(scope.getVar("x")));
    assertFalse(state.getIn().isLive(scope.getVar("y")));
    assertFalse(state.getIn().isLive(scope.getVar("z")));
  }

  @Test
  public void testDeadAssignmentEliminationWithFunction() {
    Node root = NodeFactory.createBlock(Token.EOF);
    Node n1 = NodeFactory.createName("x");
    Node n2 = NodeFactory.createName("y");
    Node functionName = NodeFactory.createName("testFunction");
    Node functionEntry = NodeFactory.createFunction(Token.FUNCTION, functionName, ImmutableList.of(), NodeFactory.createBlock(Token.EOF));
    root.setFirstChild(functionEntry);

    Node assign1 = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createNumber(1));
    Node assign2 = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createName("y"));
    Node returnStatement = NodeFactory.createReturnStatement(NodeFactory.createName("x"));

    Node block = NodeFactory.createBlock(
        ImmutableList.of(
            NodeFactory.createName("testFunction"),
            assign1,
            assign2,
            returnStatement
        )
    );

    functionEntry.setLastChild(block);

    Scope scope = compiler.getFactory().makeScope(NodeFactory.SCOPE_GLOBAL);
    scope.declareIdentificator("x", Var.VarType.VAR, NodeFactory.makeName("x"));
    scope.declareIdentificator("y", Var.VarType.VAR, NodeFactory.makeName("y"));

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    deadAssignmentsElimination.process(compiler.getFactory().getExterns(), root);
    nodeTraversal.traverseAllChildren();

    DiGraphNode<Node, Branch> node = nodeTraversal.getControlFlowGraph().getRootNode();
    FlowState<LiveVariableLattice> state = node.getAnnotation();

    assertFalse(NodeUtil.has(assign1, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));
    assertFalse(NodeUtil.has(assign2, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    assertTrue(NodeUtil.has(functionEntry, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    assertFalse(state.getOut().isLive(scope.getVar("x")));
    assertFalse(state.getOut().isLive(scope.getVar("y")));

    assertTrue(state.getIn().isLive(scope.getVar("x")));
    assertFalse(state.getIn().isLive(scope.getVar("y")));
  }
}