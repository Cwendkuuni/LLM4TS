package com.google.javascript.jscomp; // Make sure the test package matches the class package

import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.VariableType;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Scope.class)
public class ScopeTest {

  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    Scope parentScope = createMock(Scope.class);
    Node rootNode = PowerMockito.mock(Node.class);

    Scope scope = new Scope(parentScope, rootNode);

    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndCompiler() { // Add a test case for this constructor
    // ...
  }

  @Test
  public void testScopeConstructorWithRootNodeAndThisType() { // Add a test case for this constructor
    // ...
  }

  @Test
  public void testScopeGetDepth() {
    Scope scope = new Scope(null, null);
    assertEquals(0, scope.getDepth());

    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);
    assertEquals(1, childScope.getDepth());
    assertEquals(0, parentScope.getDepth());
  }

  @Test
  public void testScopeIsBottom() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isBottom());

    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(parentScope, null);
    assertFalse(childScope.isBottom());
    assertTrue(parentScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Scope scope = new Scope(null, null);

    String name = "variable";
    Node nameNode = PowerMockito.mock(Node.class);
    JSType type = VariableType.createNullType();
    CompilerInput input = null;

    Var var = scope.declare(name, nameNode, type, input);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertFalse(var.isTypeInferred());
    assertNull(var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
    assertEquals(0, var.getIndex());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Scope scope = new Scope(null, null);

    String name = "variable";
    Node nameNode = PowerMockito.mock(Node.class);
    JSType type = VariableType.createNullType();
    CompilerInput input = null;
    JSDocInfo info = PowerMockito.mock(JSDocInfo.class);

    Var var = scope.declare(name, nameNode, type, input, info);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertFalse(var.isTypeInferred());
    assertEquals(info, var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
    assertEquals(0, var.getIndex());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testDeclareVariableWithInput() {
    Scope scope = new Scope(null, null);

    String name = "variable";
    Node nameNode = PowerMockito.mock(Node.class);
    JSType type = VariableType.createNullType();
    CompilerInput input = PowerMockito.mock(CompilerInput.class);

    Var var = scope.declare(name, nameNode, type, input);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertFalse(var.isTypeInferred());
    assertNull(var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertEquals(input, var.getInput()); // Add check for input
    assertFalse(var.isNoShadow());
    assertEquals(0, var.getIndex());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testDeclareVariableWithInferredType() {
    Scope scope = new Scope(null, null);

    String name = "variable";
    Node nameNode = PowerMockito.mock(Node.class);
    JSType type = VariableType.createNullType();
    CompilerInput input = null;
    boolean inferred = true;

    Var var = scope.declare(name, nameNode, type, input, inferred);
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertTrue(var.isTypeInferred());
    assertNull(var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
    assertEquals(0, var.getIndex());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testGetParentScope() {
    Scope parentScope = createMock(Scope.class);
    Scope childScope = new Scope(parentScope, null);

    assertEquals(parentScope, childScope.getParent());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = PowerMockito.mock(ObjectType.class);
    Scope scope = new Scope(null, thisType);

    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test(expected = AssertionError.class)
  public void testGetTypeOfThisWhenThisTypeIsNull() {
    Scope scope = new Scope(null, null);

    assertEquals(null, scope.getTypeOfThis());
  }

  @Test
  public void testGetSlot() {
    Scope scope = new Scope(null, null);
    String name = "variable";
    Var var = new Var("", name, null, null, scope, 0, null, false, null);
    scope.vars.put(name, var);

    StaticSlot<JSType> slot = scope.getSlot(name);
    assertEquals(var, slot);
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    String name = "variable";

    assertTrue(scope.isDeclared(name, false));

    Scope parentScope = new Scope(null, null);
    scope = new Scope(parentScope, null);
    assertFalse(scope.isDeclared(name, false));
    assertTrue(parentScope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredWithInferredType() {
    Scope scope = new Scope(null, null);
    String name = "variable";

    assertTrue(scope.isDeclared(name, false));

    Scope parentScope = new Scope(null, null);
    scope = new Scope(parentScope, null);
    assertFalse(scope.isDeclared(name, false));
    assertTrue(parentScope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    Scope scope = new Scope(null, null);

    String name1 = "variable1";
    Node nameNode1 = PowerMockito.mock(Node.class);
    JSType type1 = VariableType.createNullType();
    CompilerInput input1 = null;
    Var var1 = scope.declare(name1, nameNode1, type1, input1);

    String name2 = "variable2";
    Node nameNode2 = PowerMockito.mock(Node.class);
    JSType type2 = VariableType.createNullType();
    CompilerInput input2 = null;
    Var var2 = scope.declare(name2, nameNode2, type2, input2);

    Iterator<Var> vars = scope.getVars();

    Var var = vars.next();
    assertEquals(var1, var);

    var = vars.next();
    assertEquals(var2, var);

    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Scope scope = new Scope(null, null);

    String name1 = "variable1";
    Node nameNode1 = PowerMockito.mock(Node.class);
    JSType type1 = VariableType.createNullType();
    CompilerInput input1 = null;
    Var var1 = scope.declare(name1, nameNode1, type1, input1);

    String name2 = "variable2";
    Node nameNode2 = PowerMockito.mock(Node.class);
    JSType type2 = VariableType.createNullType();
    CompilerInput input2 = null;
    Var var2 = scope.declare(name2, nameNode2, type2, input2);

    assertEquals(2, scope.getVarCount());
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

  @Test
  public void testUndeclareVariable() {
    Scope scope = new Scope(null, null);

    String name = "variable";
    Node nameNode = PowerMockito.mock(Node.class);
    JSType type = VariableType.createNullType();
    CompilerInput input = null;
    Var var = scope.declare(name, nameNode, type, input);

    scope.undeclare(var);

    assertFalse(scope.isDeclared(name, false));
  }
}