package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    Node rootNode = mock(Node.class);
    Scope parent = mock(Scope.class);

    Scope scope = new Scope(parent, rootNode);

    assertEquals(parent, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndCompiler() {
    Node rootNode = mock(Node.class);
    ObjectType thisType = mock(ObjectType.class);

    Scope scope = new Scope(rootNode, thisType);

    assertEquals(rootNode, scope.getRootNode());
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndNullThisType() {
    Node rootNode = mock(Node.class);

    Scope scope = new Scope(rootNode, null);

    assertEquals(rootNode, scope.getRootNode());
    assertNull(scope.getTypeOfThis());
  }

  @Test
  public void testScopeIsBottom() {
    Node rootNode = mock(Node.class);
    ObjectType thisType = mock(ObjectType.class);

    Scope scope = new Scope(rootNode, thisType);

    assertFalse(scope.isBottom());

    Scope bottomScope = new Scope(rootNode, thisType);
    bottomScope.setBottom();

    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testDeclareMethodWithEmptyMap() {
    String name = "testVar";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);

    Scope scope = new Scope(null, null);

    Var result = scope.declare(name, nameNode, type, input, false);

    assertEquals(name, result.getName());
    assertEquals(nameNode, result.getNameNode());
    assertEquals(type, result.getType());
    assertEquals(scope, result.getScope());
  }

  @Test
  public void testGetSlotMethod() {
    String name = "testVar";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);

    Scope scope = new Scope(null, null);
    scope.declare(name, nameNode, type, input, false);

    assertEquals(variable, scope.getSlot(name));
  }

  @Test
  public void testGetVarMethodWithDeclaredVariable() {
    String name = "testVar";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);

    Scope scope = new Scope(null, null);
    scope.declare(name, nameNode, type, input, false);

    assertEquals(variable, scope.getVar(name));
  }

  @Test
  public void testGetVarMethodWithUndefinedVariable() {
    String name = "testVar";
    Scope scope = new Scope(null, null);

    assertNull(scope.getVar(name));
  }

  @Test
  public void testIsDeclaredMethodWithDeclaredVariable() {
    String name = "testVar";
    Scope scope = new Scope(null, null);
    scope.declare(name, null, null, null, false);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredMethodWithUndefinedVariable() {
    String name = "testVar";
    Scope scope = new Scope(null, null);

    assertFalse(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredMethodWithParentScopeDeclaresVariable() {
    String name = "testVar";
    Scope parentScope = mock(Scope.class);
    Var variable = mock(Var.class);
    when(parentScope.getVar(name)).thenReturn(variable);

    Scope scope = new Scope(parentScope, null);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVarsMethod() {
    String name1 = "testVar1";
    String name2 = "testVar2";

    Node nameNode1 = mock(Node.class);
    Node nameNode2 = mock(Node.class);
    JSType type1 = mock(JSType.class);
    JSType type2 = mock(JSType.class);
    CompilerInput input1 = mock(CompilerInput.class);
    CompilerInput input2 = mock(CompilerInput.class);
    Var variable1 = mock(Var.class);
    Var variable2 = mock(Var.class);

    Map<String, Var> expectedVars = new HashMap<>();
    expectedVars.put(name1, variable1);
    expectedVars.put(name2, variable2);

    Scope scope = new Scope(null, null);
    scope.declare(name1, nameNode1, type1, input1, false);
    scope.declare(name2, nameNode2, type2, input2, false);

    Iterator<Var> vars = scope.getVars();

    assertEquals(ImmutableMap.of(name1, variable1, name2, variable2),
        ScopeTestUtils.getVars(vars));
  }

  @Test
  public void testGetVarCountMethod() {
    int varCount = 5;

    Scope scope = new Scope(null, null);

    for (int i = 0; i < varCount; i++) {
      String name = "testVar" + i;
      Node nameNode = mock(Node.class);
      JSType type = mock(JSType.class);
      CompilerInput input = mock(CompilerInput.class);
      scope.declare(name, nameNode, type, input, false);
    }

    assertEquals(varCount, scope.getVarCount());
  }
}