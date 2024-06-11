package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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
  @InjectMocks
  private Scope scope;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    Scope parentScope = new Scope(null, rootNode);
    scope = new Scope(parentScope, rootNode);

    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithNullParentAndRootNode() {
    scope = new Scope(null, rootNode);

    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithNullParentAndType() {
    scope = new Scope(rootNode, null);

    assertNull(scope.getParent());
    assertNotNull(scope.thisType);
  }

  @Test
  public void testIsBottom() {
    scope = new Scope(rootNode, null);
    assertFalse(scope.isBottom());

    scope = new Scope(rootNode, null);
    scope.thisType = null;
    assertTrue(scope.isBottom());
  }

  @Test
  public void testGetDepth() {
    scope = new Scope(rootNode, null);
    assertEquals(0, scope.getDepth());

    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(parentScope, rootNode);
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testGetParent() {
    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(parentScope, rootNode);

    assertEquals(parentScope, scope.getParent());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    scope = new Scope(rootNode, null);
    assertNotNull(scope.thisType);

    scope = new Scope(rootNode, new ObjectType("Object"));
    assertEquals("Object", scope.thisType.getName());
  }

  @Test
  public void testDeclare() {
    String name = "foo";
    JSType type = new FunctionType(new ObjectType("Function"), ImmutableMap.of());

    scope.declare(name, null, type, input);

    Var fooVar = scope.getVar(name);
    assertNotNull(fooVar);
    assertEquals(name, fooVar.getName());
    assertEquals(type, fooVar.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareWithExistingVariable() {
    String name = "foo";
    scope.declare(name, null, new ObjectType("Object"), input);
    scope.declare(name, null, new ObjectType("Object"), input);
  }

  @Test
  public void testUndeclare() {
    String name = "foo";
    JSType type = new ObjectType("Object");

    scope.declare(name, null, type, input);
    Var fooVar = scope.getVar(name);

    scope.undeclare(fooVar);

    assertNull(scope.getVar(name));
  }

  @Test
  public void testGetSlot() {
    String name = "foo";
    JSType type = new ObjectType("Object");

    scope.declare(name, null, type, input);
    StaticSlot<JSType> slot = scope.getSlot(name);

    assertEquals(type, slot.getValue());
  }

  @Test
  public void testGetOwnSlot() {
    String name = "foo";
    JSType type = new ObjectType("Object");

    scope.declare(name, null, type, input);
    Var fooVar = scope.getVar(name);

    assertEquals(fooVar, scope.getOwnSlot(name));
  }

  @Test
  public void testIsDeclared() {
    String name = "foo";

    assertFalse(scope.isDeclared(name, false));

    scope.declare(name, null, new ObjectType("Object"), input);
    assertTrue(scope.isDeclared(name, false));

    assertFalse(scope.isDeclared(name, true));

    Scope parentScope = new Scope(rootNode, null);
    parentScope.declare(name, null, new ObjectType("Object"), input);
    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "foo1";
    String name2 = "foo2";
    JSType type1 = new ObjectType("Object1");
    JSType type2 = new ObjectType("Object2");

    scope.declare(name1, null, type1, input);
    scope.declare(name2, null, type2, input);

    Iterator<Var> vars = scope.getVars();

    Var foo1Var = vars.next();
    assertEquals(name1, foo1Var.getName());
    assertEquals(type1, foo1Var.getType());

    Var foo2Var = vars.next();
    assertEquals(name2, foo2Var.getName());
    assertEquals(type2, foo2Var.getType());

    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    String name1 = "foo1";
    String name2 = "foo2";
    JSType type1 = new ObjectType("Object1");
    JSType type2 = new ObjectType("Object2");

    scope.declare(name1, null, type1, input);
    scope.declare(name2, null, type2, input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    scope = new Scope(null, rootNode);
    assertTrue(scope.isGlobal());

    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(parentScope, rootNode);
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    scope = new Scope(null, rootNode);
    assertFalse(scope.isLocal());

    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(parentScope, rootNode);
    assertTrue(scope.isLocal());
  }
}