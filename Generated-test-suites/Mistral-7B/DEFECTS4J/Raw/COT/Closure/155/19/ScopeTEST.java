package com.google.javascript.jscomp; // Import the necessary packages

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopeTest {
  private static final String VAR_NAME = "testVar";
  private static final Node ROOT_NODE = mock(Node.class);
  private static final CompilerInput INPUT = mock(CompilerInput.class);
  private static final Scope PARENT_SCOPE = mock(Scope.class);
  private static final ErrorReporter ERROR_REPORTER = mock(ErrorReporter.class);
  private Scope scope;

  @Before
  public void setUp() {
    when(ROOT_NODE.getJSType()).thenReturn(null); // For simplicity
    when(INPUT.getName()).thenReturn("testFile.js");
    scope = new Scope(PARENT_SCOPE, ROOT_NODE);
  }

  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    assertThat(scope.getParent()).isEqualTo(PARENT_SCOPE);
    assertThat(scope.getRootNode()).isEqualTo(ROOT_NODE);
  }

  @Test
  public void testScopeConstructorWithGlobalRootNode() {
    scope = new Scope(ROOT_NODE, null);
    assertThat(scope.getParent()).isNull();
    assertThat(scope.getRootNode()).isEqualTo(ROOT_NODE);
  }

  @Test
  public void testScopeConstructorWithBottomTypeRootNode() {
    when(ROOT_NODE.getJSType()).thenReturn(JSType.BOTTOM);
    scope = new Scope(null, ROOT_NODE);
    assertThat(scope.getParent()).isNull();
    assertThat(scope.getRootNode()).isEqualTo(ROOT_NODE);
    assertThat(scope.isBottom()).isTrue();
  }

  @Test
  public void testDeclareVariable() {
    Var testVar = scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    assertThat(scope.getVarCount()).isEqualTo(1);
    assertThat(testVar.getName()).isEqualTo(VAR_NAME);
  }

  @Test
  public void testDeclareVariableWithNameNode() {
    Node nameNode = mock(Node.class);
    when(nameNode.getName()).thenReturn(VAR_NAME);
    Var testVar = scope.declare(VAR_NAME, nameNode, JSType.STRING, INPUT);
    assertThat(scope.getVarCount()).isEqualTo(1);
    assertThat(testVar.getName()).isEqualTo(VAR_NAME);
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareVariableWithExistingName() {
    scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    scope.declare(VAR_NAME, null, JSType.NUMBER, INPUT);
  }

  @Test
  public void testUndeclareVariable() {
    Var testVar = scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    scope.undeclare(testVar);
    assertThat(scope.getVarCount()).isZero();
  }

  @Test
  public void testGetSlot() {
    Var testVar = scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    assertThat(scope.getSlot(VAR_NAME)).isEqualTo(testVar);
  }

  @Test
  public void testGetOwnSlot() {
    Var testVar = scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    assertThat(scope.getOwnSlot(VAR_NAME)).isEqualTo(testVar);
  }

  @Test
  public void testGetVar() {
    Var testVar = scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    assertThat(scope.getVar(VAR_NAME)).isEqualTo(testVar);
  }

  @Test
  public void testIsDeclared() {
    scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Boolean> recurseCaptor = ArgumentCaptor.forClass(Boolean.class);
    when(PARENT_SCOPE.isDeclared(nameCaptor.capture(), recurseCaptor.capture())).thenReturn(false);
    assertThat(scope.isDeclared(VAR_NAME, false)).isTrue();
  }

  @Test
  public void testIsDeclaredWithRecursion() {
    scope.declare(VAR_NAME, null, JSType.STRING, INPUT);
    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Boolean> recurseCaptor = ArgumentCaptor.forClass(Boolean.class);
    when(PARENT_SCOPE.isDeclared(nameCaptor.capture(), recurseCaptor.capture())).thenReturn(true);
    assertThat(scope.isDeclared(VAR_NAME, true)).isTrue();
  }

  @Test
  public void testGetVars() {
    Var testVar1 = scope.declare(VAR_NAME + "_1", null, JSType.STRING, INPUT);
    Var testVar2 = scope.declare(VAR_NAME + "_2", null, JSType.NUMBER, INPUT);
    Iterator<Var> varIterator = scope.getVars();
    assertThat(varIterator.next()).isEqualTo(testVar1);
    assertThat(varIterator.next()).isEqualTo(testVar2);
    assertThat(varIterator.hasNext()).isFalse();
  }

  @Test
  public void testGetVarCount() {
    Var testVar1 = scope.declare(VAR_NAME + "_1", null, JSType.STRING, INPUT);
    Var testVar2 = scope.declare(VAR_NAME + "_2", null, JSType.NUMBER, INPUT);
    assertThat(scope.getVarCount()).isEqualTo(2);
  }

  @Test
  public void testIsGlobal() {
    assertThat(scope.isGlobal()).isFalse();
  }

  @Test
  public void testIsLocal() {
    assertThat(scope.isLocal()).isTrue();
  }
}