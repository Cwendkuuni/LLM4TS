package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  @SuppressWarnings("unchecked")
  private static <T> T createScope(Node rootNode, ObjectType thisType, boolean isBottom) {
    Scope scope = null;
    if (isBottom) {
      scope = new Scope(rootNode, thisType);
    } else {
      scope = new Scope(null, rootNode);
      if (thisType != null) {
        scope.setTypeOfThis(thisType);
      }
    }
    return (T) scope;
  }

  @SuppressWarnings("unchecked")
  private static Var createVar(String name, Node nameNode, JSType type, CompilerInput input) {
    return (Var) new Scope.Var(true, name, nameNode, type, null, -1, input, false, null);
  }

  @org.junit.Test
  public void testScopeCreation() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);
    assertNotNull(scope);
    assertEquals(0, scope.getDepth());
    assertEquals(false, scope.isBottom());
    assertNull(scope.getParent());
  }

  @org.junit.Test
  public void testScopeCreationWithThisType() {
    Node rootNode = new Node(Token.FUNCTION, null);
    Scope scope = createScope(rootNode, new ObjectType("Object"), false);
    assertNotNull(scope);
    assertEquals(1, scope.getDepth());
    assertEquals(false, scope.isBottom());
    assertEquals(null, scope.getParent());
    assertEquals(new ObjectType("Object"), scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testScopeCreationBottom() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, true);
    assertNotNull(scope);
    assertEquals(0, scope.getDepth());
    assertEquals(true, scope.isBottom());
    assertEquals(null, scope.getParent());
  }

  @org.junit.Test
  public void testDeclareVariable() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var var = scope.declare(name, nameNode, type, input);

    assertEquals(1, scope.getVarCount());
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.getInput());
    assertEquals(false, var.isDefine());
    assertFalse(var.isConst());
    assertFalse(var.isExtern());
    assertFalse(var.isBleedingFunction());
    assertNull(var.getInitialValue());
  }

  @org.junit.Test
  public void testDeclareVariableWithInfo() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    JSDocInfo info = new JSDocInfo("@define name");
    Var var = scope.declare(name, nameNode, type, input, info);

    assertEquals(1, scope.getVarCount());
    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(input, var.getInput());
    assertTrue(var.isDefine());
    assertFalse(var.isConst());
    assertFalse(var.isExtern());
    assertFalse(var.isBleedingFunction());
    assertNull(var.getInitialValue());
    assertEquals(info, var.getJSDocInfo());
  }

  @org.junit.Test
  public void testUndeclareVariable() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var var = scope.declare(name, nameNode, type, input);
    scope.undeclare(var);

    assertNull(scope.getVar(name));
  }

  @org.junit.Test
  public void testIsDeclared() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);

    assertTrue(scope.isDeclared(name, true));

    scope.undeclare(createVar(name, nameNode, null, null));

    assertFalse(scope.isDeclared(name, true));
  }

  @org.junit.Test
  public void testGetSlot() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var var = scope.declare(name, nameNode, type, input);

    Var slot = scope.getSlot(name);
    assertEquals(var, slot);
  }

  @org.junit.Test
  public void testGetOwnSlot() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var var = scope.declare(name, nameNode, type, input);

    Var slot = scope.getOwnSlot(name);
    assertEquals(var, slot);
  }

  @org.junit.Test
  public void testGetVar() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var var = scope.declare(name, nameNode, type, input);

    Var slot = scope.getVar(name);
    assertEquals(var, slot);
  }

  @org.junit.Test
  public void testGetVarNull() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    Var slot = scope.getVar("name");
    assertNull(slot);
  }

  @org.junit.Test
  public void testGetParent() {
    Node parentRootNode = new Node(Token.BLOCK, null);
    Node rootNode = new Node(Token.FUNCTION, parentRootNode);
    Scope parentScope = createScope(parentRootNode, null, false);
    Scope scope = createScope(rootNode, null, false);
    scope.setParent(parentScope);

    assertEquals(parentScope, scope.getParent());
  }

  @org.junit.Test
  public void testGetDepth() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope1 = createScope(rootNode, null, false);
    Scope scope2 = new Scope(null, rootNode);
    scope2.setParent(scope1);

    assertEquals(1, scope2.getDepth());
    assertEquals(0, scope1.getDepth());
  }

  @org.junit.Test
  public void testIsBottom() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope1 = createScope(rootNode, null, false);
    Scope scope2 = new Scope(rootNode, null);
    scope2.setParent(scope1);

    assertFalse(scope2.isBottom());
    assertTrue(scope1.isBottom());
  }

  @org.junit.Test
  public void testGetRootNode() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    assertEquals(rootNode, scope.getRootNode());
  }

  @org.junit.Test
  public void testGetParentScope() {
    Node parentRootNode = new Node(Token.BLOCK, null);
    Node rootNode = new Node(Token.FUNCTION, parentRootNode);
    Scope parentScope = createScope(parentRootNode, null, false);
    Scope scope = createScope(rootNode, null, false);
    scope.setParent(parentScope);

    assertEquals(parentScope, scope.getParentScope());
  }

  @org.junit.Test
  public void testTypeOfThis() {
    Node rootNode = new Node(Token.FUNCTION, null);
    Scope scope = createScope(rootNode, new ObjectType("Function"), false);

    assertEquals(new ObjectType("Function"), scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testTypeOfThisNull() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    assertNull(scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testDeclareDuplicateVariable() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);

    Var var1 = scope.declare(name, nameNode, type, input);
    Var var2 = scope.declare(name, nameNode, type, input);

    assertEquals(1, scope.getVarCount());
    assertEquals(var1, var2);
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameButDifferentNode() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name1 = "name";
    Node nameNode1 = new Node(Token.IDENT, name1);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);

    String name2 = "name";
    Node nameNode2 = new Node(Token.IDENT, name2);
    Var var1 = scope.declare(name1, nameNode1, type, input);

    Var var2 = scope.declare(name2, nameNode2, type, input);

    assertEquals(1, scope.getVarCount());
    assertNotSame(var1, var2);
    assertNull(var1.getParentNode());
    assertNull(var2.getParentNode());
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameButSameNode() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("test.js", 1, 1);

    Var var1 = scope.declare(name, nameNode, type, input);
    Var var2 = scope.declare(name, nameNode, type, input);

    assertEquals(1, scope.getVarCount());
    assertEquals(var1, var2);
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameButSameNodeAndDifferentInput() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = createScope(rootNode, null, false);

    String name = "name";
    Node nameNode = new Node(Token.IDENT, name);
    JSType type = new ObjectType("Object");
    CompilerInput input1 = new CompilerInput("test1.js", 1, 1);
    CompilerInput input2 = new CompilerInput("test2.js", 1, 1);

    Var var1 = scope.declare(name, nameNode, type, input1);
    Var var2 = scope.declare(name, nameNode, type, input2);

    assertEquals(1, scope.getVarCount());
    assertEquals(var1, var2);
    assertNotSame(input1, input2);
  }
}