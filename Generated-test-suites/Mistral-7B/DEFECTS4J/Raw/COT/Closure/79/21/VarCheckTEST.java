package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import java.util.Set;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VarCheckTest {

  @Test
  public void testVarCheck() {
    // Given
    Compiler compiler = mock(Compiler.class);
    VarCheck varCheck = new VarCheck(compiler);

    // When
    Node externs = Node.object();
    Node root = Node.object();
    varCheck.process(externs, root);

    // Then
    verify(compiler, times(1)).newExternInput("{SyntheticVarsDeclar}");
  }

  @Test
  public void testUndefinedVarError() {
    // Given
    Compiler compiler = mock(Compiler.class);
    VarCheck varCheck = new VarCheck(compiler);

    Node n = Node.newString(Token.NAME, "undefinedVar");
    Node parent = Node.object();
    Node scope = Node.object();

    when(compiler.getErrorLevel(any(JSError.class)))
        .thenReturn(CheckLevel.ERROR);

    // When
    varCheck.visit(null, n, parent);

    // Then
    InOrder inOrder = inOrder(compiler);
    inOrder.verify(compiler)
        .report(eq(n), eq(UNDEFINED_VAR_ERROR), eq(n.getString()));
  }

  @Test
  public void testStrictModuleDepError() {
    // Given
    Compiler compiler = mock(Compiler.class);
    VarCheck varCheck = new VarCheck(compiler, true);

    JSModule currModule = mock(JSModule.class);
    JSModule varModule = mock(JSModule.class);
    when(currModule.getName()).thenReturn("currModule");
    when(varModule.getName()).thenReturn("varModule");

    when(compiler.getModuleGraph().dependsOn(currModule, varModule))
        .thenReturn(false);

    Node n = Node.newString(Token.NAME, "crossModuleVar");
    Node parent = Node.object();

    // When
    varCheck.visit(null, n, parent);

    // Then
    InOrder inOrder = inOrder(compiler);
    inOrder.verify(compiler)
        .report(eq(n), eq(STRICT_MODULE_DEP_ERROR),
            eq(currModule.getName()), eq("crossModuleVar"), eq("varModule"));
  }

  @Test
  public void testMissingModuleDepWarning() {
    // Given
    Compiler compiler = mock(Compiler.class);
    VarCheck varCheck = new VarCheck(compiler);

    JSModule currModule = mock(JSModule.class);
    JSModule varModule = mock(JSModule.class);
    when(currModule.getName()).thenReturn("currModule");
    when(varModule.getName()).thenReturn("varModule");

    when(compiler.getModuleGraph().dependsOn(currModule, varModule))
        .thenReturn(false);

    Node n = Node.newString(Token.NAME, "crossModuleVar");
    Node parent = Node.object();
    Scope scope = Node.object();

    // When
    varCheck.visit(null, n, parent);

    // Then
    InOrder inOrder = inOrder(compiler);
    inOrder.verify(compiler, times(1)).report(eq(n),
        eq(MISSING_MODULE_DEP_ERROR), eq(currModule.getName()),
        eq(varModule.getName()), eq("crossModuleVar"));
  }

  @Test
  public void testValidModuleDep() {
    // Given
    Compiler compiler = mock(Compiler.class);
    VarCheck varCheck = new VarCheck(compiler);

    JSModule currModule = mock(JSModule.class);
    JSModule varModule = mock(JSModule.class);
    when(currModule.getName()).thenReturn("currModule");
    when(varModule.getName()).thenReturn("varModule");

    when(compiler.getModuleGraph().dependsOn(currModule, varModule))
        .thenReturn(true);

    Node n = Node.newString(Token.NAME, "crossModuleVar");
    Node parent = Node.object();

    // When
    varCheck.visit(null, n, parent);

    // Then
    verify(compiler, never()).report(eq(n), any(DiagnosticType.class),
        anyString(), anyString());
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  private static final DiagnosticType UNDEFINED_VAR_ERROR =
      VarCheck.UNDEFINED_VAR_ERROR;
  private static final DiagnosticType STRICT_MODULE_DEP_ERROR =
      VarCheck.STRICT_MODULE_DEP_ERROR;
  private static final DiagnosticType MISSING_MODULE_DEP_ERROR =
      VarCheck.MISSING_MODULE_DEP_ERROR;
}