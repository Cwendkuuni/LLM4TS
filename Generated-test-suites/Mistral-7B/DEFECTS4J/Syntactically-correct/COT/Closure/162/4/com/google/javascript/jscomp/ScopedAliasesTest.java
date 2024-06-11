package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.annotation.Nullable;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test empty case
    Node root = null;
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test valid case
    Node externs = null;
    Node validRoot = new Node(Token.SCRIPT);
    scopedAliases.process(externs, validRoot);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliases_googScopeUsedImproperly() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node improperlyRoot = new Node(Token.SCRIPT);
    improperlyRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    improperlyRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    improperlyRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    improperlyRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    Node improperlyCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    improperlyCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    improperlyCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    improperlyCallToScope.addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    improperlyRoot.getLastChild().getLastChild().getLastChild().addChild(improperlyCallToScope);

    scopedAliases.process(null, improperlyRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testScopedAliases_googScopeHasBadParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node hasBadParametersRoot = new Node(Token.SCRIPT);
    hasBadParametersRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    hasBadParametersRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    hasBadParametersRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    hasBadParametersRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    Node hasBadParametersAnonymousFn = new Node(Token.FUNCTION, new SourcePosition(0, 0));
    hasBadParametersAnonymousFn.addChild(new Node(Token.NAME, "a", new SourcePosition(0, 0)));
    hasBadParametersAnonymousFn.addChild(new Node(Token.RETURN, new SourcePosition(0, 0)));
    Node hasBadParametersCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    hasBadParametersCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    hasBadParametersCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    hasBadParametersCallToScope.addChild(hasBadParametersAnonymousFn);
    hasBadParametersRoot.getLastChild().getLastChild().getLastChild().addChild(hasBadParametersCallToScope);

    scopedAliases.process(null, hasBadParametersRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS));
  }

  @Test
  public void testScopedAliases_googScopeRefersToThis() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node refersToThisRoot = new Node(Token.SCRIPT);
    refersToThisRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    refersToThisRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    refersToThisRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    refersToThisRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    Node refersToThisAnonymousFn = new Node(Token.FUNCTION, new SourcePosition(0, 0));
    refersToThisAnonymousFn.addChild(new Node(Token.THIS, new SourcePosition(0, 0)));
    Node refersToThisCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    refersToThisCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    refersToThisCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    refersToThisCallToScope.addChild(refersToThisAnonymousFn);
    refersToThisRoot.getLastChild().getLastChild().getLastChild().addChild(refersToThisCallToScope);

    scopedAliases.process(null, refersToThisRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testScopedAliases_googScopeUsesReturn() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node usesReturnRoot = new Node(Token.SCRIPT);
    usesReturnRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    usesReturnRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    usesReturnRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    usesReturnRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    Node usesReturnAnonymousFn = new Node(Token.FUNCTION, new SourcePosition(0, 0));
    Node usesReturnReturn = new Node(Token.RETURN, new SourcePosition(0, 0));
    usesReturnAnonymousFn.addChild(usesReturnReturn);
    Node usesReturnCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    usesReturnCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    usesReturnCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    usesReturnCallToScope.addChild(usesReturnAnonymousFn);
    usesReturnRoot.getLastChild().getLastChild().getLastChild().addChild(usesReturnCallToScope);

    scopedAliases.process(null, usesReturnRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testScopedAliases_googScopeUsesThrow() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node usesThrowRoot = new Node(Token.SCRIPT);
    usesThrowRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    usesThrowRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    usesThrowRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    usesThrowRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.FUNCTION, new SourcePosition(0, 0)));
    Node usesThrowAnonymousFn = new Node(Token.FUNCTION, new SourcePosition(0, 0));
    Node usesThrowThrow = new Node(Token.THROW, new SourcePosition(0, 0));
    usesThrowAnonymousFn.addChild(usesThrowThrow);
    Node usesThrowCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    usesThrowCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    usesThrowCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    usesThrowCallToScope.addChild(usesThrowAnonymousFn);
    usesThrowRoot.getLastChild().getLastChild().getLastChild().addChild(usesThrowCallToScope);

    scopedAliases.process(null, usesThrowRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testScopedAliases_aliasRedefined() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node redefinedRoot = new Node(Token.SCRIPT);
    redefinedRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().getLastChild().getLastChild().getLastChild().addChild(new Node(Token.NAME, "a", new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().getLastChild().getLastChild().getLastChild().getLastChild().addChild(new Node(Token.NAME, "a", new SourcePosition(0, 0)));
    Node redefinedCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    redefinedCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    redefinedCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    redefinedRoot.getLastChild().getLastChild().getLastChild().addChild(redefinedCallToScope);

    scopedAliases.process(null, redefinedRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED));
  }

  @Test
  public void testScopedAliases_nonAliasLocal() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node nonAliasLocalRoot = new Node(Token.SCRIPT);
    nonAliasLocalRoot.addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    nonAliasLocalRoot.getLastChild().addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    nonAliasLocalRoot.getLastChild().getLastChild().addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    nonAliasLocalRoot.getLastChild().getLastChild().getLastChild().addChild(new Node(Token.VAR, new SourcePosition(0, 0)));
    nonAliasLocalRoot.getLastChild().getLastChild().getLastChild().getLastChild().addChild(new Node(Token.NAME, "a", new SourcePosition(0, 0)));
    Node nonAliasLocalCallToScope = new Node(Token.CALL, new SourcePosition(0, 0));
    nonAliasLocalCallToScope.addChild(new Node(Token.GOOG, new SourcePosition(0, 0)));
    nonAliasLocalCallToScope.addChild(new Node(Token.SCOPE, new SourcePosition(0, 0)));
    nonAliasLocalRoot.getLastChild().getLastChild().addChild(nonAliasLocalCallToScope);

    scopedAliases.process(null, nonAliasLocalRoot);
    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    assertFalse(scopedAliases.hasErrors());
    compiler.verify(
        () -> compiler.report(errorCaptor.capture()), times(1));
    assertThat(errorCaptor.getValue(), equalTo(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL));
  }
}