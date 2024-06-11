package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  @Test
  public void testCreateScope() {
    Node rootNode = new Node(Token.BLOCK, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    assertEquals("rootScope", parentScope.getDepth(), 0);
    assertEquals("rootScope", scopeUnderTest.getDepth(), 1);
    assertNull("rootScope", parentScope.getParent());
    assertNotNull("scopeUnderTest", scopeUnderTest.getParent());
    assertEquals("rootScope", parentScope.getClass().getName(), parentScope.getClass().getSimpleName());
    assertEquals("scopeUnderTest", scopeUnderTest.getClass().getName(), scopeUnderTest.getClass().getSimpleName());
  }

  @Test
  public void testCreateGlobalScope() {
    Node rootNode = new Node(Token.SCRIPT, "", 0);
    Scope globalScope = new Scope(rootNode, null);

    assertEquals("globalScope", globalScope.getDepth(), 0);
    assertNull("globalScope", globalScope.getParent());
    assertNotNull("globalScope", globalScope.getTypeOfThis());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope emptyScope = new Scope(rootNode, null);

    assertEquals("emptyScope", emptyScope.getDepth(), 0);
    assertNull("emptyScope", emptyScope.getTypeOfThis());
    assertTrue("emptyScope", emptyScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    assertEquals("declaredVariable", declaredVariable.getName(), variableName);
    assertEquals("declaredVariable", declaredVariable.isDefine(), false);
    assertNull("declaredVariable", declaredVariable.getType());
    assertNull("declaredVariable", declaredVariable.getParentNode());
    assertNull("declaredVariable", declaredVariable.getScope());
    assertFalse("declaredVariable", declaredVariable.isBleedingFunction());
    assertTrue("declaredVariable", declaredVariable.isLocal());
    assertFalse("declaredVariable", declaredVariable.isGlobal());
    assertFalse("declaredVariable", declaredVariable.isExtern());
    assertFalse("declaredVariable", declaredVariable.isConst());
    assertNull("declaredVariable", declaredVariable.getInitialValue());
    assertEquals("declaredVariable", declaredVariable.getIndex(), 0);
    assertNull("declaredVariable", declaredVariable.getJSDocInfo());
    assertTrue("declaredVariable", declaredVariable.isTypeInferred());

    Var variable = scopeUnderTest.getVar(variableName);
    assertEquals("variable", variable, declaredVariable);
  }

  @Test
  public void testDeclareVariableWithInfo() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    JSDocInfo docInfo = new JSDocInfo();
    docInfo.setName("myVariableName");
    docInfo.setType("MyType");
    docInfo.setDescription("My Description");

    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null, docInfo);

    assertEquals("declaredVariable", declaredVariable.getName(), variableName);
    assertEquals("declaredVariable", declaredVariable.isDefine(), false);
    assertNull("declaredVariable", declaredVariable.getType());
    assertNull("declaredVariable", declaredVariable.getParentNode());
    assertNull("declaredVariable", declaredVariable.getScope());
    assertFalse("declaredVariable", declaredVariable.isBleedingFunction());
    assertTrue("declaredVariable", declaredVariable.isLocal());
    assertFalse("declaredVariable", declaredVariable.isGlobal());
    assertFalse("declaredVariable", declaredVariable.isExtern());
    assertFalse("declaredVariable", declaredVariable.isConst());
    assertNull("declaredVariable", declaredVariable.getInitialValue());
    assertEquals("declaredVariable", declaredVariable.getIndex(), 0);
    assertEquals("declaredVariable", declaredVariable.getJSDocInfo(), docInfo);
    assertTrue("declaredVariable", declaredVariable.isTypeInferred());

    Var variable = scopeUnderTest.getVar(variableName);
    assertEquals("variable", variable, declaredVariable);
  }

  @Test
  public void testGetVar() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    Var variable = scopeUnderTest.getVar(variableName);
    assertEquals("variable", variable, declaredVariable);
  }

  @Test
  public void testGetVarNull() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    Var variable = scopeUnderTest.getVar("nonExistentVariable");
    assertNull("variable", variable);
  }

  @Test
  public void testGetGlobalVar() {
    Node rootNode = new Node(Token.SCRIPT, "", 0);
    Scope globalScope = new Scope(rootNode, null);

    String variableName = "myVariable";
    Var declaredVariable = globalScope.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    Var variable = globalScope.getVar(variableName);
    assertEquals("variable", variable, declaredVariable);
  }

  @Test
  public void testGetOwnSlot() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    StaticSlot<JSType> slot = scopeUnderTest.getOwnSlot(variableName);
    assertEquals("slot", slot, declaredVariable);
  }

  @Test
  public void testGetSlot() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    StaticSlot<JSType> slot = scopeUnderTest.getSlot(variableName);
    assertEquals("slot", slot, declaredVariable);
  }

  @Test
  public void testGetSlotNull() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    StaticSlot<JSType> slot = scopeUnderTest.getSlot("nonExistentVariable");
    assertNull("slot", slot);
  }

  @Test
  public void testGetVars() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    String variable2Name = "variable2";
    Var variable2 = scopeUnderTest.declare(variable2Name, new Node(Token.VAR, variable2Name, 0), JSType.ANY, null);

    ImmutableList<Var> vars = ImmutableList.copyOf(scopeUnderTest.getVars());

    assertEquals("vars", vars.size(), 2);
    assertTrue("vars", vars.contains(variable1));
    assertTrue("vars", vars.contains(variable2));
  }

  @Test
  public void testGetVarCount() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    String variable2Name = "variable2";
    Var variable2 = scopeUnderTest.declare(variable2Name, new Node(Token.VAR, variable2Name, 0), JSType.ANY, null);

    assertEquals("varCount", scopeUnderTest.getVarCount(), 2);
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    String variable2Name = "variable2";
    Var variable2 = scopeUnderTest.declare(variable2Name, new Node(Token.VAR, variable2Name, 0), JSType.ANY, null);

    assertTrue("isDeclared variable1", scopeUnderTest.isDeclared(variable1Name, false));
    assertTrue("isDeclared variable2", scopeUnderTest.isDeclared(variable2Name, false));

    assertFalse("isDeclared nonExistentVariable1", scopeUnderTest.isDeclared("nonExistentVariable1", false));
    assertFalse("isDeclared nonExistentVariable2", scopeUnderTest.isDeclared("nonExistentVariable2", false));

    assertTrue("isDeclared variable1 recursive", scopeUnderTest.isDeclared(variable1Name, true));
    assertTrue("isDeclared variable2 recursive", scopeUnderTest.isDeclared(variable2Name, true));

    assertTrue("isDeclared global variable", globalScope.isDeclared("myVariable", false));
    assertTrue("isDeclared global variable recursive", globalScope.isDeclared("myVariable", true));
  }

  @Test
  public void testIsDeclaredGlobal() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = globalScope.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    assertTrue("isDeclaredGlobal variable", scopeUnderTest.isDeclared(variableName, false));
    assertTrue("isDeclaredGlobal variable recursive", scopeUnderTest.isDeclared(variableName, true));
  }

  @Test
  public void testIsLocal() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    assertFalse("isLocal globalVariable", globalScope.isLocal());
    assertTrue("isLocal variable1", scopeUnderTest.isLocal());

    Var globalVariable = globalScope.declare("globalVariable", new Node(Token.VAR, "globalVariable", 0), JSType.ANY, null);

    assertFalse("isLocal globalVariable", scopeUnderTest.isLocal());
  }

  @Test
  public void testIsGlobal() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    assertFalse("isGlobal variable1", scopeUnderTest.isGlobal());
    assertFalse("isGlobal globalVariable", globalScope.isGlobal());

    Var globalVariable = globalScope.declare("globalVariable", new Node(Token.VAR, "globalVariable", 0), JSType.ANY, null);

    assertTrue("isGlobal globalVariable", globalVariable.isGlobal());
  }

  @Test
  public void testIsExtern() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variable1Name = "variable1";
    Var variable1 = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, null);

    CompilerInput input = new CompilerInput("test.js", 0, new Node(Token.SCRIPT, "", 0));
    Var externVariable = scopeUnderTest.declare(variable1Name, new Node(Token.VAR, variable1Name, 0), JSType.ANY, input, null);

    assertFalse("isExtern variable1", variable1.isExtern());
    assertTrue("isExtern externVariable", externVariable.isExtern());
  }

  @Test
  public void testIsConst() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String constVariableName = "constVariable";
    Node constNode = new Node(Token.ASSIGN, "", 0);
    constNode.setFirstChild(new Node(Token.NAME, constVariableName, 0));
    constNode.setLastChild(new Node(Token.VALUE, "42", 0));

    Var constVariable = scopeUnderTest.declare(constVariableName, constNode, JSType.NUMBER, null);

    assertTrue("isConst constVariable", constVariable.isConst());
  }

  @Test
  public void testUndelecareVariable() {
    Node rootNode = new Node(Token.FUNCTION, "", 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    String variableName = "myVariable";
    Var declaredVariable = scopeUnderTest.declare(variableName, new Node(Token.VAR, variableName, 0), JSType.ANY, null);

    scopeUnderTest.undeclare(declaredVariable);

    assertFalse("undeclaredVariable exists", scopeUnderTest.getVar(variableName) != null);
  }
}