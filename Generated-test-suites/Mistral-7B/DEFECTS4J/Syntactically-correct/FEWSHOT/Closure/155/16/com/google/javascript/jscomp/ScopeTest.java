package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for the Scope class.
 */
public class ScopeTest {

  @Test
  public void testScopeConstructorWithParent() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode) {
      @Override
      public StaticScope<JSType> getParentScope() {
        // A stub implementation since the test doesn't use the parent scope.
        return null;
      }
    };
    Scope scope = new Scope(parentScope, rootNode);
    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testScopeConstructorWithRootNode() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    assertEquals(null, scope.getParent());
  }

  @Test
  public void testScopeConstructorWithThisType() {
    Node rootNode = new Node(Token.BLOCK);
    ObjectType thisType = new ObjectType("Object");
    Scope scope = new Scope(null, rootNode, thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testScopeGetDepth() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    assertEquals(1, childScope.getDepth());
    assertEquals(0, parentScope.getDepth());
  }

  @Test
  public void testScopeIsBottom() {
    Node rootNode = new Node(Token.BLOCK);
    Scope bottomScope = new Scope(null, rootNode, null);

    assertFalse(bottomScope.isBottom());

    Scope bottomScopeWithNullThisType = new Scope(null, rootNode, null);

    assertTrue(bottomScopeWithNullThisType.isBottom());
  }

  @Test
  public void testScopeGetParent() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    assertEquals(parentScope, childScope.getParent());

    Scope grandChildScope = new Scope(childScope, new Node(Token.BLOCK));

    assertEquals(childScope, grandChildScope.getParent());
  }

  @Test
  public void testScopeGetGlobalScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope globalScope = new Scope(null, rootNode);

    Scope parentScope = new Scope(globalScope, new Node(Token.BLOCK));

    assertEquals(globalScope, parentScope.getGlobalScope());
  }

  @Test
  public void testScopeDeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));
    CompilerInput input = new CompilerInput("test.js", "test content");

    Var variable = childScope.declare(variableName, variableNameNode, variableType, input);

    assertEquals(variableName, variable.getName());
    assertEquals(variableNameNode, variable.getNameNode());
    assertEquals(variableType, variable.getType());
    assertEquals(childScope, variable.getScope());

    assertNull(parentScope.getVar(variableName));
  }

  @Test
  public void testScopeGetSlot() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null);

    assertEquals(variable, childScope.getSlot(variableName));
    assertNull(parentScope.getSlot(variableName));
  }

  @Test
  public void testScopeGetVar() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null);

    assertEquals(variable, childScope.getVar(variableName));
    assertNull(parentScope.getVar(variableName));
  }

  @Test
  public void testScopeGetVarRecursive() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope1 = new Scope(parentScope, new Node(Token.BLOCK));
    Scope childScope2 = new Scope(childScope1, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));

    Var variable = childScope2.declare(variableName, variableNameNode, variableType, null);

    assertEquals(variable, childScope2.getVar(variableName));
    assertEquals(variable, childScope1.getVar(variableName));
    assertEquals(variable, parentScope.getVar(variableName));
  }

  @Test
  public void testScopeIsDeclared() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null);

    assertTrue(childScope.isDeclared(variableName, true));
    assertTrue(childScope.isDeclared(variableName, false));
    assertFalse(parentScope.isDeclared(variableName, true));
    assertFalse(parentScope.isDeclared(variableName, false));
  }

  @Test
  public void testScopeUndeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variableName = "variableName";
    Node variableNameNode = new Node(Token.IDENT, variableName);
    JSType variableType = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null);

    childScope.undeclare(variable);

    assertNull(childScope.getVar(variableName));
  }

  @Test
  public void testScopeGetVars() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variable1Name = "variable1Name";
    Node variable1NameNode = new Node(Token.IDENT, variable1Name);
    JSType variable1Type = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));
    Var variable1 = childScope.declare(variable1Name, variable1NameNode, variable1Type, null);

    String variable2Name = "variable2Name";
    Node variable2NameNode = new Node(Token.IDENT, variable2Name);
    JSType variable2Type = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));
    Var variable2 = childScope.declare(variable2Name, variable2NameNode, variable2Type, null);

    List<Var> vars = new ArrayList<>();
    vars.add(variable1);
    vars.add(variable2);

    NodeTraversal traversal = new NodeTraversal(new Node(Token.BLOCK));
    childScope.getVars().forEachRemaining(vars::add);

    assertEquals(vars, childScope.getVars());
  }

  @Test
  public void testScopeGetVarCount() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    String variable1Name = "variable1Name";
    Node variable1NameNode = new Node(Token.IDENT, variable1Name);
    JSType variable1Type = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));
    Var variable1 = childScope.declare(variable1Name, variable1NameNode, variable1Type, null);

    String variable2Name = "variable2Name";
    Node variable2NameNode = new Node(Token.IDENT, variable2Name);
    JSType variable2Type = new FunctionType(new ObjectType("Function"), new ObjectType("Object"));
    Var variable2 = childScope.declare(variable2Name, variable2NameNode, variable2Type, null);

    assertEquals(2, childScope.getVarCount());
  }

  @Test
  public void testScopeIsLocal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));

    assertTrue(childScope.isLocal());
    assertFalse(parentScope.isLocal());
  }

  @Test
  public void testScopeIsGlobal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope globalScope = new Scope(null, rootNode);

    assertFalse(globalScope.isGlobal());
    assertTrue(globalScope.isBottom());
  }
}