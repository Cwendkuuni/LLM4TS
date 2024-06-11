package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDefJoin() {
    Map<Var, Definition> map1 = Maps.newHashMap();
    map1.put(Var.create("a", 0), new Definition(Node.newIntLiteral(1)));
    map1.put(Var.create("b", 0), new Definition(Node.newIntLiteral(2)));

    Map<Var, Definition> map2 = Maps.newHashMap();
    map2.put(Var.create("a", 0), new Definition(Node.newIntLiteral(3)));
    map2.put(Var.create("c", 0), new Definition(Node.newIntLiteral(4)));

    MustDef mustDef1 = new MustDef(map1.entrySet().iterator());
    MustDef mustDef2 = new MustDef(map2.entrySet().iterator());

    MustDef result = new MustDefJoin().apply(mustDef1, mustDef2);

    Map<Var, Definition> expected = Maps.newHashMap();
    expected.put(Var.create("a", 0), new Definition(Node.newIntLiteral(1)));
    expected.put(Var.create("b", 0), new Definition(Node.newIntLiteral(2)));
    expected.put(Var.create("c", 0), new Definition(Node.newIntLiteral(4)));

    assertEquals(expected, result.reachingDef);
  }

  @Test
  public void testFlowThrough() {
    Scope scope = new Scope(null);
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    Node entryNode = Node.newBlock("entry");
    Node assignmentNode = Node.newAssignment("x", Node.newIntLiteral(1));

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode);
    cfg.addEdge(entryNode, assignmentNode);

    MustDef initialState = new MustDef();
    MustDef mustDef = new MustDef(initialState, entryNode, cfg);

    Node nodeToTest = Node.newName("y");

    MustDef output = mustDef.flowThrough(nodeToTest, mustDef);

    assertNull(output.reachingDef.get(Var.create("x", 0)));
    assertNull(output.reachingDef.get(Var.create("y", 0)));
  }

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope(null);
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    Node entryNode = Node.newBlock("entry");
    Node assignmentNode1 = Node.newAssignment("x", Node.newName("y"));
    Node assignmentNode2 = Node.newAssignment("x", Node.newIntLiteral(1));
    Node useNode = Node.newName("x");

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode1);
    cfg.addNode(assignmentNode2);
    cfg.addNode(useNode);
    cfg.addEdge(entryNode, assignmentNode1);
    cfg.addEdge(entryNode, assignmentNode2);
    cfg.addEdge(assignmentNode1, useNode);
    cfg.addEdge(assignmentNode2, useNode);

    MustDef initialState = new MustDef();
    MustDef mustDef = new MustBeReachingVariableDef(cfg, scope, null).createInitialEstimateLattice();

    MustDef output = mustDef.flowThrough(useNode, mustDef);

    Definition expectedDefinition1 = new Definition(assignmentNode1);
    expectedDefinition1.depends.add(Var.create("y", 0));

    Definition expectedDefinition2 = new Definition(assignmentNode2);

    Map<Var, Definition> expectedMap = Maps.newHashMap();
    expectedMap.put(Var.create("x", 0), expectedDefinition1);
    expectedMap.put(Var.create("y", 0), null);

    assertEquals(expectedMap, output.reachingDef);
  }

  @Test
  public void testGetDef() {
    Scope scope = new Scope(null);
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    Node entryNode = Node.newBlock("entry");
    Node assignmentNode1 = Node.newAssignment("x", Node.newIntLiteral(1));
    Node useNode = Node.newName("x");

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode1);
    cfg.addNode(useNode);
    cfg.addEdge(entryNode, assignmentNode1);
    cfg.addEdge(assignmentNode1, useNode);

    MustDef initialState = new MustDef();
    MustDef mustDef = new MustBeReachingVariableDef(cfg, scope, null).createInitialEstimateLattice();

    MustDef output = mustDef.flowThrough(useNode, mustDef);

    Definition expectedDefinition = new Definition(assignmentNode1);

    assertEquals(expectedDefinition.node, mustDef.getDef("x", useNode));
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Scope scope = new Scope(null);
    ControlFlowGraph cfg = new ControlFlowGraph(scope, null);
    Node entryNode = Node.newBlock("entry");
    Node assignmentNode = Node.newAssignment("x", Node.newName("y"));
    Node useNode = Node.newName("x");

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode);
    cfg.addNode(useNode);
    cfg.addEdge(entryNode, assignmentNode);
    cfg.addEdge(entryNode, useNode);

    MustDef initialState = new MustDef();
    MustDef mustDef = new MustBeReachingVariableDef(cfg, scope, null).createInitialEstimateLattice();

    assertFalse(mustDef.dependsOnOuterScopeVars("x", useNode));
    assertTrue(mustDef.dependsOnOuterScopeVars("y", useNode));
  }
}

class Definition {
  Node node;
  Set<Var> depends;

  Definition(Node node) {
    this.node = node;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Definition) {
      Definition otherDef = (Definition) other;
      return otherDef.node.equals(node);
    }
    return false;
  }
}