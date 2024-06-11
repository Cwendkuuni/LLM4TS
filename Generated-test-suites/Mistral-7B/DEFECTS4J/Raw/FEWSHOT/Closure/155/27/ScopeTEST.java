package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {
  @Test
  public void testScopeCreation() {
    Scope scope = new Scope(null, null);
    assertNull(scope.getParent());
    assertNull(scope.getRootNode());
    assertSame(ObjectType.NULL, scope.getTypeOfThis());
    assertTrue(scope.isBottom());
    assertEquals(0, scope.getDepth());

    Scope parentScope = new Scope(null, null);
    Scope scope1 = new Scope(parentScope, null);
    assertSame(parentScope, scope1.getParent());
    assertNull(scope1.getRootNode());
    assertSame(ObjectType.NULL, scope1.getTypeOfThis());
    assertFalse(scope1.isBottom());
    assertEquals(1, scope1.getDepth());
  }

  @Test
  public void testDeclaringVariables() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "a";
    String name2 = "b";

    scope.declare(name1, null, JSType.INT, null);
    Var var1 = scope.getVar(name1);
    assertSame(name1, var1.getName());
    assertNull(var1.getType());

    scope.declare(name1, null, JSType.STRING, null);
    Var var1b = scope.getVar(name1);
    assertSame(name1, var1b.getName());
    assertSame(JSType.STRING, var1b.getType());

    scope.declare(name2, null, JSType.INT, null);
    Var var2 = scope.getVar(name2);
    assertSame(name2, var2.getName());
    assertNull(var2.getType());

    Var var1c = scope.getVar(name1);
    assertSame(name1, var1c.getName());
    assertSame(JSType.STRING, var1c.getType());

    Var var2b = scope.getVar(name2);
    assertSame(name2, var2b.getName());
    assertNull(var2b.getType());

    assertEquals(2, scope.getVarCount());

    scope = new Scope(parentScope, null);
    assertNull(scope.getVar(name1));
    assertNull(scope.getVar(name2));
  }

  @Test
  public void testGettingVariables() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "a";
    String name2 = "b";

    Var var1 = scope.declare(name1, null, JSType.INT, null);
    Var var2 = scope.declare(name2, null, JSType.STRING, null);

    Var getVar1 = scope.getVar(name1);
    assertSame(var1, getVar1);

    Var getVar2 = scope.getVar(name2);
    assertSame(var2, getVar2);

    Var getVar1b = scope.getVar(name1);
    assertSame(var1, getVar1b);

    Var getVar2b = scope.getVar(name2);
    assertSame(var2, getVar2b);

    Var getNonExistentVar = scope.getVar("c");
    assertNull(getNonExistentVar);
  }

  @Test
  public void testGettingVariablesFromParentScope() {
    Scope parentScope = new Scope(null, null);
    String name1 = "a";
    Var var1 = parentScope.declare(name1, null, JSType.INT, null);

    Scope scope = new Scope(parentScope, null);
    Var getVar1 = scope.getVar(name1);
    assertSame(var1, getVar1);
  }

  @Test
  public void testIsDeclared() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "a";
    String name2 = "b";

    scope.declare(name1, null, JSType.INT, null);
    scope.declare(name2, null, JSType.STRING, null);

    assertTrue(scope.isDeclared(name1, false));
    assertTrue(scope.isDeclared(name2, false));
    assertTrue(scope.isDeclared(name1, true));
    assertTrue(scope.isDeclared(name2, true));
    assertFalse(scope.isDeclared("c", false));
    assertFalse(scope.isDeclared("c", true));
  }

  @Test
  public void testIterator() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "a";
    String name2 = "b";
    String name3 = "c";

    scope.declare(name1, null, JSType.INT, null);
    scope.declare(name2, null, JSType.STRING, null);
    scope.declare(name3, null, JSType.BOOLEAN, null);

    Iterator<Var> it = scope.getVars().iterator();

    Var var1 = it.next();
    assertSame(name1, var1.getName());
    assertNull(var1.getType());

    Var var2 = it.next();
    assertSame(name2, var2.getName());
    assertNull(var2.getType());

    Var var3 = it.next();
    assertSame(name3, var3.getName());
    assertNull(var3.getType());

    assertFalse(it.hasNext());
  }

  @Test
  public void testGetTypeOfThis() {
    Scope scope = new Scope(null, null);
    assertSame(ObjectType.NULL, scope.getTypeOfThis());

    Scope parentScope = new Scope(null, null);
    scope = new Scope(parentScope, null);
    FunctionType functionType = new FunctionType(JSType.VOID, new ObjectType[0], null);
    scope.thisType = functionType;
    assertSame(functionType, scope.getTypeOfThis());
  }

  @Test
  public void testJoin() {
    Scope parentScope = new Scope(null, null);
    Scope scope1 = new Scope(parentScope, null);
    scope1.declare("a", null, JSType.INT, null);

    Scope scope2 = new Scope(parentScope, null);
    scope2.declare("b", null, JSType.STRING, null);
    scope2.declare("a", null, JSType.BOOLEAN, null);

    Map<String, Var> joinMap = new LinkedHashMap<>();
    joinMap.putAll(scope1.getVars());
    joinMap.putAll(scope2.getVars());

    Scope joinedScope = new Scope(parentScope, null);
    joinedScope.vars = joinMap;

    Var varA1 = scope1.getVar("a");
    Var varA2 = joinedScope.getVar("a");
    assertSame(varA1, varA2);

    Var varB = scope2.getVar("b");
    Var varBJoined = joinedScope.getVar("b");
    assertSame(varB, varBJoined);

    Var varA3 = joinedScope.getVar("a");
    assertSame(varA1, varA3);

    Var varANew = joinedScope.declare("a", null, JSType.DOUBLE, null);
    assertSame(varA1, varANew);
  }

  @Test
  public void testIsBottom() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isBottom());

    Scope bottomScope = new Scope(null, null);
    bottomScope.isBottom = true;
    assertTrue(bottomScope.isBottom());
  }
}