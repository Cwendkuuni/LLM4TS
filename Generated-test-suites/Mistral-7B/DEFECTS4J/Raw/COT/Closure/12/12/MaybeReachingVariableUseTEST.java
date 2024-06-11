package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Create a sample ControlFlowGraph and Scope for testing
    controlFlowGraph = new ControlFlowGraph<>();
    Node entryNode = new Node(Token.BLOCK);
    Node exitNode = new Node(Token.NAME, "exit");
    Node ifNode = new Node(Token.IF, Token.NUMBER, 1);
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", Token.NUMBER, 1);

    controlFlowGraph.addEdge(entryNode, ifNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode, assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, exitNode, new DiGraphEdge());

    jsScope = new Scope();
    jsScope.declareLocal("x");

    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testEmptyGraph() {
    assertNull(maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode()));
  }

  @Test
  public void testSimpleAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NAME, "y"));
    controlFlowGraph.addNode(assignmentNode);
    controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, controlFlowGraph.getEntryNode(), new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());
    assertTrue(uses.containsKey(jsScope.getVar("x")));
    assertSame(assignmentNode, uses.get(jsScope.getVar("x")).get(0));
  }

  @Test
  public void testAssignmentWithCondition() {
    Node ifNode = new Node(Token.IF, Token.BOOLEAN, true);
    Node assignmentNode1 = new Node(Token.ASSIGN, Token.NAME, "x", Token.NUMBER, 1);
    Node assignmentNode2 = new Node(Token.ASSIGN, Token.NAME, "x", Token.NUMBER, 2);

    controlFlowGraph.addNode(ifNode);
    controlFlowGraph.addNode(assignmentNode1);
    controlFlowGraph.addNode(assignmentNode2);

    controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), ifNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode, assignmentNode1, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode1, ifNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode, assignmentNode2, new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertTrue(uses.containsKey(jsScope.getVar("x")));
    assertSame(assignmentNode1, uses.get(jsScope.getVar("x")).get(0));
    assertSame(assignmentNode2, uses.get(jsScope.getVar("x")).get(1));
  }

  @Test
  public void testVariableUseAfterAssignment() {
    Node variableNode = new Node(Token.NAME, "x");
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NUMBER, 1));
    Node useNode = new Node(Token.NAME, "x");

    controlFlowGraph.addNode(variableNode);
    controlFlowGraph.addNode(assignmentNode);
    controlFlowGraph.addNode(useNode);

    controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, useNode, new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertTrue(uses.containsKey(jsScope.getVar("x")));
    assertSame(useNode, uses.get(jsScope.getVar("x")).get(0));
  }

  @Test
  public void testVariableUseBeforeAssignment() {
    Node variableNode = new Node(Token.NAME, "x");
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NUMBER, 1));
    Node useNode = new Node(Token.NAME, "x");

    controlFlowGraph.addNode(variableNode);
    controlFlowGraph.addNode(useNode);
    controlFlowGraph.addNode(assignmentNode);

    controlFlowGraph.addEdge(useNode, assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, controlFlowGraph.getEntryNode(), new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertFalse(uses.containsKey(jsScope.getVar("x")));
  }

  @Test
  public void testVariableUseInConditional() {
    Node ifNode = new Node(Token.IF, Token.BOOLEAN, true);
    Node variableNode = new Node(Token.NAME, "x");
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NUMBER, 1));
    Node useNode = new Node(Token.NAME, "x");

    controlFlowGraph.addNode(ifNode);
    controlFlowGraph.addNode(variableNode);
    controlFlowGraph.addNode(useNode);
    controlFlowGraph.addNode(assignmentNode);

    controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), ifNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode, useNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode, assignmentNode, new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertTrue(uses.containsKey(jsScope.getVar("x")));
    assertSame(variableNode, uses.get(jsScope.getVar("x")).get(0));
  }

  @Test
  public void testEscapedVariable() {
    Node escapedVariableNode = new Node(Token.VAR, null, "arguments", null);
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NAME, "arguments[0]"));
    Node useNode = new Node(Token.NAME, "x");

    controlFlowGraph.addNode(escapedVariableNode);
    controlFlowGraph.addNode(assignmentNode);
    controlFlowGraph.addNode(useNode);

    controlFlowGraph.addEdge(escapedVariableNode, assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, useNode, new DiGraphEdge());

    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertFalse(uses.containsKey(jsScope.getVar("x")));
  }

  @Test
  public void testVariableUseInLoop() {
    Node forNode = new Node(Token.FOR, null, null, null, null, null, null, null, null);
    Node variableNode = new Node(Token.NAME, "i");
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "i", new Node(Token.NUMBER, 1));
    Node useNode = new Node(Token.NAME, "i");

    controlFlowGraph.addNode(forNode);
    controlFlowGraph.addNode(variableNode);
    controlFlowGraph.addNode(useNode);
    controlFlowGraph.addNode(assignmentNode);

    controlFlowGraph.addEdge(forNode, variableNode, new DiGraphEdge());
    controlFlowGraph.addEdge(variableNode, useNode, new DiGraphEdge());
    controlFlowGraph.addEdge(useNode, assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, forNode, new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("i", controlFlowGraph.getEntryNode());

    assertTrue(uses.containsKey(jsScope.getVar("i")));
    assertSame(variableNode, uses.get(jsScope.getVar("i")).get(0));
  }

  @Test
  public void testVariableUseInNestedConditional() {
    Node ifNode1 = new Node(Token.IF, Token.BOOLEAN, true);
    Node ifNode2 = new Node(Token.IF, Token.BOOLEAN, true);
    Node variableNode = new Node(Token.NAME, "x");
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, "x", new Node(Token.NUMBER, 1));
    Node useNode = new Node(Token.NAME, "x");

    controlFlowGraph.addNode(ifNode1);
    controlFlowGraph.addNode(ifNode2);
    controlFlowGraph.addNode(variableNode);
    controlFlowGraph.addNode(useNode);
    controlFlowGraph.addNode(assignmentNode);

    controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), ifNode1, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode1, ifNode2, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode2, useNode, new DiGraphEdge());
    controlFlowGraph.addEdge(ifNode2, assignmentNode, new DiGraphEdge());
    controlFlowGraph.addEdge(assignmentNode, controlFlowGraph.getEntryNode(), new DiGraphEdge());

    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertTrue(uses.containsKey(jsScope.getVar("x")));
    assertSame(variableNode, uses.get(jsScope.getVar("x")).get(0));
  }
}