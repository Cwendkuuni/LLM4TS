package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSFunction;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticScope.Var;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {
  private Scope scope;
  private Node rootNode;
  private ErrorReporter errorReporter;
  private ObjectType thisType;

  @Before
  public void setUp() {
    thisType = new ObjectType("Object");
    rootNode = new JSFunction("test", new FunctionType(null, new ObjectType[]{}), null);
    errorReporter = new ErrorReporter();
    scope = new Scope(null, rootNode) {
      @Override
      public ObjectType getTypeOfThis() {
        return thisType;
      }
    };
  }

  @Test
  public void testNewScopeWithParentAndRootNode() {
    Scope parentScope = new Scope(null, new JSFunction("parent", null, null));
    Scope newScope = new Scope(parentScope, rootNode);
    assertFalse(newScope.isBottom());
    assertEquals(parentScope.depth + 1, newScope.depth);
  }

  @Test
  public void testNewGlobalScope() {
    Scope globalScope = new Scope(null, null, thisType);
    assertTrue(globalScope.isBottom());
    assertEquals(0, globalScope.depth);
  }

  @Test
  public void testNewEmptyScope() {
    Scope emptyScope = new Scope(rootNode, null);
    assertTrue(emptyScope.isBottom());
    assertEquals(0, emptyScope.depth);
  }

  @Test
  public void testGetParent() {
    Scope parentScope = new Scope(null, new JSFunction("parent", null, null));
    Scope childScope = new Scope(parentScope, rootNode);
    assertEquals(parentScope, childScope.getParent());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testDeclare() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);
    assertNotNull(var);
    assertTrue(var.isLocal());
    assertEquals(name, var.getName());
    assertEquals(JSTypeNative.STRING, var.getType());
    assertNull(var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertNull(var.getParentNode());
    assertNull(var.getInitialValue());
    assertFalse(var.isBleedingFunction());
    assertNull(var.getScope());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
  }

  @Test
  public void testDeclareWithJSDocInfo() {
    String name = "name";
    JSDocInfo info = new JSDocInfo();
    info.setName("Name");
    info.setDescription("description");
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null, null, info, null);
    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(JSTypeNative.STRING, var.getType());
    assertSame(info, var.getJSDocInfo());
    assertFalse(var.isDefine());
    assertNull(var.getParentNode());
    assertNull(var.getInitialValue());
    assertFalse(var.isBleedingFunction());
    assertNull(var.getScope());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
  }

  @Test
  public void testDeclareDefine() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null, null, null, new CompilerInput("test.js"));
    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(JSTypeNative.STRING, var.getType());
    assertNull(var.getJSDocInfo());
    assertTrue(var.isDefine());
    assertNull(var.getParentNode());
    assertNull(var.getInitialValue());
    assertFalse(var.isBleedingFunction());
    assertNull(var.getScope());
    assertNull(var.getInputName());
    assertFalse(var.isNoShadow());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareNullName() {
    scope.declare(null, new Node("NameNode"), JSTypeNative.STRING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareEmptyName() {
    scope.declare("", new Node("NameNode"), JSTypeNative.STRING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareNullNameNode() {
    scope.declare("name", null, JSTypeNative.STRING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareNullType() {
    scope.declare("name", new Node("NameNode"), null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareNullInput() {
    scope.declare("name", new Node("NameNode"), JSTypeNative.STRING, null, null, null, null);
  }

  @Test
  public void testGetVar() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = scope.declare(name1, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name2, new Node("NameNode2"), JSTypeNative.NUMBER, null);

    Var getVar1 = scope.getVar(name1);
    assertEquals(var1, getVar1);

    Var getVar2 = scope.getVar(name2);
    assertEquals(var2, getVar2);

    Var getNullVar = scope.getVar("nonexistentVarName");
    assertNull(getNullVar);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);
    StaticSlot<JSType> slot = scope.getOwnSlot(name);
    assertEquals(var, slot);
  }

  @Test
  public void testGetSlot() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);
    StaticSlot<JSType> slot = scope.getSlot(name);
    assertEquals(var, slot);
  }

  @Test
  public void testIsDeclared() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);
    assertTrue(scope.isDeclared(name, true));
    assertFalse(scope.isDeclared("nonexistentVarName", true));
  }

  @Test
  public void testIsDeclaredWithRecursion() {
    String name = "name";
    Var var1 = scope.declare(name, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name, new Node("NameNode2"), JSTypeNative.NUMBER, null);
    assertTrue(scope.isDeclared(name, true));
    assertTrue(scope.isDeclared(name, false));
  }

  @Test
  public void testUndelecare() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);
    scope.undeclare(var);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testGetVars() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = scope.declare(name1, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name2, new Node("NameNode2"), JSTypeNative.NUMBER, null);

    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    Var var1FromIter = vars.next();
    assertEquals(var1, var1FromIter);

    assertTrue(vars.hasNext());
    Var var2FromIter = vars.next();
    assertEquals(var2, var2FromIter);

    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = scope.declare(name1, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name2, new Node("NameNode2"), JSTypeNative.NUMBER, null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
  }

  @Test
  public void testResolveType() {
    Var var = scope.declare("name", new Node("NameNode"), JSTypeNative.INT, null);
    JSType type = var.getType();
    Preconditions.checkState(type != null);
    var.resolveType(errorReporter);
    assertSame(JSTypeNative.STRING, var.getType());
  }

  @Test
  public void testIsTypeInferred() {
    Var var = scope.declare("name", new Node("NameNode"), null, null);
    assertFalse(var.isTypeInferred());
  }

  @Test
  public void testEquals() {
    String name1 = "name1";
    String name2 = "name2";
    Var var1 = scope.declare(name1, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name2, new Node("NameNode2"), JSTypeNative.NUMBER, null);
    Var var3 = scope.declare(name1, new Node("NameNode1"), JSTypeNative.STRING, null);

    assertTrue(var1.equals(var3));
    assertFalse(var1.equals(var2));
    assertFalse(var1.equals(null));
  }

  @Test
  public void testHashCode() {
    String name = "name";
    Var var1 = scope.declare(name, new Node("NameNode1"), JSTypeNative.STRING, null);
    Var var2 = scope.declare(name, new Node("NameNode2"), JSTypeNative.STRING, null);

    int hashCode1 = var1.hashCode();
    int hashCode2 = var2.hashCode();

    assertEquals(hashCode1, hashCode1);
    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testToString() {
    String name = "name";
    Var var = scope.declare(name, new Node("NameNode"), JSTypeNative.STRING, null);

    assertEquals("Scope.Var name", var.toString());
  }
}