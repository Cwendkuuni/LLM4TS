package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeIsBottom() {
    Scope scope = new Scope(null, null);
    assertTrue(scope.isBottom());
  }

  @Test
  public void testScopeIsNotBottom() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);
    assertFalse(scope.isBottom());
  }

  @Test
  public void testScopeGetDepth() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    Scope scope = new Scope(parentScope, Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0)));
    assertEquals(3, scope.getDepth());
  }

  @Test
  public void testScopeGetParent() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    Scope scope = new Scope(parentScope, Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0)));
    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testScopeGetTypeOfThis() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    ObjectType thisType = new ObjectType(JSType.getTopType());
    Scope scope = new Scope(parentScope, Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, thisType, null, 0)), thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testScopeDeclare() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    String name = "foo";
    JSType type = JSType.getPrimitiveType(JSType.INT);
    CompilerInput input = new CompilerInput("test.js", "test file");
    Var var = parentScope.declare(name, Node.newNameNode(name, null), type, input, true);
    assertEquals(name, var.getName());
    assertEquals(parentScope, var.getScope());
  }

  @Test
  public void testScopeGetVar() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    String name = "foo";
    JSType type = JSType.getPrimitiveType(JSType.INT);
    CompilerInput input = new CompilerInput("test.js", "test file");
    Var var = parentScope.declare(name, Node.newNameNode(name, null), type, input, true);
    assertEquals(name, parentScope.getVar(name).getName());
  }

  @Test
  public void testScopeIsDeclared() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    String name = "foo";
    Var var = parentScope.declare(name, Node.newNameNode(name, null), null, null, true);
    assertTrue(parentScope.isDeclared(name, false));
    assertFalse(parentScope.isDeclared(name, true));
    assertFalse(globalScope.isDeclared(name, false));
  }

  @Test
  public void testScopeGetVars() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    String name1 = "foo";
    String name2 = "bar";
    JSType type1 = JSType.getPrimitiveType(JSType.INT);
    JSType type2 = JSType.getPrimitiveType(JSType.STRING);
    CompilerInput input1 = new CompilerInput("test1.js", "test file 1");
    CompilerInput input2 = new CompilerInput("test2.js", "test file 2");
    parentScope.declare(name1, Node.newNameNode(name1, null), type1, input1, true);
    parentScope.declare(name2, Node.newNameNode(name2, null), type2, input2, true);
    Iterator<Var> vars = parentScope.getVars();
    int count = 0;
    while (vars.hasNext()) {
      Var var = vars.next();
      assertEquals(count++, var.getIndex());
    }
  }

  @Test
  public void testScopeGetVarCount() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    String name1 = "foo";
    String name2 = "bar";
    JSType type1 = JSType.getPrimitiveType(JSType.INT);
    JSType type2 = JSType.getPrimitiveType(JSType.STRING);
    CompilerInput input1 = new CompilerInput("test1.js", "test file 1");
    CompilerInput input2 = new CompilerInput("test2.js", "test file 2");
    parentScope.declare(name1, Node.newNameNode(name1, null), type1, input1, true);
    parentScope.declare(name2, Node.newNameNode(name2, null), type2, input2, true);
    assertEquals(2, parentScope.getVarCount());
  }

  @Test
  public void testScopeIsGlobal() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    assertTrue(globalScope.isGlobal());
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    assertFalse(parentScope.isGlobal());
  }

  @Test
  public void testScopeIsLocal() {
    Node rootNode = Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0));
    Scope globalScope = new Scope(null, rootNode);
    Scope parentScope = new Scope(globalScope, Node.newBlockNode(null, null));
    assertTrue(parentScope.isLocal());
    Scope scope = new Scope(parentScope, Node.newFunctionNode(new FunctionType(new ObjectType(JSType.VOID), new ObjectType(JSType.VOID), null, new ObjectType(JSType.VOID), null, 0)));
    assertTrue(scope.isLocal());
  }
}