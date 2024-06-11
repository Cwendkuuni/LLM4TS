package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Iterator;
import java.util.Map;

/**
 * Test class for Scope.
 */
public class ScopeTest {

  @org.junit.Before
  public void setUp() {}

  

  /**
   * Test constructor with parent and rootNode.
   */
  @org.junit.Test
  public void testScopeConstructorWithParentAndRootNode() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNull(scope.thisType);

    Scope globalScope = new Scope(null, rootNode);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertNotNull(globalScope.thisType);

    // Test with null parent
    Scope nullParentScope = new Scope(null, rootNode);
    assertEquals(0, nullParentScope.getDepth());
    assertFalse(nullParentScope.isBottom());
    assertNotNull(nullParentScope.thisType);
  }

  /**
   * Test declare method.
   */
  @org.junit.Test
  public void testDeclareMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; var y = 2; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    String nameY = "y";

    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);
    Var yVar = scope.declare(nameY, rootNode.getChild(3), JSType.NUMBER, null);

    // Check variables are in the scope
    assertEquals(2, scope.getVarCount());
    Var x = scope.getVar(nameX);
    Var y = scope.getVar(nameY);
    assertNotNull(x);
    assertNotNull(y);

    // Check variables have correct properties
    assertEquals(nameX, x.getName());
    assertEquals(nameX, x.getNameNode().getIdentityWiredString());
    assertEquals(JSType.NUMBER, x.getType());
    assertEquals(nameX, x.getInputName());
    assertTrue(x.isLocal());
    assertFalse(x.isDefine());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertNull(x.getInitialValue());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isTypeInferred());

    assertEquals(nameY, y.getName());
    assertEquals(nameY, y.getNameNode().getIdentityWiredString());
    assertEquals(JSType.NUMBER, y.getType());
    assertEquals(nameY, y.getInputName());
    assertTrue(y.isLocal());
    assertFalse(y.isDefine());
    assertNull(y.getParentNode());
    assertFalse(y.isBleedingFunction());
    assertNull(y.getInitialValue());
    assertNull(y.getJSDocInfo());
    assertFalse(y.isTypeInferred());

    // Check variables can be retrieved using getSlot method
    assertEquals(x, scope.getSlot(nameX));
    assertEquals(y, scope.getSlot(nameY));

    // Test getVar method with a non-existent variable
    assertNull(scope.getVar("z"));

    // Test getVar method with parent scope
    Var zVar = parentScope.declare("z", rootNode.getChild(1), JSType.NUMBER, null);
    assertEquals(zVar, scope.getVar("z"));

    // Test getVar method with grandparent scope
    Var aVar = parentScope.getParent().declare("a", rootNode.getPreviousSibling(), JSType.NUMBER, null);
    assertEquals(aVar, scope.getVar("a"));
  }

  /**
   * Test undeclare method.
   */
  @org.junit.Test
  public void testUndeclareMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);
    scope.undeclare(xVar);

    // Check variable is removed from the scope
    assertEquals(0, scope.getVarCount());
    assertNull(scope.getVar(nameX));
  }

  /**
   * Test isDeclared method.
   */
  @org.junit.Test
  public void testIsDeclaredMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);

    // Test isDeclared method with a local variable
    assertTrue(scope.isDeclared(nameX, true));

    // Test isDeclared method with a non-existent variable
    assertFalse(scope.isDeclared("z", true));

    // Test isDeclared method with a parent scope
    String nameY = "y";
    Var yVar =
        parentScope.declare(nameY, rootNode.getChild(1), JSType.NUMBER, null);
    assertTrue(scope.isDeclared(nameY, false));
  }

  /**
   * Test getVars method.
   */
  @org.junit.Test
  public void testGetVarsMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; var y = 2; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    String nameY = "y";

    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);
    Var yVar = scope.declare(nameY, rootNode.getChild(3), JSType.NUMBER, null);

    // Test getVars method
    Iterator<Var> vars = scope.getVars();
    assertEquals(2, vars.size());
    assertTrue(vars.hasNext());
    Var firstVar = vars.next();
    assertEquals(nameX, firstVar.getName());
    assertTrue(vars.hasNext());
    Var secondVar = vars.next();
    assertEquals(nameY, secondVar.getName());
    assertFalse(vars.hasNext());
  }

  /**
   * Test getVarCount method.
   */
  @org.junit.Test
  public void testGetVarCountMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; var y = 2; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    String nameY = "y";

    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);
    Var yVar = scope.declare(nameY, rootNode.getChild(3), JSType.NUMBER, null);

    // Test getVarCount method
    assertEquals(2, scope.getVarCount());
  }

  /**
   * Test isLocal method.
   */
  @org.junit.Test
  public void testIsLocalMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    String nameX = "x";
    Var xVar = scope.declare(nameX, rootNode.getChild(1), JSType.NUMBER, null);

    // Test isLocal method with a local variable
    assertTrue(scope.isLocal());

    // Test isLocal method with a global variable
    Scope globalScope = new Scope(null, rootNode);
    Var globalVar = globalScope.declare("global", rootNode.getChild(1), JSType.NUMBER, null);
    assertFalse(scope.isLocal());
  }

  /**
   * Test isGlobal method.
   */
  @org.junit.Test
  public void testIsGlobalMethod() {
    Node rootNode = NodeTraversal.parseTree("function foo() { var x = 1; }");
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode.getFirstChild());

    // Test isGlobal method with a local variable
    assertFalse(scope.isGlobal());

    // Test isGlobal method with a global variable
    Scope globalScope = new Scope(null, rootNode);
    Var globalVar = globalScope.declare("global", rootNode.getChild(1), JSType.NUMBER, null);
    assertTrue(globalScope.isGlobal());
  }

  
}