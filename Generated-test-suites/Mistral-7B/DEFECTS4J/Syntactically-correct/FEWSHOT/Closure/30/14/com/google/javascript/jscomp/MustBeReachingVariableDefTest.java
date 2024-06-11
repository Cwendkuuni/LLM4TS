package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDefJoin() {
    MustDef a = new MustDef();
    MustDef b = new MustDef();

    Var var1 = new Var("A1", 1, null);
    Var var2 = new Var("A2", 2, null);
    Var var3 = new Var("A3", 3, null);

    a.reachingDef.put(var1, new Definition(new Node(Token.NAME, "N1")));
    a.reachingDef.put(var2, new Definition(new Node(Token.NAME, "N2")));

    b.reachingDef.put(var1, new Definition(new Node(Token.NAME, "N3")));
    b.reachingDef.put(var3, new Definition(new Node(Token.NAME, "N4")));

    MustDef c = new MustDefJoin().apply(a, b);

    for (Entry<Var, Definition> entry : c.reachingDef.entrySet()) {
      if (entry.getKey().getName().equals("A1")) {
        Definition def = entry.getValue();
        assertEquals("N3", def.node.getToken().toString());
      } else if (entry.getKey().getName().equals("A2")) {
        assertNull(c.reachingDef.get(entry.getKey()));
      } else if (entry.getKey().getName().equals("A3")) {
        Definition def = entry.getValue();
        assertEquals("N4", def.node.getToken().toString());
      }
    }
  }

  @Test
  public void testIsForward() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, null, null);
    assertTrue(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    MustDef entryLattice = analysis.createEntryLattice();

    for (Entry<Var, Definition> entry : entryLattice.reachingDef.entrySet()) {
      Definition def = entry.getValue();
      assertEquals(jsScope.getRootNode(), def.node);
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    MustDef initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertNull(initialEstimateLattice.reachingDef.get(var1));
    assertNull(initialEstimateLattice.reachingDef.get(var2));
    assertNull(initialEstimateLattice.reachingDef.get(var3));
  }

  @Test
  public void testFlowThrough() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    MustDef input = new MustDef();
    input.reachingDef.put(var1, new Definition(new Node(Token.NAME, "N1")));
    input.reachingDef.put(var2, new Definition(new Node(Token.NAME, "N2")));
    input.reachingDef.put(var3, new Definition(new Node(Token.NAME, "N3")));

    Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "A1"),
        new Node(Token.NAME, "A2"));

    MustDef output = analysis.flowThrough(n, input);

    assertEquals(input, output);
  }

  @Test
  public void testComputeMustDef() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    Node n1 = new Node(Token.ASSIGN, new Node(Token.NAME, "A1"),
        new Node(Token.NAME, "A2"));
    Node n2 = new Node(Token.ASSIGN, new Node(Token.NAME, "A1"),
        new Node(Token.NAME, "A3"));

    analysis.computeMustDef(n1, null, new MustDef(), false);
    analysis.computeMustDef(n2, n1, new MustDef(), false);

    Definition a1Def = analysis.reachingDef.get(var1);
    assertEquals(n1, a1Def.node);

    Definition a2Def = analysis.reachingDef.get(var2);
    assertEquals(n1, a2Def.node);

    Definition a3Def = analysis.reachingDef.get(var3);
    assertNull(a3Def);
  }

  @Test
  public void testAddToDefIfLocal() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    analysis.addToDefIfLocal("A1", null, null, new MustDef());
    analysis.addToDefIfLocal("A2", null, null, new MustDef());
    analysis.addToDefIfLocal("A3", null, null, new MustDef());

    Definition a1Def = analysis.reachingDef.get(var1);
    assertEquals(jsScope.getRootNode(), a1Def.node);

    Definition a2Def = analysis.reachingDef.get(var2);
    assertEquals(jsScope.getRootNode(), a2Def.node);

    Definition a3Def = analysis.reachingDef.get(var3);
    assertNull(a3Def);
  }

  @Test
  public void testEscapeParameters() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);
    Var var4 = new Var("A4", 4, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    analysis.escapeParameters(new MustDef());

    Definition a1Def = analysis.reachingDef.get(var1);
    assertNull(a1Def);

    Definition a2Def = analysis.reachingDef.get(var2);
    assertNull(a2Def);

    Definition a3Def = analysis.reachingDef.get(var3);
    assertNull(a3Def);

    Definition a4Def = analysis.reachingDef.get(var4);
    assertNull(a4Def);
  }

  @Test
  public void testGetDef() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var var3 = new Var("A3", 3, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    analysis.addToDefIfLocal("A1", null, null, new MustDef());
    analysis.addToDefIfLocal("A2", null, new Node(Token.NAME, "N2"), new MustDef());
    analysis.addToDefIfLocal("A3", new Node(Token.NAME, "N3"), null, new MustDef());

    Node n1 = new Node(Token.ASSIGN, new Node(Token.NAME, "A1"),
        new Node(Token.NAME, "A2"));
    Node n2 = new Node(Token.ASSIGN, new Node(Token.NAME, "A2"),
        new Node(Token.NAME, "A3"));
    Node n3 = new Node(Token.ASSIGN, new Node(Token.NAME, "A3"), null);

    analysis.computeMustDef(n1, null, new MustDef(), false);
    analysis.computeMustDef(n2, n1, new MustDef(), false);
    analysis.computeMustDef(n3, n2, new MustDef(), false);

    Node useNode1 = n1;
    Node useNode2 = n2;

    Node def1 = analysis.getDef("A1", useNode1);
    assertNotNull(def1);
    assertEquals("N1", def1.getToken().toString());

    Node def2 = analysis.getDef("A2", useNode2);
    assertNotNull(def2);
    assertEquals("N3", def2.getToken().toString());

    Node def3 = analysis.getDef("A3", useNode2);
    assertNull(def3);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Scope jsScope = new Scope();
    Var var1 = new Var("A1", 1, jsScope);
    Var var2 = new Var("A2", 2, jsScope);
    Var outerVar = new Var("outer", 1, null);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);

    analysis.addToDefIfLocal("A1", null, null, new MustDef());
    analysis.addToDefIfLocal("outer", null, null, new MustDef());
    analysis.addToDefIfLocal("A2", null, null, new MustDef());

    Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "A1"),
        new Node(Token.NAME, "outer"));

    analysis.computeMustDef(n, null, new MustDef(), false);

    assertTrue(analysis.dependsOnOuterScopeVars("A1", n));
  }
}