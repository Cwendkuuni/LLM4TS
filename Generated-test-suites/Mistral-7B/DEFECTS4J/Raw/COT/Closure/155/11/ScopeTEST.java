package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeRegistry;

import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScopeTest {

  @Mock private Node rootNode;
  @Mock private AbstractCompiler compiler;
  @Mock private StaticScope<JSType> parentScope;
  @Mock private NodeTraversal nodeTraversal;
  @Mock private ErrorReporter errorReporter;
  @Mock private CompilerInput input;
  @Mock private JSDocInfo info;

  private Scope scope;
  private Var var;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    TypeRegistry typeRegistry = mock(TypeRegistry.class);
    when(compiler.getTypeRegistry()).thenReturn(typeRegistry);
    when(typeRegistry.getNativeObjectType(Scope.GLOBAL_THIS)).thenReturn(mock(ObjectType.class));
    scope = new Scope(parentScope, rootNode);
  }

  @Test
  public void testScopeConstructorWithParentScopeAndRootNode() {
    // Given

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndThisType() {
    // Given

    // When
    when(rootNode.getJSType()).thenReturn(mock(FunctionType.class));
    Scope scope = new Scope(rootNode, mock(ObjectType.class));

    // Then
    assertEquals(mock(ObjectType.class), scope.getTypeOfThis());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndNullThisType() {
    // Given

    // When
    Scope scope = new Scope(rootNode, null);

    // Then
    assertNull(scope.getTypeOfThis());
  }

  @Test
  public void testScopeConstructorWithNullParentScopeAndRootNode() {
    // Given

    // When
    Scope scope = new Scope(rootNode, compiler);

    // Then
    assertEquals(null, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testScopeConstructorWithNullParentScopeAndNullRootNode() {
    // Given

    // When
    Scope scope = new Scope(null, mock(ObjectType.class));

    // Then
    assertEquals(null, scope.getParent());
    assertNull(scope.getRootNode());
  }

  @Test
  public void testGetDepth() {
    // Given
    Scope scope = new Scope(null, mock(Node.class));

    // When
    int depth = scope.getDepth();

    // Then
    assertEquals(0, depth);
  }

  @Test
  public void testIsBottom() {
    // Given
    Scope scope = new Scope(null, null);

    // When
    boolean isBottom = scope.isBottom();

    // Then
    assertTrue(isBottom);
  }

  @Test
  public void testDeclareVariable() {
    // Given
    String name = "name";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    when(nameNode.getName()).thenReturn(name);

    // When
    Var var = scope.declare(name, nameNode, type, input);

    // Then
    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(scope, var.getScope());
    assertEquals(type, var.getType());
    assertEquals(input, var.getInput());
  }

  @Test
  public void testDeclareVariableWithExistingVariable() {
    // Given
    String name = "name";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);

    Var existingVar = mock(Var.class);
    when(scope.getVar(name)).thenReturn(existingVar);

    // When & Then
    assertNull(scope.declare(name, nameNode, type, input));
    verify(errorReporter, times(1)).error(eq("Duplicate variable declaration: " + name));
  }

  @Test
  public void testGetVar() {
    // Given
    String name = "name";
    Var var = new Var(false, name, null, null, scope, 0, null, false, null);
    when(scope.vars.get(name)).thenReturn(var);

    // When
    Var result = scope.getVar(name);

    // Then
    assertEquals(var, result);
  }

  @Test
  public void testGetVarWithNullName() {
    // Given

    // When
    Var result = scope.getVar(null);

    // Then
    assertNull(result);
  }

  @Test
  public void testGetVarWithNonExistentName() {
    // Given

    // When
    Var result = scope.getVar("nonExistentName");

    // Then
    assertNull(result);
  }

  @Test
  public void testGetVarWithNonExistentNameInParentScope() {
    // Given
    String name = "nonExistentName";
    Scope parentScope = mock(Scope.class);

    // When
    Var result = scope.getVar(name);

    // Then
    verify(parentScope, times(1)).getVar(eq(name));
    assertNull(result);
  }

  @Test
  public void testIsDeclared() {
    // Given
    String name = "name";

    // When
    boolean isDeclared = scope.isDeclared(name, false);

    // Then
    assertFalse(isDeclared);

    // Given a declared variable
    Var var = new Var("name", null, null, null, scope, 0, null, false, null);
    scope.vars.put("name", var);

    // When
    isDeclared = scope.isDeclared(name, false);

    // Then
    assertTrue(isDeclared);
  }

  @Test
  public void testIsDeclaredWithRecursiveParentScope() {
    // Given
    String name = "name";
    Scope parentScope = mock(Scope.class);

    // When
    boolean isDeclared = scope.isDeclared(name, true);

    // Then
    verify(parentScope, times(1)).isDeclared(eq(name), eq(true));
    assertFalse(isDeclared);
  }

  @Test
  public void testGetVars() {
    // Given
    Var var1 = new Var(false, "name1", null, null, scope, 0, null, false, null);
    Var var2 = new Var(false, "name2", null, null, scope, 1, null, false, null);
    scope.vars.put("name1", var1);
    scope.vars.put("name2", var2);

    // When
    Iterator<Var> varIterator = scope.getVars().iterator();

    // Then
    assertEquals(var1, varIterator.next());
    assertEquals(var2, varIterator.next());
    assertFalse(varIterator.hasNext());
  }

  @Test
  public void testGetVarCount() {
    // Given
    Var var1 = new Var(false, "name1", null, null, scope, 0, null, false, null);
    Var var2 = new Var(false, "name2", null, null, scope, 1, null, false, null);
    scope.vars.put("name1", var1);
    scope.vars.put("name2", var2);

    // When
    int varCount = scope.getVarCount();

    // Then
    assertEquals(2, varCount);
  }

  @Test
  public void testIsGlobal() {
    // Given
    Scope scope = new Scope(null, mock(Node.class));

    // When
    boolean isGlobal = scope.isGlobal();

    // Then
    assertTrue(isGlobal);

    // Given a non-global scope
    Scope parentScope = mock(Scope.class);
    when(parentScope.isGlobal()).thenReturn(false);
    Scope childScope = new Scope(parentScope, mock(Node.class));

    // When
    isGlobal = childScope.isGlobal();

    // Then
    assertFalse(isGlobal);
  }

  @Test
  public void testIsLocal() {
    // Given
    Scope scope = new Scope(null, mock(Node.class));

    // When
    boolean isLocal = scope.isLocal();

    // Then
    assertFalse(isLocal);

    // Given a non-global scope
    Scope parentScope = mock(Scope.class);
    when(parentScope.isGlobal()).thenReturn(false);
    Scope childScope = new Scope(parentScope, mock(Node.class));

    // When
    isLocal = childScope.isLocal();

    // Then
    assertTrue(isLocal);
  }

  @Test
  public void testGetSlot() {
    // Given
    String name = "name";
    Var var = new Var(false, name, null, null, scope, 0, null, false, null);

    // When
    StaticSlot<JSType> slot = scope.getSlot(name);

    // Then
    assertEquals(var, slot);
  }

  @Test
  public void testGetOwnSlot() {
    // Given
    String name = "name";
    Var var = new Var(false, name, null, null, scope, 0, null, false, null);

    // When
    StaticSlot<JSType> slot = scope.getOwnSlot(name);

    // Then
    assertEquals(var, slot);
  }

  @Test
  public void testGetVarWithBleedingFunction() {
    // Given
    Node parentNode = mock(Node.class);
    Node functionNode = mock(Node.class);
    Node variableNode = mock(Node.class);
    when(parentNode.getLastChild()).thenReturn(functionNode);
    when(functionNode.getFirstChild()).thenReturn(variableNode);
    when(variableNode.getName()).thenReturn("name");

    // When
    Var result = scope.getVar("name");

    // Then
    verify(nodeTraversal, times(1)).traverse(eq(rootNode), eq(new NodeTraversal.CallbackWithContext<Node>() {
      @Override
      public void visit(Node node, NodeTraversal.Context context) {
        if (node.getJSType().isFunction()) {
          node.traverse(new NodeTraversal.CallbackWithContext<Node>() {
            @Override
            public void visit(Node node, NodeTraversal.Context context) {
              assertEquals("name", node.getName());
              context.stop();
            }
          });
          context.stop();
        }
      }
    }));
    assertNull(result);
  }
}