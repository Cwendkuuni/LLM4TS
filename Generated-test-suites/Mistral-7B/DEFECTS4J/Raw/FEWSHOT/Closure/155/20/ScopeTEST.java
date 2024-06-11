package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  @Test
  public void testDeclare() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, JSType.getUndefinedType(), null);
    assertEquals("x", var1.getName());
    assertEquals(null, var1.getParentNode());
    assertEquals(JSType.getUndefinedType(), var1.getType());
    assertEquals(null, var1.getScope());
    assertEquals(0, var1.getIndex());
    assertEquals(null, var1.getInput());
    assertFalse(var1.isDefine());
    assertFalse(var1.isConst());
    assertNull(var1.getInitialValue());
    assertFalse(var1.isBleedingFunction());
    assertFalse(var1.isTypeInferred());
    assertNull(var1.getJSDocInfo());
    assertEquals(0, scope.getVarCount());

    Var var2 = scope.declare("x", null, JSType.getBooleanType(), null);
    assertEquals("x", var2.getName());
    assertEquals(null, var2.getParentNode());
    assertEquals(JSType.getBooleanType(), var2.getType());
    assertEquals(scope, var2.getScope());
    assertEquals(1, var2.getIndex());
    assertEquals(null, var2.getInput());
    assertFalse(var2.isDefine());
    assertFalse(var2.isConst());
    assertNull(var2.getInitialValue());
    assertFalse(var2.isBleedingFunction());
    assertFalse(var2.isTypeInferred());
    assertNull(var2.getJSDocInfo());
    assertEquals(2, scope.getVarCount());

    Var var3 = scope.declare("x", null, JSType.getIntType(), null);
    assertEquals("x", var3.getName());
    assertEquals(null, var3.getParentNode());
    assertEquals(JSType.getIntType(), var3.getType());
    assertEquals(scope, var3.getScope());
    assertEquals(2, var3.getIndex());
    assertEquals(null, var3.getInput());
    assertFalse(var3.isDefine());
    assertFalse(var3.isConst());
    assertNull(var3.getInitialValue());
    assertFalse(var3.isBleedingFunction());
    assertFalse(var3.isTypeInferred());
    assertNull(var3.getJSDocInfo());
    assertEquals(3, scope.getVarCount());
  }

  @Test
  public void testGetVar() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, JSType.getUndefinedType(), null);
    Var var2 = scope.declare("x", null, JSType.getBooleanType(), null);
    Var var3 = scope.declare("x", null, JSType.getIntType(), null);

    Var getVar1 = scope.getVar("x");
    assertEquals(var1, getVar1);

    Var getVar2 = scope.getVar("x");
    assertEquals(var2, getVar2);

    Var getVar3 = scope.getVar("x");
    assertEquals(var3, getVar3);

    Var getVarNull = scope.getVar("y");
    assertNull(getVarNull);

    Var getVarParent = new Scope(scope, null).getVar("x");
    assertEquals(var1, getVarParent);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, JSType.getUndefinedType(), null);
    Var var2 = scope.declare("x", null, JSType.getBooleanType(), null);
    Var var3 = scope.declare("x", null, JSType.getIntType(), null);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("x", true));

    assertFalse(scope.isDeclared("y", false));
    assertFalse(scope.isDeclared("y", true));

    assertTrue(scope.getParent().isDeclared("x", false));
    assertTrue(scope.getParent().isDeclared("x", true));

    assertFalse(scope.getParent().isDeclared("y", false));
    assertFalse(scope.getParent().isDeclared("y", true));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, JSType.getUndefinedType(), null);
    Var var2 = scope.declare("x", null, JSType.getBooleanType(), null);
    Var var3 = scope.declare("x", null, JSType.getIntType(), null);

    Iterator<Var> iterator = scope.getVars();

    assertTrue(iterator.hasNext());
    Var next1 = iterator.next();
    assertEquals(var1, next1);

    assertTrue(iterator.hasNext());
    Var next2 = iterator.next();
    assertEquals(var2, next2);

    assertTrue(iterator.hasNext());
    Var next3 = iterator.next();
    assertEquals(var3, next3);

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("x", null, JSType.getUndefinedType(), null);
    Var var2 = scope.declare("x", null, JSType.getBooleanType(), null);
    Var var3 = scope.declare("x", null, JSType.getIntType(), null);

    assertEquals(3, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isGlobal());

    Scope childScope = new Scope(scope, null);
    assertFalse(childScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isLocal());

    Scope childScope = new Scope(scope, null);
    assertTrue(childScope.isLocal());
  }
}