package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {
  @Test
  public void testConstructorWithParentAndRootNode() {
    Scope parent = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parent, rootNode);

    assertEquals(parent, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testConstructorWithGlobalRootNode() {
    Node rootNode = new Node(Token.BLOCK, new Node[0]);
    Scope scope = new Scope(null, rootNode);

    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testConstructorWithBottomRootNode() {
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(rootNode, new ObjectType(rootNode.getSourceName(), "ObjectType"));

    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testGetDepth() {
    Scope parent = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parent, rootNode);

    assertEquals(1, scope.getDepth());
    assertEquals(0, parent.getDepth());
  }

  @Test
  public void testIsBottom() {
    Scope scope = new Scope(null, new Node(Token.FUNCTION, new Node[0]));

    assertTrue(scope.isBottom());

    ObjectType thisType = new ObjectType("ThisType", "ObjectType");
    scope = new Scope(null, new Node(Token.FUNCTION, new Node[0]), thisType);

    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetParent() {
    Scope parent = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parent, rootNode);

    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testGetGlobalScope() {
    Scope rootScope = new Scope(null, new Node(Token.BLOCK, new Node[0]));

    assertEquals(rootScope, rootScope.getGlobalScope());

    Scope parentScope = new Scope(rootScope, new Node(Token.FUNCTION, new Node[0]));
    assertEquals(rootScope, parentScope.getGlobalScope());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType("ThisType", "ObjectType");

    Scope scope = new Scope(null, new Node(Token.FUNCTION, new Node[0]));
    assertEquals(null, scope.getTypeOfThis());

    scope = new Scope(null, new Node(Token.FUNCTION, new Node[0]), thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testDeclareAndGetVar() {
    Scope parent = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parent, rootNode);

    String name = "name";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    assertSame(name, var.getName());
    assertSame(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(vars.size(), scope.getVarCount());

    Var varFromParent = parent.getVar(name);
    assertNull(varFromParent);
  }

  @Test
  public void testDeclareAndGetVarWithParent() {
    String parentName = "parentName";
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, parentName));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node parentNameNode = new Node(Token.NAME, parentName);
    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var parentVar = parentScope.declare(parentName, parentNameNode, type, input);

    Var var = scope.declare(name, nameNode, type, input, false);

    assertSame(name, var.getName());
    assertSame(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(parentScope, var.getScope().getParent());
    assertEquals(parentVar, parentScope.getVar(parentName));
    assertEquals(vars.size(), scope.getVarCount());
  }

  @Test
  public void testDeclareAndGetVarWithSameNameButDifferentNodes() {
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node nameNode1 = new Node(Token.NAME, name);
    Node nameNode2 = new Node(Token.NAME, name + "_");
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var1 = scope.declare(name, nameNode1, type, input);

    Var var2 = scope.declare(name, nameNode2, type, input, false);

    assertSame(name, var1.getName());
    assertSame(nameNode1, var1.getNameNode());
    assertEquals(type, var1.getType());
    assertEquals(scope, var1.getScope());
    assertEquals(vars.size(), scope.getVarCount());

    assertSame(name, var2.getName());
    assertSame(nameNode2, var2.getNameNode());
    assertEquals(type, var2.getType());
    assertEquals(scope, var2.getScope());
    assertEquals(vars.size(), scope.getVarCount());
  }

  @Test
  public void testGetSlot() {
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    StaticSlot<JSType> slot = scope.getSlot(name);
    assertEquals(var, slot);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "name";

    Scope scope = new Scope(null, new Node(Token.FUNCTION, new Node[0]));

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    StaticSlot<JSType> slot = scope.getOwnSlot(name);
    assertEquals(var, slot);
  }

  @Test
  public void testGetVar() {
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    Var varFromScope = scope.getVar(name);
    assertSame(var, varFromScope);

    Var varFromParent = parentScope.getVar(name);
    assertNull(varFromParent);
  }

  @Test
  public void testGetVarWithRecursion() {
    String name = "name";

    Scope parentScope1 = new Scope(null, new Node(Token.NAME, "parent1"));
    Scope parentScope2 = new Scope(parentScope1, new Node(Token.NAME, "parent2"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope2, rootNode);

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var varFromScope = scope.getVar(name);
    assertNull(varFromScope);

    Var varFromParent1 = parentScope1.getVar(name);
    assertNull(varFromParent1);

    Var varFromParent2 = parentScope2.getVar(name);
    assertNull(varFromParent2);

    Var var = parentScope1.declare(name, nameNode, type, input, false);

    Var varFromScopeWithRecursion = scope.getVar(name, true);
    assertSame(var, varFromScopeWithRecursion);
  }

  @Test
  public void testIsDeclared() {
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, true));
    assertFalse(parentScope.isDeclared(name, false));
    assertFalse(parentScope.isDeclared(name, true));
  }

  @Test
  public void testUndeclare() {
    String name = "name";

    Scope parentScope = new Scope(null, new Node(Token.NAME, "parent"));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node nameNode = new Node(Token.NAME, name);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input = new CompilerInput("input", "inputName");

    Var var = scope.declare(name, nameNode, type, input);

    assertEquals(1, scope.getVarCount());

    scope.undeclare(var);

    assertEquals(0, scope.getVarCount());

    Var varFromParent = parentScope.getVar(name);
    assertNull(varFromParent);
  }

  @Test
  public void testGetVars() {
    String parentName = "parent";
    String name1 = "name1";
    String name2 = "name2";

    Scope parentScope = new Scope(null, new Node(Token.NAME, parentName));
    Node rootNode = new Node(Token.FUNCTION, new Node[0]);
    Scope scope = new Scope(parentScope, rootNode);

    Node parentNameNode = new Node(Token.NAME, parentName);
    Node name1Node = new Node(Token.NAME, name1);
    Node name2Node = new Node(Token.NAME, name2);
    JSType type = new ObjectType("ObjectType", "ObjectType");
    CompilerInput input1 = new CompilerInput("input1", "inputName1");
    CompilerInput input2 = new CompilerInput("input2", "inputName2");

    Var parentVar = parentScope.declare(parentName, parentNameNode, type, input1);

    Var var1 = scope.declare(name1, name1Node, type, input1, false);
    Var var2 = scope.declare(name2, name2Node, type, input2, false);

    Iterator<Var> iterator = scope.getVars();

    assertTrue(iterator.hasNext());
    Var var1FromIterator = iterator.next();
    assertEquals(name1, var1FromIterator.getName());

    assertTrue(iterator.hasNext());
    Var var2FromIterator = iterator.next();
    assertEquals(name2, var2FromIterator.getName());

    assertFalse(iterator.hasNext());

    assertEquals(2, scope.getVarCount());
  }
}