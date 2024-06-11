package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Setup test environment
    AbstractCompiler compiler = new AbstractCompiler();
    DataFlowAnalysis dataFlowAnalysis = new DataFlowAnalysis(compiler);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        dataFlowAnalysis, null, compiler);
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(
        new NodeTraversal(compiler), null);

    Node root = Node.newBlock("root");
    Node fnBlock = Node.newBlock("fnBlock");
    root.addChild(fnBlock);

    Node assign1 = Node.newName("x");
    Node assign2 = Node.newName("x");
    Node assign3 = Node.newName("x");
    Node assign4 = Node.newName("x");
    Node assign5 = Node.newName("x");

    Node assign1ToX = Node.newAssign(assign1, Node.newNumber(1));
    Node assign2ToX = Node.newAssign(assign2, Node.newNumber(2));
    Node assign3ToX = Node.newAssign(assign3, Node.newNumber(3));
    Node assign4ToX = Node.newAssign(assign4, Node.newNumber(4));
    Node assign5ToX = Node.newAssign(assign5, Node.newNumber(5));

    Node deadAssign1 = Node.newAssign(assign1, assign2);
    Node deadAssign2 = Node.newAssign(assign3, assign1);
    Node deadAssign3 = Node.newAssign(assign4, assign3);
    Node deadAssign4 = Node.newAssign(assign5, assign4);

    Node cond1 = Node.newConditional(Node.newNumber(1), assign1ToX, assign2ToX);
    Node cond2 = Node.newConditional(Node.newNumber(2), assign2ToX, assign3ToX);
    Node cond3 = Node.newConditional(Node.newNumber(3), assign3ToX, assign4ToX);
    Node cond4 = Node.newConditional(Node.newNumber(4), assign4ToX, assign5ToX);

    fnBlock.addChild(assign1ToX);
    fnBlock.addChild(cond1);
    fnBlock.addChild(deadAssign1);

    fnBlock.addChild(assign2ToX);
    fnBlock.addChild(cond2);
    fnBlock.addChild(deadAssign2);

    fnBlock.addChild(assign3ToX);
    fnBlock.addChild(cond3);
    fnBlock.addChild(deadAssign3);

    fnBlock.addChild(assign4ToX);
    fnBlock.addChild(cond4);
    fnBlock.addChild(deadAssign4);

    fnBlock.addChild(assign5ToX);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // Run test
    deadAssignmentsElimination.process(null, root);

    // Assertions
    Graph<Node> graph = controlFlowGraph.buildGraph(root);
    DiGraphNode<Node, Branch> node1 = graph.getNode(assign1ToX);
    DiGraphNode<Node, Branch> node2 = graph.getNode(assign2ToX);
    DiGraphNode<Node, Branch> node3 = graph.getNode(assign3ToX);
    DiGraphNode<Node, Branch> node4 = graph.getNode(assign4ToX);
    DiGraphNode<Node, Branch> node5 = graph.getNode(assign5ToX);
    DiGraphNode<Node, Branch> nodeDead1 = graph.getNode(deadAssign1);
    DiGraphNode<Node, Branch> nodeDead2 = graph.getNode(deadAssign2);
    DiGraphNode<Node, Branch> nodeDead3 = graph.getNode(deadAssign3);
    DiGraphNode<Node, Branch> nodeDead4 = graph.getNode(deadAssign4);

    FlowState<LiveVariableLattice> state1 = node1.getAnnotation();
    FlowState<LiveVariableLattice> state2 = node2.getAnnotation();
    FlowState<LiveVariableLattice> state3 = node3.getAnnotation();
    FlowState<LiveVariableLattice> state4 = node4.getAnnotation();
    FlowState<LiveVariableLattice> state5 = node5.getAnnotation();
    FlowState<LiveVariableLattice> stateDead1 = nodeDead1.getAnnotation();
    FlowState<LiveVariableLattice> stateDead2 = nodeDead2.getAnnotation();
    FlowState<LiveVariableLattice> stateDead3 = nodeDead3.getAnnotation();
    FlowState<LiveVariableLattice> stateDead4 = nodeDead4.getAnnotation();

    // Dead assignments should be removed
    assertTrue(nodeDead1.getValue() == null);
    assertTrue(nodeDead2.getValue() == null);
    assertTrue(nodeDead3.getValue() == null);
    assertTrue(nodeDead4.getValue() == null);

    // Variables should be dead after the assignments
    assertFalse(state1.getIn().isLive(assign1));
    assertFalse(state2.getIn().isLive(assign2));
    assertFalse(state3.getIn().isLive(assign3));
    assertFalse(state4.getIn().isLive(assign4));

    // Variables should be alive before the assignments
    assertTrue(state1.getOut().isLive(assign1));
    assertTrue(state2.getOut().isLive(assign2));
    assertTrue(state3.getOut().isLive(assign3));
    assertTrue(state4.getOut().isLive(assign4));

    // Variables should not be live after the dead assignments are removed
    assertFalse(stateDead1.getIn().isLive(assign1));
    assertFalse(stateDead2.getIn().isLive(assign2));
    assertFalse(stateDead3.getIn().isLive(assign3));
    assertFalse(stateDead4.getIn().isLive(assign4));

    // Variables should still be dead after the removal of the dead assignments
    assertFalse(state1.getIn().isLive(assign1));
    assertFalse(state2.getIn().isLive(assign2));
    assertFalse(state3.getIn().isLive(assign3));
    assertFalse(state4.getIn().isLive(assign4));

  }
}