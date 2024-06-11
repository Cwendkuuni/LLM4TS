package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class MustBeReachingVariableDefTest {
  @Test
  public void testMustDefJoin() {
    Scope scope = new Scope();
    Scope subScope = new Scope(scope);

    MustDef join1 = new MustDef(ImmutableSet.of(scope.getVar("a")));
    MustDef join2 = new MustDef(ImmutableSet.of(subScope.getVar("a")));
    MustDef join3 = new MustDefJoin().apply(join1, join2);

    assertEquals(join1, join3);
  }

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope();
    Node rootNode = new Node(Token.BLOCK);

    MustDef def = new MustDef();

    addToDefIfLocal("a", rootNode, null, def);
    assertEquals(1, def.reachingDef.size());

    addToDefIfLocal("a", rootNode, null, def);
    assertEquals(1, def.reachingDef.size());

    addToDefIfLocal("b", rootNode, null, def);
    assertEquals(2, def.reachingDef.size());
  }

  @Test
  public void testComputeMustDef() {
    Scope scope = new Scope();
    Node rootNode = new Node(Token.BLOCK);

    MustDef def = new MustDef();

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setFirstChild(new Node(Token.NAME, "a"));
    assignNode.setLastChild(new Node(Token.NAME, "b"));
    rootNode.setFirstChild(assignNode);

    computeMustDef(rootNode, rootNode, def, false);

    assertEquals(1, def.reachingDef.size());
    assertEquals(rootNode, def.reachingDef.get(scope.getVar("a")).node);
  }

  @Test
  public void testGetDef() {
    Scope scope = new Scope();
    Node rootNode = new Node(Token.BLOCK);

    MustDef def = new MustDef();

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.setFirstChild(new Node(Token.NAME, "a"));
    assignNode.setLastChild(new Node(Token.NAME, "b"));
    rootNode.setFirstChild(assignNode);

    Node useNode = assignNode;

    Node result = getDef("a", useNode);

    assertEquals(assignNode, result);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);

    MustDef def = new MustDef();

    Node outerAssignNode = new Node(Token.ASSIGN);
    outerAssignNode.setFirstChild(new Node(Token.NAME, "outer"));
    outerAssignNode.setLastChild(new Node(Token.NAME, "inner"));
    outerScope.setRootNode(outerAssignNode);

    Node innerAssignNode = new Node(Token.ASSIGN);
    innerAssignNode.setFirstChild(new Node(Token.NAME, "inner"));
    innerAssignNode.setLastChild(new Node(Token.NAME, "outer"));
    innerScope.setRootNode(innerAssignNode);

    Node useNode = innerAssignNode;

    boolean result = dependsOnOuterScopeVars("outer", useNode);

    assertTrue(result);

    result = dependsOnOuterScopeVars("inner", useNode);

    assertFalse(result);
  }

  @Test
  public void testEscapeParameters() {
    Scope scope = new Scope();

    MustDef def = new MustDef();
    escapeParameters(def);

    assertEquals(0, def.reachingDef.size());
  }
}