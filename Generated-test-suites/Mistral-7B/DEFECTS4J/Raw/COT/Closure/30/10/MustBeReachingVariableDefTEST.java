package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef mustBeReachingVariableDef;
  private Scope scope;

  @Before
  public void setup() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node rootNode = Node.createTree("function foo() { let x = 1; let y = 1; }");
    scope = new Scope(rootNode);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, scope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node useNode = Node.createTree("let z = x;");
    Node defNode = mustBeReachingVariableDef.getDef("x", useNode);
    assertEquals(mustBeReachingVariableDef.getCfg().getEntryNode(), defNode);
  }

  @Test
  public void testComplexAssignment() {
    Node useNode = Node.createTree("let z = y + x;");
    Node defNode = mustBeReachingVariableDef.getDef("x", useNode);
    assertEquals(Node.createTree("function foo() { let x = 1; let y = 1; }"), defNode);
    defNode = mustBeReachingVariableDef.getDef("y", useNode);
    assertEquals(mustBeReachingVariableDef.getCfg().getEntryNode(), defNode);
  }

  @Test
  public void testComplexAssignmentWithEscapedVariable() {
    Node useNode = Node.createTree("let x = y; function bar() { let y = 1; } bar(); let z = x;");
    assertNull(mustBeReachingVariableDef.getDef("y", useNode));
  }

  @Test
  public void testComplexAssignmentWithFunctionParameter() {
    Node useNode = Node.createTree("function foo(x) { let y = x; } foo(1); let z = y;");
    assertNull(mustBeReachingVariableDef.getDef("x", useNode));
  }

  @Test
  public void testComplexAssignmentWithOuterScopeVariable() {
    Node rootNode = Node.createTree("let a = 1; function foo() { let x = a; let y = x; } foo(); let z = y;");
    Scope outerScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, outerScope, null);
    Node useNode = Node.createTree("let z = y;");
    assertNull(mustBeReachingVariableDef.getDef("a", useNode));
    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, scope, null);
    useNode = Node.createTree("let z = y;");
    Node defNode = mustBeReachingVariableDef.getDef("a", useNode);
    assertNull(defNode);
    defNode = mustBeReachingVariableDef.getDef("x", useNode);
    assertEquals(Node.createTree("function foo() { let x = a; let y = x; }"), defNode);
  }

  @Test
  public void testMustDefJoin() {
    Node useNode1 = Node.createTree("let x = 1; let y = x; let z = y;");
    Node defNode1 = mustBeReachingVariableDef.getDef("x", useNode1);
    assertEquals(Node.createTree("function foo() { let x = 1; }"), defNode1);

    Node useNode2 = Node.createTree("let x = 1; let y = x; let z = y; let y = 2; let w = z;");
    Node defNode2 = mustBeReachingVariableDef.getDef("w", useNode2);
    assertEquals(Node.createTree("function foo() { let x = 1; }"), defNode2);

    MustDef join = mustBeReachingVariableDef.createEntryLattice();
    join = mustBeReachingVariableDef.flowThrough(defNode1, join);
    join = mustBeReachingVariableDef.flowThrough(defNode2, join);
    assertEquals(join, mustBeReachingVariableDef.createEntryLattice());
  }
}