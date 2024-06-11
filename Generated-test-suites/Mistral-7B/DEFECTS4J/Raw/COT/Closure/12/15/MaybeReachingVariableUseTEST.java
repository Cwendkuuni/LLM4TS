package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private static final Node N_1 = Node.newIntLiteral(1);
  private static final Node N_2 = Node.newIntLiteral(2);
  private static final Node N_3 = Node.newIntLiteral(3);

  private MaybeReachingVariableUse analyzer;
  private Scope jsScope;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setUp() {
    jsScope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>(new DummyCompilationUnit(), jsScope);
    analyzer = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testDummy() {
    // This test case just check that the object is initialized correctly.
    assertNotNull(analyzer);
  }

  @Test
  public void testSimpleAssignment() {
    Node root = Node.newBlock(ImmutableSet.of(
        Node.newName("x"), Node.newAssign(Node.newName("x"), N_1)
    ));

    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    Collection<Node> uses = analyzer.getUses("x", rootNode.getValue());
    assertEquals(1, uses.size());
    assertEquals(rootNode.getValue(), uses.iterator().next());
  }

  @Test
  public void testAssignmentInConditional() {
    Node root = Node.newBlock(ImmutableSet.of(
        Node.newName("x"),
        Node.newAssign(Node.newName("x"), N_1),
        Node.newIf(Node.newName("y"), Node.newAssign(Node.newName("x"), N_2), N_3),
        Node.newName("x")
    ));

    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    Collection<Node> usesWhenYIsTrue = analyzer.getUses("x", rootNode.getValue());
    assertEquals(1, usesWhenYIsTrue.size());
    assertEquals(Node.newAssign(Node.newName("x"), N_2), usesWhenYIsTrue.iterator().next());

    Collection<Node> usesWhenYIsFalse = analyzer.getUses("x", Node.newName("else"));
    assertEquals(1, usesWhenYIsFalse.size());
    assertEquals(rootNode.getValue(), usesWhenYIsFalse.iterator().next());
  }

  @Test
  public void testAssignmentInLoop() {
    Node root = Node.newBlock(ImmutableSet.of(
        Node.newName("x"),
        Node.newAssign(Node.newName("x"), N_1),
        Node.newFor(Node.newName("i"), Node.newIntLiteral(0), Node.newIntLiteral(1)),
        Node.newAssign(Node.newName("x"), Node.newAddOp(Node.newName("x"), N_1)),
        Node.newName("x")
    ));

    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    Collection<Node> uses = analyzer.getUses("x", rootNode.getValue());
    for (int i = 0; i < 5; i++) {
      assertEquals(Node.newAssign(Node.newName("x"), Node.newAddOp(Node.newName("x"), N_1)), uses.iterator().next());
      uses = analyzer.getUses("x", rootNode.getValue());
    }

    assertFalse(uses.isEmpty());
  }

  @Test
  public void testFunctionDefinition() {
    Node root = Node.newFunction(new Node("myFunction"), ImmutableSet.of(), ImmutableSet.of(
        Node.newVar("x", Node.SCOPE_VAR),
        Node.newAssign(Node.newName("x"), N_1),
        Node.newReturn(Node.newName("x"))
    ));

    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    Collection<Node> uses = analyzer.getUses("x", rootNode.getValue());
    assertEquals(1, uses.size());
    assertEquals(Node.newAssign(Node.newName("x"), N_1), uses.iterator().next());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullParameter() {
    Node root = Node.newBlock(ImmutableSet.of());
    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    analyzer.getUses(null, rootNode.getValue());
  }

  @Test
  public void testEscapeVariable() {
    Scope escapedScope = new Scope();
    Node root = Node.newBlock(ImmutableSet.of(
        Node.newVar("x", Node.SCOPE_VAR, Node.newName("e")),
        Node.newAssign(Node.newName("e"), Node.newIntLiteral(1)),
        Node.newReturn(Node.newName("x"))
    ));
    controlFlowGraph = new ControlFlowGraph<>(new DummyCompilationUnit(), escapedScope);
    MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(controlFlowGraph, escapedScope, null);

    GraphNode<Node, Branch> rootNode = controlFlowGraph.addStartNode(root, null);
    analyzer.processStart(rootNode, null);

    Collection<Node> uses = analyzer.getUses("x", rootNode.getValue());
    assertNull(uses);
  }
}