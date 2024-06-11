package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticTypes;
import com.google.javascript.rhino.jstype.TypeRegistry;

public class ScopeTest {
  private static final String VAR_NAME = "testVar";
  private static final Node ROOT_NODE = mock(Node.class);
  private static final ObjectType THIS_TYPE = mock(ObjectType.class);
  private Scope scope;

  @Before
  public void setUp() {
    when(ROOT_NODE.getJSType()).thenReturn(null);
    TypeRegistry typeRegistry = mock(TypeRegistry.class);
    when(typeRegistry.getNativeObjectType(GLOBAL_THIS)).thenReturn(THIS_TYPE);
    scope = new Scope(null, ROOT_NODE, typeRegistry);
  }

  @Test
  public void testCreateGlobalScope() {
    Scope scope = new Scope(ROOT_NODE, null);
    assertEquals(0, scope.getDepth());
    assertEquals(THIS_TYPE, scope.getTypeOfThis());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testCreateFunctionScope() {
    JSType functionType = StaticTypes.createFunctionType(null, null, null);
    Scope parentScope = mock(Scope.class);
    ArgumentCaptor<Node> rootNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<ObjectType> thisTypeCaptor = ArgumentCaptor.forClass(ObjectType.class);
    Scope childScope = new Scope(parentScope, ROOT_NODE, functionType, thisTypeCaptor.capture());
    assertEquals(parentScope, childScope.getParent());
    assertEquals(parentScope.getDepth() + 1, childScope.getDepth());
    assertEquals(thisTypeCaptor.getValue(), childScope.getTypeOfThis());
    assertFalse(childScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    JSType variableType = StaticTypes.createStringType();
    CompilerInput input = mock(CompilerInput.class);

    Var var = scope.declare(VAR_NAME, null, variableType, input);
    Var expectedVar = new Var(false, VAR_NAME, null, variableType, scope, 0, input, false, null);
    assertEquals(expectedVar, var);
    assertTrue(scope.getVars().contains(expectedVar));
    assertEquals(1, scope.getVarCount());
    assertEquals(expectedVar, scope.getOwnSlot(VAR_NAME));
  }

  @Test(expected = NullPointerException.class)
  public void testDeclareVariableNullName() {
    scope.declare(null, null, StaticTypes.createStringType(), null);
  }

  @Test
  public void testDeclareVariableDuplicateName() {
    JSType variableType = StaticTypes.createStringType();
    CompilerInput input = mock(CompilerInput.class);
    Var firstVar = scope.declare(VAR_NAME, null, variableType, input);
    scope.declare(VAR_NAME, null, variableType, input);
    assertEquals(firstVar, scope.getOwnSlot(VAR_NAME));
    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testGetVar() {
    Var var1 = scope.declare(VAR_NAME + "_1", null, StaticTypes.createNumberType(), null);
    Var var2 = scope.declare(VAR_NAME + "_2", null, StaticTypes.createBooleanType(), null);
    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
    when(parent.getVar(nameCaptor.capture())).thenReturn(null);

    Var testVar = scope.getVar(VAR_NAME);
    ArgumentCaptor<Var> expectedVarCaptor = ArgumentCaptor.forClass(Var.class);
    scope.getParent().getVars().add(expectedVarCaptor.capture());

    assertEquals(var1, testVar);
    assertEquals(VAR_NAME + "_1", var1.getName());
    assertEquals(expectedVarCaptor.getValue(), scope.getParent());
  }

  @Test
  public void testGetVarNullParent() {
    Var var = scope.declare(VAR_NAME, null, StaticTypes.createStringType(), null);
    assertNull(scope.getVar("nonExistentVar"));
  }

  @Test
  public void testGetVarRecursive() {
    Scope parentScope = mock(Scope.class);
    Var parentVar = mock(Var.class);
    when(parentScope.getVar(VAR_NAME)).thenReturn(parentVar);
    scope = new Scope(parentScope, ROOT_NODE);
    Var testVar = scope.getVar(VAR_NAME);
    assertEquals(parentVar, testVar);
  }

  @Test
  public void testIsDeclared() {
    Var var1 = scope.declare(VAR_NAME + "_1", null, StaticTypes.createNumberType(), null);
    assertFalse(scope.isDeclared(VAR_NAME, false));
    assertTrue(scope.isDeclared(VAR_NAME + "_1", false));
    assertTrue(scope.isDeclared(VAR_NAME + "_1", true));
    assertTrue(scope.isDeclared(VAR_NAME + "_1", false));
    assertTrue(scope.isDeclared(VAR_NAME + "_1", false));
  }

  @Test
  public void testIsDeclaredRecursive() {
    Scope parentScope = mock(Scope.class);
    Var parentVar = mock(Var.class);
    when(parentScope.isDeclared(VAR_NAME, false)).thenReturn(false);
    when(parentScope.getVar(VAR_NAME)).thenReturn(parentVar);
    scope = new Scope(parentScope, ROOT_NODE);
    assertFalse(scope.isDeclared(VAR_NAME, false));
    assertTrue(scope.isDeclared(VAR_NAME, true));
  }

  @Test
  public void testGetVars() {
    Var var1 = scope.declare(VAR_NAME + "_1", null, StaticTypes.createNumberType(), null);
    Var var2 = scope.declare(VAR_NAME + "_2", null, StaticTypes.createBooleanType(), null);

    assertEquals(2, scope.getVars().size());
    assertTrue(scope.getVars().contains(var1));
    assertTrue(scope.getVars().contains(var2));
  }

  @Test
  public void testGetVarsEmpty() {
    assertEquals(0, scope.getVars().size());
  }

  @Test
  public void testGetVarCount() {
    Var var1 = scope.declare(VAR_NAME + "_1", null, StaticTypes.createNumberType(), null);
    Var var2 = scope.declare(VAR_NAME + "_2", null, StaticTypes.createBooleanType(), null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testGetVarCountEmpty() {
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testGetParent() {
    Scope parentScope = mock(Scope.class);
    scope = new Scope(parentScope, ROOT_NODE);
    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testGetParentNull() {
    scope = new Scope(null, ROOT_NODE);
    assertNull(scope.getParent());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(ROOT_NODE, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    assertEquals(THIS_TYPE, scope.getTypeOfThis());
  }

  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
  }

  @Test
  public void testIsBottomNewInstance() {
    Scope scope = new Scope(null, ROOT_NODE, null);
    assertTrue(scope.isBottom());
  }
}