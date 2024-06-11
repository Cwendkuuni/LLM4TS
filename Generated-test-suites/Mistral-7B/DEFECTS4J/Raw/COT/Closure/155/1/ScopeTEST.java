package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticTypeRegistry;
import com.google.javascript.rhino.nodes.FunctionNode;
import com.google.javascript.rhino.nodes.VariableNode;
import com.google.javascript.rhino.util.CompilationError;
import com.google.javascript.rhino.util.SourceFile;
import com.google.javascript.rhino.util.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScopeTest {
  private static final SourceFile SOURCE_FILE = new SourceFile("MyScript.js");
  private StaticTypeRegistry typeRegistry;
  private ErrorReporter errorReporter;
  private Scope globalScope;
  private Scope localScope;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    typeRegistry = mock(StaticTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);

    globalScope = new Scope(SOURCE_FILE.getProgram(), typeRegistry) {
      @Override
      public StaticScope<JSType> getParentScope() {
        return null;
      }
    };

    localScope = new Scope(new FunctionNode(new NodeTraversal(typeRegistry, errorReporter, null)), globalScope) {
      @Override
      public StaticScope<JSType> getParentScope() {
        return globalScope;
      }
    };
  }

  @Test
  public void testCreateGlobalScope() {
    assertEquals(0, globalScope.getDepth());
    assertNotNull(globalScope.getTypeOfThis());
    assertTrue(globalScope.isBottom());
  }

  @Test
  public void testCreateLocalScope() {
    FunctionNode functionNode = new FunctionNode(new NodeTraversal(typeRegistry, errorReporter, null));
    Scope functionScope = new Scope(functionNode, globalScope);

    assertEquals(1, functionScope.getDepth());
    assertNotNull(functionScope.getTypeOfThis());
    assertFalse(functionScope.isBottom());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = new Node(Token.BLOCK, Collections.emptyList());
    Scope emptyScope = new Scope(rootNode, null);

    assertEquals(0, emptyScope.getDepth());
    assertNotNull(emptyScope.getTypeOfThis());
    assertTrue(emptyScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);
    JSType type = new ObjectType("String");

    Var declaredVar = localScope.declare(name, nameNode, type, new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    assertEquals(name, declaredVar.getName());
    assertEquals(type, declaredVar.getType());
    assertEquals(localScope, declaredVar.getScope());
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareVariableTwice() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);
    JSType type = new ObjectType("String");

    localScope.declare(name, nameNode, type, new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));
    localScope.declare(name, nameNode, type, new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));
  }

  @Test
  public void testUndelecareVariable() {
    String name = "testVariable";
    Var declaredVar = localScope.declare(name, new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    localScope.undeclare(declaredVar);

    assertNull(localScope.getVar(name));
  }

  @Test
  public void testGetVarByName() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);

    Var declaredVar = localScope.declare(name, nameNode, new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    Var retrievedVar = localScope.getVar(name);

    assertEquals(declaredVar, retrievedVar);
  }

  @Test
  public void testGetVarByNameInParentScope() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);

    Var parentDeclaredVar = globalScope.declare(name, nameNode, new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    assertNull(localScope.getVar(name));
    assertEquals(parentDeclaredVar, globalScope.getVar(name));
  }

  @Test
  public void testGetVarByParentScope() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);

    Var parentDeclaredVar = globalScope.declare(name, nameNode, new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    Var retrievedVar = localScope.getParent().getVar(name);

    assertEquals(parentDeclaredVar, retrievedVar);
  }

  @Test
  public void testIsDeclared() {
    String name = "testVariable";

    Var declaredVar = localScope.declare(name, new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    assertTrue(localScope.isDeclared(name, false));
    assertTrue(localScope.isDeclared(name, true));

    Var nonDeclaredVar = new Var(false, name, null, null, null, -1, null, false, null);

    assertFalse(localScope.isDeclared(name, false));
    assertFalse(localScope.isDeclared(name, true));

    Var parentDeclaredVar = globalScope.declare(name, new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    assertFalse(localScope.isDeclared(name, false));
    assertTrue(localScope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "testVariable1";
    String name2 = "testVariable2";

    Var var1 = localScope.declare(name1, new VariableNode(name1, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name1, SOURCE_FILE, new SourcePosition(0, 0)));
    Var var2 = localScope.declare(name2, new VariableNode(name2, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("Number"), new CompilerInput(name2, SOURCE_FILE, new SourcePosition(0, 0)));

    Iterator<Var> vars = localScope.getVars();

    assertEquals(2, vars.size());

    Var retrievedVar1 = vars.next();
    Var retrievedVar2 = vars.next();

    assertEquals(var1, retrievedVar1);
    assertEquals(var2, retrievedVar2);
  }

  @Test
  public void testGetVarCount() {
    String name1 = "testVariable1";
    String name2 = "testVariable2";

    Var var1 = localScope.declare(name1, new VariableNode(name1, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name1, SOURCE_FILE, new SourcePosition(0, 0)));
    Var var2 = localScope.declare(name2, new VariableNode(name2, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("Number"), new CompilerInput(name2, SOURCE_FILE, new SourcePosition(0, 0)));

    assertEquals(2, localScope.getVarCount());
  }

  @Test
  public void testGetSlot() {
    String name = "testVariable";

    Var declaredVar = localScope.declare(name, new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    StaticSlot<JSType> slot = localScope.getSlot(name);

    assertEquals(declaredVar, slot);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "testVariable";

    Var declaredVar = localScope.declare(name, new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0), new ObjectType("String"), new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)));

    StaticSlot<JSType> slot = declaredVar;

    assertEquals(slot, localScope.getOwnSlot(name));
  }

  @Test(expected = NullPointerException.class)
  public void testGetVarByNullName() {
    localScope.getVar(null);
  }

  @Test(expected = NullPointerException.class)
  public void testGetSlotByNullName() {
    localScope.getSlot(null);
  }

  @Test(expected = NullPointerException.class)
  public void testGetOwnSlotByNullName() {
    localScope.getOwnSlot(null);
  }

  @Test(expected = CompilationError.class)
  public void testDeclareVariableWithError() {
    String name = "testVariable";
    Node nameNode = new VariableNode(name, null, null, null, null, null, null, SOURCE_FILE, 0, 0);
    JSType type = new ObjectType("String");

    localScope.declare(name, nameNode, type, new CompilerInput(name, SOURCE_FILE, new SourcePosition(0, 0)), errorReporter);

    verify(errorReporter).reportError(eq(SOURCE_FILE), isA(CompilationError.class), anyInt(), anyInt());
  }
}