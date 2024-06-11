package com.google.javascript.jscomp; // make sure to import all necessary classes

import static org.junit.Assert.*;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * Test class for the Scope class.
 */
public class ScopeTest {

  private static final String TEST_VAR_NAME = "testVar";
  private static final String TEST_FUNCTION_NAME = "testFunction";

  private Scope rootScope;
  private Scope nestedScope;

  @Before
  public void setUp() {
    Node rootNode = Node.newFunctionNode(0);
    rootScope = new Scope(null, rootNode);

    Node nestedFunctionNode = Node.newFunctionNode(0);
    nestedScope = new Scope(rootScope, nestedFunctionNode);
  }

  @Test
  public void testCreation() {
    assertEquals(0, rootScope.getDepth());
    assertFalse(rootScope.isBottom());
    assertNull(rootScope.getParent());
    assertNull(rootScope.getTypeOfThis());

    assertEquals(1, rootScope.getVars().size());
    assertNull(rootScope.getVar(TEST_VAR_NAME));

    assertEquals(1, nestedScope.getDepth());
    assertFalse(nestedScope.isBottom());
    assertSame(rootScope, nestedScope.getParent());
    assertNull(nestedScope.getTypeOfThis());

    Iterator<Var> vars = nestedScope.getVars();
    assertFalse(vars.hasNext());
  }

  @Test
  public void testDeclareVariable() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);
    Var nestedVar = nestedScope.declare(TEST_VAR_NAME, null, JSType.NUMBER, null);

    assertEquals(2, rootScope.getVarCount());
    assertEquals(1, rootScope.getVars().size());
    assertSame(rootVar, rootScope.getVar(TEST_VAR_NAME));
    assertEquals(JSType.STRING, rootVar.getType());

    assertEquals(2, nestedScope.getVarCount());
    assertEquals(1, nestedScope.getVars().size());
    assertSame(nestedVar, nestedScope.getVar(TEST_VAR_NAME));
    assertEquals(JSType.NUMBER, nestedVar.getType());

    Var rootVarInNestedScope = nestedScope.getVar(TEST_VAR_NAME);
    assertNotNull(rootVarInNestedScope);
    assertSame(rootVar, rootVarInNestedScope);

    Var nestedVarInRootScope = rootScope.getVar(TEST_VAR_NAME);
    assertNotNull(nestedVarInRootScope);
    assertSame(nestedVar, nestedVarInRootScope);
  }

  @Test
  public void testUnddeclareVariable() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);
    Var nestedVar = nestedScope.declare(TEST_VAR_NAME, null, JSType.NUMBER, null);

    rootScope.undeclare(rootVar);

    assertEquals(1, rootScope.getVarCount());
    assertEquals(0, rootScope.getVars().size());
    assertNull(rootScope.getVar(TEST_VAR_NAME));

    assertEquals(1, nestedScope.getVarCount());
    assertEquals(1, nestedScope.getVars().size());
    assertSame(nestedVar, nestedScope.getVar(TEST_VAR_NAME));
  }

  @Test
  public void testGetSlot() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);

    Var testVar = rootScope.getSlot(TEST_VAR_NAME);
    assertSame(rootVar, testVar);
  }

  @Test
  public void testGetOwnSlot() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);

    Var testVar = rootScope.getOwnSlot(TEST_VAR_NAME);
    assertSame(rootVar, testVar);
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullSlot() {
    rootScope.getSlot(null);
  }

  @Test(expected = NullPointerException.class)
  public void testGetOwnNullSlot() {
    rootScope.getOwnSlot(null);
  }

  @Test
  public void testIsDeclared() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);
    Var nestedVar = nestedScope.declare(TEST_VAR_NAME, null, JSType.NUMBER, null);

    assertTrue(rootScope.isDeclared(TEST_VAR_NAME, false));
    assertTrue(rootScope.isDeclared(TEST_VAR_NAME, true));
    assertTrue(nestedScope.isDeclared(TEST_VAR_NAME, false));
    assertTrue(nestedScope.isDeclared(TEST_VAR_NAME, true));
  }

  @Test
  public void testGetVars() {
    Var rootVar = rootScope.declare(TEST_VAR_NAME, null, JSType.STRING, null);
    Var nestedVar = nestedScope.declare(TEST_VAR_NAME, null, JSType.NUMBER, null);

    Iterator<Var> rootVars = rootScope.getVars();
    Var rootVarFromIterator = rootVars.next();
    assertSame(rootVar, rootVarFromIterator);

    Iterator<Var> nestedVars = nestedScope.getVars();
    Var nestedVarFromIterator = nestedVars.next();
    assertSame(nestedVar, nestedVarFromIterator);
  }
}