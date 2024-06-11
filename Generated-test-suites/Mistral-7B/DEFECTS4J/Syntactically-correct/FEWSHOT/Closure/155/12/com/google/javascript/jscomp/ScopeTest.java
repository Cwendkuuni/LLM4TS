package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private Scope scope;
  private Node rootNode;

  @Before
  public void setup() {
    rootNode = Node.newProgram();
    scope = new Scope(null, rootNode);
  }

  @Test
  public void testCreateScope() {
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = Node.newFunction(null, null, new ObjectType(null), null, 0);
    Scope functionScope = new Scope(scope, functionNode);

    assertEquals(scope, functionScope.getParent());
    assertEquals(functionNode, functionScope.getRootNode());
    assertNull(functionScope.getTypeOfThis());
    assertEquals(1, functionScope.getDepth());
  }

  @Test
  public void testDeclareVariable() {
    String name = "foo";
    JSType type = new ObjectType(null);
    CompilerInput input = new CompilerInput("test.js");

    Var var = scope.declare(name, Node.newName(name), type, input, true);

    assertEquals(1, scope.getVarCount());
    assertEquals(name, var.getName());
    assertEquals(type, var.getType());
  }

  @Test
  public void testDeclareVariableWithScope() {
    String name = "foo";
    JSType type = new ObjectType(null);
    CompilerInput input = new CompilerInput("test.js");

    Scope childScope = new Scope(scope, Node.newFunction(null, null, new ObjectType(null), null, 0));
    Var var = childScope.declare(name, Node.newName(name), type, input, true);

    assertEquals(1, childScope.getVarCount());
    assertEquals(name, var.getName());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
  }

  @Test
  public void testGetVariable() {
    String name = "foo";
    JSType type = new ObjectType(null);
    CompilerInput input = new CompilerInput("test.js");

    Var var = scope.declare(name, Node.newName(name), type, input, true);

    Var gotVar = scope.getVar(name);
    assertEquals(var, gotVar);
  }

  @Test
  public void testGetVariableWithParent() {
    String name = "foo";
    JSType type = new ObjectType(null);
    CompilerInput input = new CompilerInput("test.js");

    Scope childScope = new Scope(scope, Node.newFunction(null, null, new ObjectType(null), null, 0));
    Var var = childScope.declare(name, Node.newName(name), type, input, true);

    Var gotVar = scope.getVar(name);
    assertNull(gotVar);

    gotVar = childScope.getVar(name);
    assertEquals(var, gotVar);
  }

  @Test
  public void testUndelecareVariable() {
    String name = "foo";
    JSType type = new ObjectType(null);
    CompilerInput input = new CompilerInput("test.js");

    Var var = scope.declare(name, Node.newName(name), type, input, true);
    scope.undeclare(var);

    Var gotVar = scope.getVar(name);
    assertNull(gotVar);
  }

  @Test
  public void testIsDeclared() {
    String name = "foo";

    assertTrue(scope.isDeclared(name, false));
    assertFalse(scope.isDeclared(name, true));

    scope.declare(name, Node.newName(name), new ObjectType(null), null, false);
    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVarsIterator() {
    String name1 = "foo";
    String name2 = "bar";
    JSType type = new ObjectType(null);
    CompilerInput input1 = new CompilerInput("test1.js");
    CompilerInput input2 = new CompilerInput("test2.js");

    Var var1 = scope.declare(name1, Node.newName(name1), type, input1, true);
    Var var2 = scope.declare(name2, Node.newName(name2), type, input2, true);

    Iterator<Var> it = scope.getVars().iterator();
    assertTrue(it.hasNext());
    Var next = it.next();
    assertEquals(name1, next.getName());

    assertTrue(it.hasNext());
    next = it.next();
    assertEquals(name2, next.getName());

    assertFalse(it.hasNext());
  }

  @Test
  public void testJoinScopes() {
    String name1 = "foo";
    String name2 = "bar";
    JSType type = new ObjectType(null);
    CompilerInput input1 = new CompilerInput("test1.js");
    CompilerInput input2 = new CompilerInput("test2.js");

    Scope childScope = new Scope(scope, Node.newFunction(null, null, new ObjectType(null), null, 0));

    Var var1 = childScope.declare(name1, Node.newName(name1), type, input1, true);
    Var var2 = scope.declare(name2, Node.newName(name2), type, input2, true);

    Var joinedVar1 = childScope.getVar(name1);
    Var joinedVar2 = scope.getVar(name2);

    JSType joinedType = joinedVar1.getType().join(joinedVar2.getType());

    assertEquals(type, joinedType);
  }

  @Test
  public void testJoinScopesWithNull() {
    JSType type1 = new ObjectType(null);
    JSType type2 = new ObjectType(null);

    JSType joinedType = TypeJoiner.join(type1, null);
    JSType joinedType2 = TypeJoiner.join(null, type2);

    assertEquals(type1, joinedType);
    assertEquals(type2, joinedType2);

    assertEquals(type1, joinedType.join(type2));
    assertEquals(type2, joinedType2.join(type1));
  }

  @Test
  public void testGetParentScope() {
    Scope childScope = new Scope(scope, Node.newFunction(null, null, new ObjectType(null), null, 0));
    assertEquals(scope, childScope.getParentScope());
  }

  @Test
  public void testIsBottom() {
    Scope bottomScope = new Scope(rootNode, new ObjectType(null));
    assertFalse(bottomScope.isBottom());

    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType globalThisType = new ObjectType(null);

    assertEquals(globalThisType, scope.getTypeOfThis());
  }

  @Test
  public void testCreateGlobalScope() {
    Node rootNode = Node.newProgram();
    Scope globalScope = new Scope(rootNode, null);

    assertEquals(rootNode, globalScope.getRootNode());
    assertEquals(null, globalScope.getParent());
    assertNull(globalScope.getTypeOfThis());
    assertEquals(0, globalScope.getDepth());
  }
}