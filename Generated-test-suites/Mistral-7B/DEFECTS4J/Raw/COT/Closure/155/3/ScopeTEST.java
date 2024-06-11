package com.google.javascript.jscomp; // make sure to import other necessary classes

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticScope.Var;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopeTest {

  private Scope scope;
  private Node rootNode;
  private Scope parentScope;
  private CompilerInput input;

  @Before
  public void setUp() {
    parentScope = mock(Scope.class);
    rootNode = mock(Node.class);
    input = mock(CompilerInput.class);
    scope = new Scope(parentScope, rootNode);
  }

  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithGlobalRootNode() {
    scope = new Scope(rootNode, null);
    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithBottomRootNode() {
    scope = new Scope(rootNode, new ObjectType(String.class));
    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testGetDepth() {
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetParent() {
    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testGetTypeOfThis() {
    FunctionType functionType = mock(FunctionType.class);
    when(rootNode.getJSType()).thenReturn(functionType);
    when(functionType.getTypeOfThis()).thenReturn(new ObjectType(String.class));

    ObjectType expectedThisType = new ObjectType(String.class);
    assertEquals(expectedThisType, scope.getTypeOfThis());
  }

  @Test
  public void testDeclareVariable() {
    String name = "name";
    Node nameNode = mock(Node.class);
    JSType expectedType = new ObjectType(String.class);

    Var var = scope.declare(name, nameNode, expectedType, input);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(expectedType, var.getType());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testDeclareVariableWithInferredType() {
    String name = "name";
    Node nameNode = mock(Node.class);
    JSType expectedType = new ObjectType(Number.class);

    Var var = scope.declare(name, nameNode, null, input, true);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertNull(var.getType());
    assertEquals(scope, var.getScope());

    ArgumentCaptor<JSType> typeCaptor = ArgumentCaptor.forClass(JSType.class);
    verify(input).recordVariable(eq(name), typeCaptor.capture());
    assertEquals(expectedType, typeCaptor.getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithNullName() {
    String name = null;
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);

    scope.declare(name, nameNode, type, input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithNullNameNode() {
    String name = "name";
    Node nameNode = null;

    scope.declare(name, nameNode, null, input);
  }

  @Test
  public void testDeclareVariableWithNoType() {
    String name = "name";
    Node nameNode = mock(Node.class);

    Var var = scope.declare(name, nameNode, null, input);
    assertNull(var.getType());
  }

  @Test
  public void testDeclareVariableWithInferredTypeFromNameNode() {
    String name = "name";
    Node nameNode = mock(Node.class);
    JSType expectedType = new ObjectType(String.class);

    when(nameNode.getType()).thenReturn(expectedType);

    Var var = scope.declare(name, nameNode, input);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(expectedType, var.getType());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testGetVar() {
    String name = "name";
    Var var = new Var("John Doe", name, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    vars.add(var);

    Var expectedVar = var;
    Var resultVar = scope.getVar(name);

    assertEquals(expectedVar, resultVar);
  }

  @Test
  public void testGetVarWithNullName() {
    assertNull(scope.getVar(null));
  }

  @Test
  public void testIsDeclared() {
    String name = "name";
    Var var = new Var("John Doe", null, null, null, scope, 0, input, true, null);
    vars.add(var);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredWithNullName() {
    assertFalse(scope.isDeclared(null, true));
  }

  @Test
  public void testIsDeclaredWithNameInParentScope() {
    String name = "name";
    Var var = new Var("John Doe", null, null, null, parentScope, 0, input, true, null);
    vars.add(var);

    assertFalse(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = new Var("John Doe1", null, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    Var var2 = new Var("John Doe2", null, null, new ObjectType(Number.class),
        scope, 1, input, true, null);
    vars.add(var1);
    vars.add(var2);

    Iterator<Var> expectedVars = vars.iterator();
    Iterator<Var> resultVars = scope.getVars().iterator();

    assertEquals(expectedVars.next(), resultVars.next());
    assertEquals(expectedVars.next(), resultVars.next());
  }

  @Test
  public void testGetVarCount() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = new Var("John Doe1", null, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    Var var2 = new Var("John Doe2", null, null, new ObjectType(Number.class),
        scope, 1, input, true, null);
    vars.add(var1);
    vars.add(var2);

    assertEquals(vars.size(), scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
  }

  @Test
  public void testGetOwnSlot() {
    String name = "name";
    Var var = new Var("John Doe", null, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    vars.add(var);

    StaticSlot<JSType> expectedSlot = var;
    StaticSlot<JSType> resultSlot = scope.getOwnSlot(name);

    assertEquals(expectedSlot, resultSlot);
  }

  @Test
  public void testGetSlot() {
    String name = "name";
    Var var = new Var("John Doe", null, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    vars.add(var);

    StaticSlot<JSType> expectedSlot = var;
    StaticSlot<JSType> resultSlot = scope.getSlot(name);

    assertEquals(expectedSlot, resultSlot);
  }

  @Test
  public void testGetSlotWithNullName() {
    assertNull(scope.getSlot(null));
  }

  @Test
  public void testGetSlotWithUndefinedName() {
    assertNull(scope.getSlot("undefinedName"));
  }

  @Test
  public void testUndelecareVariable() {
    String name = "name";
    Var var = new Var("John Doe", null, null, new ObjectType(String.class),
        scope, 0, input, true, null);
    vars.add(var);

    scope.undeclare(var);
    assertFalse(vars.containsKey(name));
  }
}