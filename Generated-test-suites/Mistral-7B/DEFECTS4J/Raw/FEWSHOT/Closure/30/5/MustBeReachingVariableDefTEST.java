package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node rootNode = Node.newBlock("root");
    rootNode.addChild(Node.newAssign("a", Node.newName("1"), Node.newNumber(1)));
    rootNode.addChild(Node.newAssign("x", Node.newName("a"), Node.newName("x")));
    rootNode.addChild(Node.newReturn(Node.newName("x")));

    cfg.addNode(rootNode);

    Node useNode = rootNode.getChild(2);

    Var xVar = scope.getVar("x");
    Var aVar = scope.getVar("a");

    Node xDef = analyzer.getDef("x", useNode);
    assertThat(xDef).isNotNull();
    assertThat(xDef.getString()).isEqualTo("a");

    Node aDef = analyzer.getDef("a", useNode);
    assertThat(aDef).isNotNull();
    assertThat(aDef.getString()).isEqualTo("root");

    assertNull(analyzer.getDef("y", useNode));
  }

  @Test
  public void testGetDef_dependsOnOuterScopeVars() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node rootNode = Node.newBlock("root");
    rootNode.addChild(Node.newAssign("outer", Node.newName("1"), Node.newNumber(1)));
    rootNode.addChild(Node.newAssign("x", Node.newName("outer"), Node.newName("x")));
    rootNode.addChild(Node.newReturn(Node.newName("x")));

    cfg.addNode(rootNode);

    Node useNode = rootNode.getChild(2);

    Var xVar = scope.getVar("x");
    Var outerVar = scope.getVar("outer");

    assertNull(analyzer.getDef("x", useNode));
    assertTrue(analyzer.dependsOnOuterScopeVars("x", useNode));

    Node outerDef = analyzer.getDef("outer", useNode);
    assertThat(outerDef).isNotNull();
    assertThat(outerDef.getString()).isEqualTo("root");
  }

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node rootNode = Node.newBlock("root");
    rootNode.addChild(Node.newAssign("x", Node.newName("x"), Node.newNumber(1)));
    rootNode.addChild(Node.newAssign("y", Node.newName("x"), Node.newNumber(2)));
    rootNode.addChild(Node.newAssign("x", Node.newName("x"), Node.newNumber(2)));
    rootNode.addChild(Node.newReturn(Node.newName("x")));

    cfg.addNode(rootNode);

    Node useNode = rootNode.getChild(3);

    Var xVar = scope.getVar("x");
    Var yVar = scope.getVar("y");

    analyzer.addToDefIfLocal("x", useNode, null, analyzer.createEntryLattice());
    analyzer.addToDefIfLocal("y", useNode, null, analyzer.createEntryLattice());

    Definition xDef = analyzer.createEntryLattice().reachingDef.get(xVar);
    Definition yDef = analyzer.createEntryLattice().reachingDef.get(yVar);

    assertNull(xDef);
    assertNull(yDef);

    analyzer.computeMustDef(rootNode, rootNode, analyzer.createEntryLattice(), false);

    xDef = analyzer.createEntryLattice().reachingDef.get(xVar);
    yDef = analyzer.createEntryLattice().reachingDef.get(yVar);

    Definition rootDef = rootNode.getAnnotation();

    assertThat(xDef).isNotNull();
    assertThat(xDef.node).isSameInstanceAs(rootNode);
    assertThat(xDef.depends).isEmpty();

    assertThat(yDef).isNotNull();
    assertThat(yDef.node).isSameInstanceAs(rootNode);
    assertThat(yDef.depends).containsExactly(xVar);

    Preconditions.checkState(rootDef != null);
    Definition rootDef2 = analyzer.createEntryLattice().reachingDef.get(xVar);
    assertThat(rootDef2).isSameInstanceAs(rootDef);
  }

  @Test
  public void testComputeDependence() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node rootNode = Node.newBlock("root");
    rootNode.addChild(Node.newAssign("x", Node.newName("x"), Node.newName("y")));
    rootNode.addChild(Node.newAssign("y", Node.newName("z"), Node.newNumber(1)));
    rootNode.addChild(Node.newReturn(Node.newName("x")));

    cfg.addNode(rootNode);

    Definition xDef = new Definition(rootNode);
    Definition yDef = new Definition(Node.newName("z"));

    analyzer.computeDependence(xDef, rootNode);

    assertThat(xDef.depends).containsExactly(yVar);
  }

  @Test
  public void testEscapeParameters() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null, null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node functionNode = Node.newFunction("myFunction", Arrays.asList("arg"), Node.newBlock("root"));

    Node rootNode = functionNode.getLastChild();
    rootNode.addChild(Node.newAssign("x", Node.newName("arg"), Node.newNumber(1)));
    rootNode.addChild(Node.newAssign("arg", Node.newName("x"), Node.newNumber(2)));
    rootNode.addChild(Node.newReturn(Node.newName("arg")));

    cfg.addNode(functionNode);

    MustDef entryLattice = analyzer.createEntryLattice();

    analyzer.escapeParameters(entryLattice);

    Var argVar = scope.getVar("arg");
    Definition argDef = entryLattice.reachingDef.get(argVar);

    assertNull(argDef);
  }
}