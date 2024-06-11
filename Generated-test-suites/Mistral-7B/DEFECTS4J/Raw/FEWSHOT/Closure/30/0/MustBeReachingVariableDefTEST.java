package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Unit tests for the {@link MustBeReachingVariableDef} class.
 */
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDefAnalysis() {
    // Create a simple control flow graph for testing.
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope(null, null, "testFunction");
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    // Add some nodes to the control flow graph.
    Node entryNode = new Node(Token.BLOCK, null);
    Node aAssignmentNode = new Node(Token.ASSIGN, null);
    Node bAssignmentNode = new Node(Token.ASSIGN, null);
    Node cAssignmentNode = new Node(Token.ASSIGN, null);
    Node ifNode = new Node(Token.IF, null);
    Node elseNode = new Node(Token.BLOCK, null);
    Node returnNode = new Node(Token.RETURN, null);

    // Set the entry node as the root node of the control flow graph.
    cfg.setRootNode(entryNode);

    // Add edges to the control flow graph.
    entryNode.addEdge(aAssignmentNode);
    entryNode.addEdge(bAssignmentNode);
    aAssignmentNode.addEdge(cAssignmentNode);
    bAssignmentNode.addEdge(ifNode);
    ifNode.addEdge(cAssignmentNode);
    ifNode.addEdge(elseNode);
    elseNode.addEdge(returnNode);

    // Create a new instance of MustBeReachingVariableDef.
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Run the analysis.
    MustDef output = analysis.compute(entryNode, analysis.createInitialEstimateLattice());

    // Check the result.
    Map<Var, Definition> reachingDef = output.reachingDef;

    // The variable "a" is defined at the assignment node.
    Definition aDef = reachingDef.get("a");
    assertEquals(aAssignmentNode, aDef.node);

    // The variable "b" is defined at the assignment node.
    Definition bDef = reachingDef.get("b");
    assertEquals(bAssignmentNode, bDef.node);

    // The variable "c" is defined at the assignment node reached from both
    // the "a" and "b" assignments.
    Definition cDef = reachingDef.get("c");
    assertEquals(cAssignmentNode, cDef.node);

    // Check that the "else" block does not affect the reaching definitions.
    reachingDef = output.reachingDef;
    assertNull(reachingDef.get("x"));
  }
}