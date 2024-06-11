package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test class for {@link Scope} class.
 */
public class ScopeTest {

  @Test
  public void testCreateScope() {
    // Test creating a local scope
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    assertNotNull(localScope);

    // Test creating a global scope
    Scope globalScope = new Scope(Node.newBlockNode(null), new AbstractCompiler() {});
    assertNotNull(globalScope);
  }

  @Test
  public void testIsBottom() {
    // Test creating a bottom scope
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope bottomScope = new Scope(rootNode, null);
    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testGetDepth() {
    // Test getting depth of a scope
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    assertEquals(1, localScope.getDepth());

    Scope parentScope = new Scope(null, Node.newBlockNode(null));
    localScope = new Scope(parentScope, rootNode);
    assertEquals(2, localScope.getDepth());
  }

  @Test
  public void testGetRootNode() {
    // Test getting root node of a scope
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    assertEquals(rootNode, localScope.getRootNode());
  }

  @Test
  public void testGetParent() {
    // Test getting parent scope
    Node rootNode1 = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Node rootNode2 = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope parentScope = new Scope(null, rootNode1);
    Scope childScope = new Scope(parentScope, rootNode2);
    assertEquals(parentScope, childScope.getParent());

    assertNull(childScope.getParent());
  }

  @Test
  public void testGetGlobalScope() {
    // Test getting global scope
    Node rootNode = Node.newBlockNode(null);
    Scope localScope = new Scope(rootNode, new AbstractCompiler() {});
    Scope globalScope = localScope.getGlobalScope();
    assertEquals(localScope.getParent(), globalScope);
    assertNull(globalScope.getParent());
  }

  @Test
  public void testDeclare() {
    // Test declaring a variable
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name = "name";
    Var var = localScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertEquals(name, var.getName());
    assertEquals(localScope, var.getScope());
    assertEquals(1, localScope.getVarCount());
  }

  @Test
  public void testGetSlot() {
    // Test getting a slot for a variable
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name = "name";
    Var var = localScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertEquals(var, localScope.getSlot(name));
  }

  @Test
  public void testGetOwnSlot() {
    // Test getting an own slot for a variable
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name = "name";
    Var var = localScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertEquals(var, localScope.getOwnSlot(name));
  }

  @Test
  public void testGetVar() {
    // Test getting a variable
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name = "name";
    Var var = localScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertEquals(var, localScope.getVar(name));

    Scope parentScope = new Scope(null, Node.newBlockNode(null));
    localScope = new Scope(parentScope, rootNode);
    Var parentVar = parentScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertNull(localScope.getVar(name));
    assertEquals(parentVar, parentScope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    // Test checking if a variable is declared
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name = "name";
    Var var = localScope.declare(name, Node.newNameNode(name), Node.INT_TYPE, null);
    assertTrue(localScope.isDeclared(name, true));
    assertFalse(localScope.isDeclared("otherName", true));
  }

  @Test
  public void testGetVars() {
    // Test getting all variables in a scope
    Node rootNode = Node.newFunctionNode(null, "function", new Object[0], new Object[0]);
    Scope localScope = new Scope(null, rootNode);
    String name1 = "name1";
    Var var1 = localScope.declare(name1, Node.newNameNode(name1), Node.INT_TYPE, null);
    String name2 = "name2";
    Var var2 = localScope.declare(name2, Node.newNameNode(name2), Node.INT_TYPE, null);

    Iterator<Var> vars = localScope.getVars();
    assertEquals(2, localScope.getVarCount());

    Var var;
    while (vars.hasNext()) {
      var = vars.next();
      if (var.getName().equals(name1)) {
        assertEquals(var1, var);
      } else if (var.getName().equals(name2)) {
        assertEquals(var2, var);
      }
    }
  }
}