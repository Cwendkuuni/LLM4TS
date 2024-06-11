package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setUp() {
    // Setup the test environment
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    compiler = new AbstractCompiler(cfg, jsScope);
  }

  @Test
  public void testBasicFunctionality() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node node = createTestNode("a = 5;");
    MustDef mustDef = mustBeReachingVariableDef.flowThrough(node, mustBeReachingVariableDef.createEntryLattice());

    Node useNode = createTestNode("alert(a);");
    Node defNode = mustBeReachingVariableDef.getDef("a", useNode);
    assertEquals(node, defNode);
  }

  @Test
  public void testConditionalBranch() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node ifNode = createTestNode("if (x > 5) { a = 5; }");
    Node useNode = createTestNode("alert(a);");

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(ifNode, mustBeReachingVariableDef.createEntryLattice());

    Node defNode = mustBeReachingVariableDef.getDef("a", useNode);
    assertEquals(ifNode.getFirstChild(), defNode);
  }

  @Test
  public void testForLoop() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node forNode = createTestNode("for (var i = 0; i < 5; i++) { a[i] = i; }");
    Node useNode = createTestNode("alert(a[2]);");

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(forNode, mustBeReachingVariableDef.createEntryLattice());

    Node defNode = mustBeReachingVariableDef.getDef("a[2]", useNode);
    assertEquals(forNode.getChild(1), defNode);
  }

  @Test
  public void testEscapedVariable() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Scope escapedScope = new Scope();
    Var escapedVar = escapedScope.getVar("escapedVar");
    escapedScope.putVar(escapedVar.getName(), new NodeName(escapedVar.getName()));

    Node functionNode = createTestNode("function foo() { var escapedVar = arguments[0]; }");
    Node useNode = createTestNode("alert(escapedVar);");

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    mustBeReachingVariableDef.escaped.addAll(Sets.newHashSet(escapedVar));

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(functionNode, mustBeReachingVariableDef.createEntryLattice());

    Node defNode = mustBeReachingVariableDef.getDef("escapedVar", useNode);
    assertNull(defNode);
  }

  @Test
  public void testVariableDependency() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node node1 = createTestNode("var x = 5; var y = x;");
    Node useNode = createTestNode("alert(y);");

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(node1, mustBeReachingVariableDef.createEntryLattice());

    Definition def1 = mustDef.reachingDef.get(jsScope.getVar("x"));
    Definition def2 = mustDef.reachingDef.get(jsScope.getVar("y"));

    Set<Var> expectedDependencies = Sets.newHashSet(jsScope.getVar("x"));
    assertEquals(expectedDependencies, def1.depends);

    Set<Var> expectedDependedVariables = Sets.newHashSet(jsScope.getVar("y"));
    assertEquals(expectedDependedVariables, def2.depends);
  }

  private Node createTestNode(String nodeCode) {
    Node node = compiler.parseSingleStatement(nodeCode);
    cfg.addNode(node);
    return node;
  }
}