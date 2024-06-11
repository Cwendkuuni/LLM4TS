package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopeTest {
  @Mock
  private ErrorReporter errorReporter;
  @Mock
  private CompilerInput input;
  @Mock
  private Node rootNode;
  @Mock
  private AbstractCompiler compiler;

  private Scope scope;
  private Scope parentScope;
  private Var variable;

  @Before
  public void setUp() {
    parentScope = new Scope(null, rootNode) {
      @Override
      public StaticScope<JSType> getParentScope() {
        return null;
      }
    };
    scope = new Scope(parentScope, rootNode);
    variable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0, input,
        false, null);
  }

  @Test
  public void testGetDepth() {
    assertEquals(1, scope.getDepth());
    assertEquals(0, parentScope.getDepth());
  }

  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
    assertTrue(new Scope(rootNode, null).isBottom());
  }

  @Test
  public void testGetParent() {
    assertSame(parentScope, scope.getParent());
    assertNull(parentScope.getParent());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType("Object");
    assertEquals(thisType, scope.getTypeOfThis());
    assertNull(parentScope.getTypeOfThis());
  }

  @Test
  public void testDeclare() {
    JSType type = JSType.getBasicTypes().BOOLEAN();
    scope.declare("name", null, type, input);
    assertEquals("name", variable.getName());
    assertEquals(type, variable.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareNullName() {
    scope.declare(null, null, JSType.UNDEFINED, input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareEmptyName() {
    scope.declare("", null, JSType.UNDEFINED, input);
  }

  @Test
  public void testGetVar() {
    scope.declare("name1", null, JSType.UNDEFINED, input);
    scope.declare("name2", null, JSType.STRING(), input);

    Var name1 = scope.getVar("name1");
    assertEquals("name1", name1.getName());
    assertEquals(JSType.UNDEFINED, name1.getType());

    Var name2 = scope.getVar("name2");
    assertEquals("name2", name2.getName());
    assertEquals(JSType.STRING(), name2.getType());

    assertNull(scope.getVar("nonExisting"));
  }

  @Test
  public void testIsDeclared() {
    scope.declare("name1", null, JSType.UNDEFINED, input);
    scope.declare("name2", null, JSType.STRING(), input);

    assertTrue(scope.isDeclared("name1"));
    assertTrue(scope.isDeclared("name2"));
    assertFalse(scope.isDeclared("nonExisting"));

    assertFalse(parentScope.isDeclared("name1"));
    assertFalse(parentScope.isDeclared("name2"));
    assertFalse(parentScope.isDeclared("nonExisting"));
  }

  @Test
  public void testGetVars() {
    List<Var> expectedVars = new ArrayList<>();
    expectedVars.add(variable);

    Scope scopeWithTwoVariables = new Scope(scope, rootNode);
    scopeWithTwoVariables.declare("name2", null, JSType.STRING(), input);

    List<Var> actualVars = new ArrayList<>();
    for (Var var : scopeWithTwoVariables.getVars()) {
      actualVars.add(var);
    }

    assertEquals(expectedVars, actualVars);
  }

  @Test
  public void testGetVarCount() {
    scope.declare("name1", null, JSType.UNDEFINED, input);
    scope.declare("name2", null, JSType.STRING(), input);

    assertEquals(2, scope.getVarCount());
    assertEquals(0, parentScope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(parentScope.isGlobal());
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(parentScope.isLocal());
    assertFalse(scope.isLocal());
  }

  @Test
  public void testGetOwnSlot() {
    scope.declare("name", null, JSType.UNDEFINED, input);
    Var expectedVar = scope.getOwnSlot("name");

    Var actualVar = scope.getOwnSlot("name");
    assertEquals(expectedVar, actualVar);
  }

  @Test
  public void testGetSlot() {
    Scope outerScope = new Scope(parentScope, rootNode);
    outerScope.declare("outerName", null, JSType.UNDEFINED, input);

    scope.declare("name", null, JSType.UNDEFINED, input);

    Var expectedOuterVar = outerScope.getOwnSlot("outerName");
    Var expectedVar = scope.getSlot("name");

    Var outerVar = scope.getSlot("outerName");
    Var var = scope.getSlot("name");

    assertEquals(expectedOuterVar, outerVar);
    assertEquals(expectedVar, var);
  }

  @Test
  public void testUndeclare() {
    scope.declare("name", null, JSType.UNDEFINED, input);
    scope.undeclare(variable);

    assertFalse(scope.isDeclared("name"));
  }

  @Test
  public void testMultipleDeclarations() {
    scope.declare("name1", null, JSType.UNDEFINED, input);
    scope.declare("name1", null, JSType.INT(), input);

    Var name1Var = scope.getVar("name1");
    assertEquals(JSType.INT(), name1Var.getType());
  }

  @Test
  public void testGetInputName() {
    assertEquals("<non-file>", variable.getInputName());
  }

  @Test
  public void testIsNoShadow() {
    JSDocInfo info = new JSDocInfo("name", null, null, null, null, false, null, null);
    variable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0, input,
        false, info);

    assertTrue(variable.isNoShadow());
  }

  @Test
  public void testIsDefine() {
    JSDocInfo info = new JSDocInfo("name", null, null, null, null, false, null,
        new String[]{"@define"});
    variable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0, input,
        true, info);

    assertTrue(variable.isDefine());
  }

  @Test
  public void testGetBleedingFunction() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null);
    variable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0, input,
        false, null);

    assertTrue(variable.isBleedingFunction());
  }

  @Test
  public void testGetInitialValue() {
    Node assignNode = Node.newAssignNode(Token.ASSIGN, null, null, null, null,
        null, null, null, null, null, null, null, null);
    Node varNode = Node.newVarNode(Token.IDENT, "name", null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null);
    Node valueNode = Node.newNumberNode(Token.NUMBER, 42, null);

    Node parentNode = Node.newAssignNode(Token.ASSIGN, null, null, null, null,
        null, null, null, null, null, assignNode, null, null, null, null, null);
    Node rootNode = Node.newBlockNode(Token.BLOCK, null, null, null,
        Arrays.asList(parentNode), null, null, null, null, null, null, null,
        null, null, null, null, null);

    scope = new Scope(null, rootNode);
    scope.declare("name", null, JSType.UNDEFINED, input);

    variable = scope.getVar("name");

    Node expectedInitialValue = functionNode;
    Node actualInitialValue = variable.getInitialValue();

    assertEquals(expectedInitialValue, actualInitialValue);
  }

  @Test
  public void testGetNameNode() {
    Node nameNode = Node.newVarNode(Token.IDENT, "name", null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null);

    scope.declare("name", nameNode, JSType.UNDEFINED, input);

    assertEquals(nameNode, variable.getNameNode());
  }

  @Test
  public void testGetJSDocInfo() {
    JSDocInfo info = new JSDocInfo("name", null, null, null, null, false, null,
        null);
    variable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0, input,
        false, info);

    assertEquals(info, variable.getJSDocInfo());
  }

  @Test
  public void testSetType() {
    JSType expectedType = JSType.getBasicTypes().INT();
    scope.declare("name", null, JSType.UNDEFINED, input);

    variable.setType(expectedType);

    assertEquals(expectedType, variable.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetTypeInferred() {
    JSType expectedType = JSType.getBasicTypes().INT();
    scope.declare("name", null, JSType.UNDEFINED, input);

    variable.setType(expectedType);

    // Throws IllegalStateException since variable is inferred
    variable.setType(JSType.STRING());
  }

  @Test
  public void testResolveType() {
    JSType expectedType = JSType.getBasicTypes().INT();

    JSType inputType = JSType.getBasicTypes().STRING();

    scope.declare("name", null, JSType.UNDEFINED, input);

    variable.resolveType(errorReporter);

    assertEquals(expectedType, variable.getType());
  }

  @Test
  public void testIsTypeInferred() {
    assertTrue(variable.isTypeInferred());
  }

  @Test
  public void testEquals() {
    Var otherVariable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0,
        input, false, null);

    assertTrue(variable.equals(otherVariable));
  }

  @Test
  public void testHashCode() {
    Var otherVariable = new Var(true, "name", null, JSType.UNDEFINED, scope, 0,
        input, false, null);

    assertEquals(variable.hashCode(), otherVariable.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("Scope.Var name", variable.toString());
  }

  @Test
  public void testJoin() {
    Map<String, JSType> map1 = new LinkedHashMap<>();
    map1.put("name1", JSType.getBasicTypes().INT());
    map1.put("name2", JSType.getBasicTypes().STRING());

    Map<String, JSType> map2 = new LinkedHashMap<>();
    map2.put("name2", JSType.getBasicTypes().BOOLEAN());
    map2.put("name3", JSType.getBasicTypes().INT());

    Map<String, JSType> expectedResult = new LinkedHashMap<>();
    expectedResult.putAll(map1);
    expectedResult.putAll(map2);

    Multimap<String, JSType> result = Scope.join(map1, map2);

    Map<String, JSType> actualResult = new LinkedHashMap<>();
    for (Map.Entry<String, Collection<JSType>> entry : result.asMap().entrySet()) {
      actualResult.put(entry.getKey(), Sets.newHashSet(entry.getValue().toArray(new JSType[0]))
          .stream()
          .map(jst -> jst.getLeastSupertype(errorReporter)).collect(Collectors.toList())
          .get(0));
    }

    assertEquals(expectedResult, actualResult);
  }
}