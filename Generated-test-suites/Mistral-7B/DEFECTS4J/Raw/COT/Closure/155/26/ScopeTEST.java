package com.google.javascript.jscomp; // Make sure to import the necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeConstruction() {
    // Test construction with a parent Scope and rootNode
    Node rootNode = mock(Node.class);
    Scope parent = mock(Scope.class);
    Scope sut = new Scope(parent, rootNode);
    assertEquals(parent, sut.getParent());
    assertEquals(rootNode, sut.getRootNode());

    // Test construction of a global Scope
    Node rootNodeGlobal = mock(Node.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Scope sutGlobal = new Scope(rootNodeGlobal, compiler);
    assertNull(sutGlobal.getParent());
    assertEquals(rootNodeGlobal, sutGlobal.getRootNode());

    // Test construction of an empty Scope
    Node rootNodeEmpty = mock(Node.class);
    ObjectType thisType = mock(ObjectType.class);
    Scope sutEmpty = new Scope(rootNodeEmpty, thisType);
    assertNull(sutEmpty.getParent());
    assertEquals(rootNodeEmpty, sutEmpty.getRootNode());
    assertTrue(sutEmpty.isBottom());
  }

  @Test
  public void testDeclare() {
    Node rootNode = mock(Node.class);
    Scope parent = mock(Scope.class);
    ObjectType thisType = mock(ObjectType.class);
    CompilerInput input = mock(CompilerInput.class);
    Scope sut = new Scope(parent, rootNode);

    String name = "testVar";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    Var expectedVar = mock(Var.class);

    when(nameNode.getName()).thenReturn(name);
    when(input.getName()).thenReturn("testInput.js");

    sut.declare(name, nameNode, type, input, false);

    // Basic check
    Var actualVar = sut.getVar(name);
    assertEquals(expectedVar, actualVar);

    // Check the properties
    assertEquals(name, actualVar.getName());
    assertEquals(nameNode, actualVar.getNameNode());
    assertEquals(type, actualVar.getType());
    assertEquals(input, actualVar.getInput());
    assertFalse(actualVar.isDefine());
    assertEquals(sut, actualVar.getScope());
    assertEquals(0, actualVar.getIndex());
    assertFalse(actualVar.isTypeInferred());
    assertFalse(actualVar.isNoShadow());

    // Check the hashCode and equals
    Var var2 = new Var("testVar2", null, null, null, null, 0, null, false, null);
    assertEquals(actualVar.hashCode(), var2.hashCode());
    assertTrue(actualVar.equals(actualVar));
    assertFalse(actualVar.equals(var2));

    // Test the case when the variable is already declared
    sut.declare(name, nameNode, type, input, false);
    actualVar = sut.getVar(name);
    assertEquals(expectedVar, actualVar);
  }

  @Test
  public void testDeclareWithDefine() {
    Node rootNode = mock(Node.class);
    Scope parent = mock(Scope.class);
    ObjectType thisType = mock(ObjectType.class);
    CompilerInput input = mock(CompilerInput.class);
    Scope sut = new Scope(parent, rootNode);

    String name = "testVar";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    Var expectedVar = mock(Var.class);

    when(nameNode.getName()).thenReturn(name);
    when(input.getName()).thenReturn("testInput.js");
    when(input.isExtern()).thenReturn(false);

    sut.declare(name, nameNode, type, input, true);

    // Basic check
    Var actualVar = sut.getVar(name);
    assertEquals(expectedVar, actualVar);

    // Check the properties
    assertEquals(name, actualVar.getName());
    assertEquals(nameNode, actualVar.getNameNode());
    assertEquals(type, actualVar.getType());
    assertEquals(input, actualVar.getInput());
    assertTrue(actualVar.isDefine());
    assertEquals(sut, actualVar.getScope());
    assertEquals(0, actualVar.getIndex());
    assertFalse(actualVar.isTypeInferred());
    assertFalse(actualVar.isNoShadow());
  }

  @Test
  public void testGetVar() {
    String name = "testVar";
    Var var = new Var("testVar", null, null, null, null, 0, null, false, null);
    Scope scope = mock(Scope.class);

    when(scope.getVar(name)).thenReturn(var);
    Var actualVar = scope.getVar(name);
    assertEquals(var, actualVar);
  }

  @Test
  public void testGetVarNull() {
    String name = "testVar";
    Scope scope = mock(Scope.class);

    when(scope.getVar(name)).thenReturn(null);
    Var actualVar = scope.getVar(name);
    assertNull(actualVar);
  }

  @Test
  public void testGetVarParent() {
    String name = "testVar";
    Var var = new Var("testVar", null, null, null, null, 0, null, false, null);
    Scope parentScope = mock(Scope.class);

    when(parentScope.getVar(name)).thenReturn(var);
    Scope scope = new Scope(parentScope, null);
    Var actualVar = scope.getVar(name);
    assertEquals(var, actualVar);
  }

  @Test
  public void testGetVarParentNull() {
    String name = "testVar";
    Scope scope = new Scope(null, null);
    Var actualVar = scope.getVar(name);
    assertNull(actualVar);
  }

  @Test
  public void testIsDeclared() {
    String name = "testVar";
    Scope scope = mock(Scope.class);
    Var var = new Var("testVar", null, null, null, null, 0, null, false, null);
    when(scope.isDeclared(name, true)).thenReturn(true);

    assertTrue(scope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, true));
    assertFalse(scope.isDeclared("testVar2", false));
    assertFalse(scope.isDeclared("testVar2", true));
  }

  @Test
  public void testIsDeclaredParent() {
    String name = "testVar";
    Scope parentScope = mock(Scope.class);
    Var var = new Var("testVar", null, null, null, null, 0, null, false, null);
    when(parentScope.isDeclared(name, true)).thenReturn(true);
    Scope scope = new Scope(parentScope, null);

    assertTrue(scope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, true));
    assertFalse(scope.isDeclared("testVar2", false));
    assertFalse(scope.isDeclared("testVar2", true));
  }

  @Test
  public void testGetVars() {
    String name1 = "testVar1";
    String name2 = "testVar2";
    Var var1 = new Var(name1, null, null, null, null, 0, null, false, null);
    Var var2 = new Var(name2, null, null, null, null, 0, null, false, null);

    Map<String, Var> vars = new LinkedHashMap<>();
    vars.put(name1, var1);
    vars.put(name2, var2);

    Scope scope = new Scope(null, null) {
      @Override
      public Iterator<Var> getVars() {
        return vars.values().iterator();
      }
    };

    Iterator<Var> actualVars = scope.getVars();
    Iterator<Var> expectedVars = Arrays.asList(var1, var2).iterator();

    while (expectedVars.hasNext()) {
      Var expectedVar = expectedVars.next();
      Var actualVar = actualVars.next();
      assertEquals(expectedVar, actualVar);
    }
  }

  @Test
  public void testGetVarCount() {
    String name1 = "testVar1";
    String name2 = "testVar2";
    Var var1 = new Var(name1, null, null, null, null, 0, null, false, null);
    Var var2 = new Var(name2, null, null, null, null, 0, null, false, null);

    Map<String, Var> vars = new LinkedHashMap<>();
    vars.put(name1, var1);
    vars.put(name2, var2);

    Scope scope = new Scope(null, null) {
      @Override
      public Iterator<Var> getVars() {
        return vars.values().iterator();
      }
    };

    int expectedVarCount = 2;
    int actualVarCount = scope.getVarCount();

    assertEquals(expectedVarCount, actualVarCount);
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(null, null);
    assertTrue(globalScope.isGlobal());

    Scope localScope = new Scope(globalScope, null);
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, null);
    assertFalse(globalScope.isLocal());

    Scope localScope = new Scope(globalScope, null);
    assertTrue(localScope.isLocal());
  }
}