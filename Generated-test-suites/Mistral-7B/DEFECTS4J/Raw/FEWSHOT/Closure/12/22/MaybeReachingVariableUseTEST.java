package org.apache.commons.cli; // Change the package name if necessary

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Create a mock ControlFlowGraph and Scope for testing.
    controlFlowGraph = new ControlFlowGraph<Node>() {};
    jsScope = new Scope() {};
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testFlowThroughSimpleAssignment() {
    Node n1 = Node.newNode(1, Token.ASSIGN, "a", null, null, null);
    Node n2 = Node.newNode(2, Token.NAME, "a", null, null, null);

    maybeReachingVariableUse.flowThrough(n1, maybeReachingVariableUse.createEntryLattice());
    ReachingUses result = maybeReachingVariableUse.flowThrough(n2, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result.mayUseMap.get(jsScope.getVar("a")));
  }

  @Test
  public void testFlowThroughAssignmentWithCondition() {
    Node ifNode = Node.newNode(1, Token.IF, null, null, null, null);
    Node condition = Node.newNode(2, Token.NAME, "a", null, null, null);
    Node assignmentNode = Node.newNode(3, Token.ASSIGN, "a", null, null, null);

    maybeReachingVariableUse.flowThrough(condition, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(ifNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(assignmentNode, maybeReachingVariableUse.createEntryLattice());

    ReachingUses result = maybeReachingVariableUse.flowThrough(ifNode, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result.mayUseMap.get(jsScope.getVar("a")));
  }

  @Test
  public void testFlowThroughNestedAssignments() {
    Node n1 = Node.newNode(1, Token.ASSIGN, "a", null, null, null);
    Node n2 = Node.newNode(2, Token.ASSIGN, "b", null, null, null);
    Node n3 = Node.newNode(3, Token.ASSIGN, "a", null, null, null);
    Node n4 = Node.newNode(4, Token.ASSIGN, "b", null, null, null);

    maybeReachingVariableUse.flowThrough(n1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(n3, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(n2, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(n4, maybeReachingVariableUse.createEntryLattice());

    ReachingUses result = maybeReachingVariableUse.flowThrough(n3, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result.mayUseMap.get(jsScope.getVar("a")));

    result = maybeReachingVariableUse.flowThrough(n4, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result.mayUseMap.containsKey(jsScope.getVar("b")));
    assertNotNull(result.mayUseMap.get(jsScope.getVar("b")));
  }

  @Test
  public void testFlowThroughMultipleAssignmentsWithCondition() {
    Node ifNode = Node.newNode(1, Token.IF, null, null, null, null);
    Node condition1 = Node.newNode(2, Token.NAME, "a", null, null, null);
    Node condition2 = Node.newNode(3, Token.NAME, "b", null, null, null);
    Node assignmentNode1 = Node.newNode(4, Token.ASSIGN, "a", null, null, null);
    Node assignmentNode2 = Node.newNode(5, Token.ASSIGN, "b", null, null, null);

    maybeReachingVariableUse.flowThrough(condition1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(ifNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(condition2, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(assignmentNode1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(assignmentNode2, maybeReachingVariableUse.createEntryLattice());

    ReachingUses result1 = maybeReachingVariableUse.flowThrough(ifNode, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result1.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result1.mayUseMap.get(jsScope.getVar("a")));

    result1 = maybeReachingVariableUse.flowThrough(condition2, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result1.mayUseMap.containsKey(jsScope.getVar("b")));
    assertNotNull(result1.mayUseMap.get(jsScope.getVar("b")));

    ReachingUses result2 = maybeReachingVariableUse.flowThrough(assignmentNode1, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result2.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result2.mayUseMap.get(jsScope.getVar("a")));

    result2 = maybeReachingVariableUse.flowThrough(assignmentNode2, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result2.mayUseMap.containsKey(jsScope.getVar("b")));
    assertNotNull(result2.mayUseMap.get(jsScope.getVar("b")));
  }

  @Test
  public void testFlowThroughConditionalAssignment() {
    Node ifNode = Node.newNode(1, Token.IF, null, null, null, null);
    Node condition = Node.newNode(2, Token.NAME, "a", null, null, null);
    Node assignmentNode1 = Node.newNode(3, Token.ASSIGN, "a", null, null, null);
    Node assignmentNode2 = Node.newNode(4, Token.ASSIGN, "b", null, null, null);

    maybeReachingVariableUse.flowThrough(condition, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(ifNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(assignmentNode1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(assignmentNode2, maybeReachingVariableUse.createEntryLattice());

    ReachingUses result1 = maybeReachingVariableUse.flowThrough(assignmentNode1, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result1.mayUseMap.containsKey(jsScope.getVar("a")));
    assertNotNull(result1.mayUseMap.get(jsScope.getVar("a")));

    result1 = maybeReachingVariableUse.flowThrough(condition, maybeReachingVariableUse.createEntryLattice());

    assertFalse(result1.mayUseMap.containsKey(jsScope.getVar("a")));

    ReachingUses result2 = maybeReachingVariableUse.flowThrough(assignmentNode2, maybeReachingVariableUse.createEntryLattice());

    assertTrue(result2.mayUseMap.containsKey(jsScope.getVar("b")));
    assertNotNull(result2.mayUseMap.get(jsScope.getVar("b")));
  }

  @Test
  public void testGetUses() {
    Node n1 = Node.newNode(1, Token.ASSIGN, "a", null, null, null);
    Node n2 = Node.newNode(2, Token.NAME, "a", null, null, null);

    maybeReachingVariableUse.flowThrough(n1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(n2, maybeReachingVariableUse.createEntryLattice());

    Set<Node> uses = new HashSet<>();
    uses.add(n2);

    Collection<Node> result = maybeReachingVariableUse.getUses("a", n1);

    assertTrue(result.containsAll(uses));
  }
}