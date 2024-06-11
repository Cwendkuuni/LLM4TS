import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.ScopeTestUtils;

import java.util.Arrays;
import java.util.List;

public class ScopeTest {

  @org.junit.Test
  public void testScopeCreation() {
    // Test creating a local scope
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    assertEquals(0, scope.getDepth());
    assertEquals(parent, scope.getParent());
    assertNull(scope.getTypeOfThis());

    // Test creating a global scope
    rootNode = new Node(Token.SCRIPT);
    parent = new Scope(rootNode, null);
    scope = new Scope(rootNode, parent.getTypeRegistry());

    assertEquals(0, scope.getDepth());
    assertEquals(null, scope.getParent());
    assertNotNull(scope.getTypeOfThis());
  }

  @org.junit.Test
  public void testScopeDeclareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var declaredVariable = scope.declare(variableName, null, variableType, input);

    assertEquals(variableName, declaredVariable.getName());
    assertEquals(variableType, declaredVariable.getType());
    assertEquals(scope, declaredVariable.getScope());
    assertEquals(0, declaredVariable.getIndex());
    assertFalse(declaredVariable.isDefine());
    assertNull(declaredVariable.getJSDocInfo());
    assertFalse(declaredVariable.isTypeInferred());
    assertNull(declaredVariable.getInputName());
    assertFalse(declaredVariable.isNoShadow());
  }

  @org.junit.Test
  public void testScopeDeclareVariableWithJSDocInfo() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");
    JSDocInfo jsDocInfo = new JSDocInfo("@param {number} testVar");

    Var declaredVariable = scope.declare(variableName, null, variableType, input, jsDocInfo);

    assertEquals(variableName, declaredVariable.getName());
    assertEquals(variableType, declaredVariable.getType());
    assertEquals(scope, declaredVariable.getScope());
    assertEquals(0, declaredVariable.getIndex());
    assertTrue(declaredVariable.isDefine());
    assertEquals(jsDocInfo, declaredVariable.getJSDocInfo());
    assertFalse(declaredVariable.isTypeInferred());
    assertNull(declaredVariable.getInputName());
    assertFalse(declaredVariable.isNoShadow());
  }

  @org.junit.Test
  public void testScopeDeclareVariableWithTypeInferred() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var declaredVariable = scope.declare(variableName, null, null, input, true);

    assertEquals(variableName, declaredVariable.getName());
    assertNull(declaredVariable.getType());
    assertEquals(scope, declaredVariable.getScope());
    assertEquals(0, declaredVariable.getIndex());
    assertFalse(declaredVariable.isDefine());
    assertNull(declaredVariable.getJSDocInfo());
    assertTrue(declaredVariable.isTypeInferred());
    assertNull(declaredVariable.getInputName());
    assertFalse(declaredVariable.isNoShadow());
  }

  @org.junit.Test
  public void testScopeDeclareAndGetVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var declaredVariable = scope.declare(variableName, null, variableType, input);

    Var getVariable = scope.getVar(variableName);

    assertEquals(declaredVariable, getVariable);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testScopeGetUndeclaredVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";

    Var getVariable = scope.getVar(variableName);

    assertNull(getVariable);
  }

  @org.junit.Test
  public void testScopeGetParentVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var parentVariable = parentScope.declare(variableName, null, variableType, input);

    Var childVariable = childScope.getVar(variableName);

    assertEquals(parentVariable, childVariable);
  }

  @org.junit.Test
  public void testScopeGetParentVariableWhenUndeclaredInChildScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, rootNode);

    String variableName = "testVar";

    Var childVariable = childScope.getVar(variableName);

    assertNull(childVariable);
  }

  @org.junit.Test
  public void testScopeGetDeclaredVariables() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variable1 = "testVar1";
    String variable2 = "testVar2";

    JSType variableType1 = JSType.INT;
    JSType variableType2 = JSType.STRING;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var variable1Declared = scope.declare(variable1, null, variableType1, input);
    Var variable2Declared = scope.declare(variable2, null, variableType2, input);

    List<Var> variables = Arrays.asList(variable1Declared, variable2Declared);

    Iterator<Var> iterator = scope.getVars();

    List<Var> variablesFromIterator = ScopeTestUtils.getListFromIterator(iterator);

    assertEquals(variables, variablesFromIterator);
  }

  @org.junit.Test
  public void testScopeUndelecareVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var variable = scope.declare(variableName, null, variableType, input);

    scope.undeclare(variable);

    Var getVariable = scope.getVar(variableName);

    assertNull(getVariable);
  }

  @org.junit.Test
  public void testScopeGetVariableType() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVar";
    JSType variableType = JSType.INT;
    CompilerInput input = ScopeTestUtils.createCompilerInput("Test.js");

    Var variable = scope.declare(variableName, null, variableType, input);

    JSType variableTypeFromScope = scope.getVar(variableName).getType();

    assertEquals(variableType, variableTypeFromScope);
  }

  @org.junit.Test
  public void testScopeGetVariableTypeWhenUndeclared() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String variableName = "testVar";

    JSType variableTypeFromScope = scope.getVar(variableName).getType();

    assertNull(variableTypeFromScope);
  }

  @org.junit.Test
  public void testScopeJoin() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope1 = new Scope(parentScope, rootNode);
    Scope childScope2 = new Scope(parentScope, rootNode);

    ImmutableMap<String, Var> parentScopeVars = ImmutableMap.of();
    ImmutableMap<String, Var> childScope1Vars = ImmutableMap.of(
        "testVar1", new Var("", "testVar1", null, JSType.INT, childScope1, 0, null, false, null)
    );
    ImmutableMap<String, Var> childScope2Vars = ImmutableMap.of(
        "testVar2", new Var("", "testVar2", null, JSType.STRING, childScope2, 1, null, false, null)
    );

    Scope joinedScope = new Scope(parentScope, rootNode, parentScopeVars, childScope1Vars, childScope2Vars);

    Iterator<Var> parentScopeIterator = parentScope.getVars().iterator();
    Iterator<Var> childScope1Iterator = childScope1.getVars().iterator();
    Iterator<Var> childScope2Iterator = childScope2.getVars().iterator();
    Iterator<Var> joinedScopeIterator = joinedScope.getVars().iterator();

    List<Var> parentScopeVarsList = ScopeTestUtils.getListFromIterator(parentScopeIterator);
    List<Var> childScope1VarsList = ScopeTestUtils.getListFromIterator(childScope1Iterator);
    List<Var> childScope2VarsList = ScopeTestUtils.getListFromIterator(childScope2Iterator);
    List<Var> joinedScopeVarsList = ScopeTestUtils.getListFromIterator(joinedScopeIterator);

    assertEquals(parentScopeVarsList, joinedScopeVarsList.subList(0, parentScopeVarsList.size()));
    assertEquals(childScope1VarsList, joinedScopeVarsList.subList(parentScopeVarsList.size(), parentScopeVarsList.size() + childScope1VarsList.size()));
    assertEquals(childScope2VarsList, joinedScopeVarsList.subList(parentScopeVarsList.size() + childScope1VarsList.size(), parentScopeVarsList.size() + childScope1VarsList.size() + childScope2VarsList.size()));
  }

  @org.junit.Test
  public void testScopeIsGlobal() {
    Node rootNode = new Node(Token.SCRIPT);
    Scope scope = new Scope(rootNode, null);

    assertTrue(scope.isGlobal());
  }

  @org.junit.Test
  public void testScopeIsLocal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope parent = new Scope(null, rootNode);
    Scope scope = new Scope(parent, rootNode);

    assertFalse(scope.isGlobal());
  }
}