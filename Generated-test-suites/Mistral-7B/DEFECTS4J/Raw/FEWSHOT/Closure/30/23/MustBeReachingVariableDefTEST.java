package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope();
    String var1 = "var1";
    String var2 = "var2";
    String arg = "args";

    MustBeReachingVariableDef mustDef = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new NodeFactory()), scope, null);

    Node var1Node = new Node(Token.VAR, "var1");
    Node var1Assignment = new Node(Token.ASSIGN, var1Node, new Node(Token.NUM, 0));

    Node argNode = new Node("arguments", new NodeFactory());
    Node argAssignment = new Node(Token.ASSIGN, argNode, new Node(Token.NUM, 1));

    mustDef.addToDefIfLocal(var1, null, null, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var1)));

    mustDef.addToDefIfLocal(var1, null, var1Assignment, mustDef.createEntryLattice());
    assertEquals(var1Node, mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var1)).node);

    mustDef.addToDefIfLocal(var1, argNode, null, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var1)));

    mustDef.addToDefIfLocal(var1, argNode, argAssignment, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var1)));

    mustDef.addToDefIfLocal(var2, null, null, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var2)));

    mustDef.addToDefIfLocal(var2, var1Node, null, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var2)));

    mustDef.addToDefIfLocal(var2, var1Node, var1Assignment, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(var2)));

    mustDef.addToDefIfLocal(arg, null, null, mustDef.createEntryLattice());
    assertNull(mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(arg)));

    mustDef.addToDefIfLocal(arg, var1Node, null, mustDef.createEntryLattice());
    assertEquals(var1Node, mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(arg)).node);

    mustDef.addToDefIfLocal(arg, var1Node, argAssignment, mustDef.createEntryLattice());
    assertEquals(var1Node, mustDef.createEntryLattice().reachingDef.get(jsScope.getVar(arg)).node);
  }

  @Test
  public void testGetDef() {
    Scope scope = new Scope();
    String var1 = "var1";
    String var2 = "var2";
    String arg = "args";

    MustBeReachingVariableDef mustDef = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new NodeFactory()), scope, null);

    Node var1Node = new Node(Token.VAR, "var1");
    Node var1Assignment = new Node(Token.ASSIGN, var1Node, new Node(Token.NUM, 0));

    Node argNode = new Node("arguments", new NodeFactory());
    Node argAssignment = new Node(Token.ASSIGN, argNode, new Node(Token.NUM, 1));

    Node useNode = new Node(Token.ASSIGN, var1Node, argNode);

    mustDef.addToDefIfLocal(var1, null, var1Assignment, mustDef.createEntryLattice());
    mustDef.addToDefIfLocal(arg, null, argAssignment, mustDef.createEntryLattice());

    Node def = mustDef.getDef(var1, useNode);
    assertEquals(var1Node, def);

    def = mustDef.getDef(var2, useNode);
    assertNull(def);

    def = mustDef.getDef(arg, useNode);
    assertEquals(argNode, def);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Scope scope = new Scope();
    String var1 = "var1";
    String var2 = "outerScopeVar";

    MustBeReachingVariableDef mustDef = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new NodeFactory()), scope, null);

    Node var1Node = new Node(Token.VAR, "var1");
    Node var1Assignment = new Node(Token.ASSIGN, var1Node, new Node(Token.NUM, 0));

    Node outerScopeVarNode = new Node("outerScopeVar", new NodeFactory());

    mustDef.addToDefIfLocal(var1, null, var1Assignment, mustDef.createEntryLattice());
    mustDef.addToDefIfLocal(var2, null, outerScopeVarNode, mustDef.createEntryLattice());

    boolean result = mustDef.dependsOnOuterScopeVars(var1, var1Node);
    assertFalse(result);

    result = mustDef.dependsOnOuterScopeVars(var1, outerScopeVarNode);
    assertFalse(result);

    result = mustDef.dependsOnOuterScopeVars(var2, var1Node);
    assertFalse(result);

    result = mustDef.dependsOnOuterScopeVars(var2, outerScopeVarNode);
    assertTrue(result);
  }
}