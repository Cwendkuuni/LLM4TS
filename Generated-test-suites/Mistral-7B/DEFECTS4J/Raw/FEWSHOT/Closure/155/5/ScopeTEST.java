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
  public void testCreateScope() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    assertEquals(parent, scope.getParent());
    assertEquals(parent.getRootNode(), scope.getRootNode());
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);

    assertNull(globalScope.getParent());
    assertEquals(globalScope.getRootNode(), globalScope.getRootNode());
    assertTrue(globalScope.isGlobal());
  }

  @Test
  public void testCreateBottomScope() {
    Scope bottomScope = new Scope(new Node(Token.BLOCK), null);

    assertNull(bottomScope.getParent());
    assertEquals(bottomScope.getRootNode(), bottomScope.getRootNode());
    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testGetDepth() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    assertEquals(1, scope.getDepth());
    assertEquals(0, parent.getDepth());
  }

  @Test
  public void testGetThisType() {
    ObjectType globalThis = new ObjectType("Global");
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    globalScope.setTypeOfThis(globalThis);

    Scope scope = new Scope(globalScope.getRootNode(), null);
    ObjectType parentThis = new ObjectType("Parent");
    scope.setTypeOfThis(parentThis);

    ObjectType expectedThisType = new ObjectType("Parent");
    expectedThisType.setPrototypeOf(globalThis);

    assertEquals(expectedThisType, scope.getTypeOfThis());
  }

  @Test
  public void testDeclareVariable() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var var = scope.declare(name, nameNode, type, input);

    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertEquals(true, var.isDefine());
    assertNull(var.getJSDocInfo());
    assertEquals(true, var.isTypeInferred());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");
    JSDocInfo info = new JSDocInfo("MyClass.foo", "Description");

    Var var = scope.declare(name, nameNode, type, input, info);

    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertEquals(true, var.isDefine());
    assertEquals(info, var.getJSDocInfo());
    assertEquals(true, var.isTypeInferred());
  }

  @Test
  public void testDeclareVariableWithSameNameInParentScope() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var parentVar = parentScope.declare(name, nameNode, type, input);
    Var scopeVar = scope.declare(name, nameNode, type, input);

    assertEquals(name, scopeVar.getName());
    assertEquals(nameNode, scopeVar.getNameNode());
    assertEquals(type, scopeVar.getType());
    assertEquals(scope, scopeVar.getScope());
    assertEquals(1, scopeVar.getIndex());
    assertEquals(input, scopeVar.getInput());
    assertEquals(true, scopeVar.isDefine());
    assertEquals(parentVar.getJSDocInfo(), scopeVar.getJSDocInfo());
    assertEquals(true, scopeVar.isTypeInferred());
  }

  @Test
  public void testGetVar() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var var = scope.declare(name, nameNode, type, input);

    assertEquals(var, scope.getVar(name));
    assertNull(parent.getVar(name));
  }

  @Test
  public void testGetVarInParentScope() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var parentVar = parent.declare(name, nameNode, type, input);
    Var scopeVar = scope.declare(name, nameNode, type, input);

    assertEquals(parentVar, scope.getVar(name));
  }

  @Test
  public void testUndeclareVariable() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var var = scope.declare(name, nameNode, type, input);
    scope.undeclare(var);

    assertNull(scope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name = "foo";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new IntType();
    CompilerInput input = new CompilerInput("input.js");

    Var var = scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, true));
    assertTrue(scope.isDeclared(name, false));
    assertFalse(parent.isDeclared(name, true));
    assertFalse(parent.isDeclared(name, false));
  }

  @Test
  public void testGetVars() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name1 = "foo";
    Node nameNode1 = new Node(Token.IDENT, name1);
    JSType type1 = new IntType();
    CompilerInput input1 = new CompilerInput("input1.js");
    Var var1 = scope.declare(name1, nameNode1, type1, input1);

    String name2 = "bar";
    Node nameNode2 = new Node(Token.IDENT, name2);
    JSType type2 = new IntType();
    CompilerInput input2 = new CompilerInput("input2.js");
    Var var2 = scope.declare(name2, nameNode2, type2, input2);

    Iterator<Var> iterator = scope.getVars();
    Var[] vars = new Var[2];
    int i = 0;
    while (iterator.hasNext()) {
      vars[i++] = iterator.next();
    }

    assertEquals(vars[0], var1);
    assertEquals(vars[1], var2);
  }

  @Test
  public void testGetVarCount() {
    Scope parent = new Scope(null, new Node(Token.BLOCK));
    Scope scope = new Scope(parent, new Node(Token.FUNCTION));

    String name1 = "foo";
    Node nameNode1 = new Node(Token.IDENT, name1);
    JSType type1 = new IntType();
    CompilerInput input1 = new CompilerInput("input1.js");
    Var var1 = scope.declare(name1, nameNode1, type1, input1);

    String name2 = "bar";
    Node nameNode2 = new Node(Token.IDENT, name2);
    JSType type2 = new IntType();
    CompilerInput input2 = new CompilerInput("input2.js");
    Var var2 = scope.declare(name2, nameNode2, type2, input2);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    Scope localScope = new Scope(globalScope.getRootNode(), null);

    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    Scope localScope = new Scope(globalScope.getRootNode(), null);

    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }
}