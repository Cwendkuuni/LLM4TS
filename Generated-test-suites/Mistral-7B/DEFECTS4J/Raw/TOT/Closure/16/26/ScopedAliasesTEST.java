package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Traversal.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopedAliasesTest {
  private static final String SOME_FILE = "SomeFile.js";

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testNoScopeCall() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verifyZeroInteractions(compiler);
  }

  @Test
  public void testValidScopeCall() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    root.addChild(scopeCall);
    root.addChild(new Node(Token.LP, null));
    root.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    root.addChild(new Node(Token.RP, null));
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    ArgumentCaptor<AliasTransformation> aliasCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler).logAliasTransformation(eq(SOME_FILE), eq(new SourcePosition<>(0, 0)));
    verify(transformationHandler).logAliasTransformation(eq(SOME_FILE), eq(new SourcePosition<>(0, 1)));
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testValidScopeCallWithFunctionBody() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    Node body = new Node(Token.FUNCTION, null, new ArrayList<Node>(), null);
    body.addChild(new Node(Token.RETURN, null, null, new Node(Token.NUMBER, 42)));
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(body);
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    ArgumentCaptor<AliasTransformation> aliasCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler).logAliasTransformation(eq(SOME_FILE), eq(new SourcePosition<>(0, 1)));
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testScopeCallWithBadParameters() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.IDENT, "someVar"));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_HAS_BAD_PARAMETERS), anyString());
  }

  @Test
  public void testScopeCallWithThisReference() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(new Node(Token.THIS, null, null, null));
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testScopeCallWithReturnStatement() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(new Node(Token.RETURN, null, null, null));
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testScopeCallWithThrowStatement() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(new Node(Token.THROW, null, null, new Node(Token.IDENT, "someError")));
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_USES_THROW), eq("someError"));
  }

  @Test
  public void testInvalidScopeCall() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    Node body = new Node(Token.FUNCTION, null, new ArrayList<Node>(), null);
    body.addChild(new Node(Token.RETURN, null, null, new Node(Token.NUMBER, 42)));
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(body);
    scopeCall.addChild(new Node(Token.LP, null)); // extra LP
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testScopeCallWithAlias() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(
        new Node(Token.ASSIGN, null, new Node(Token.IDENT, "dom"), new Node(Token.IDENT, "goog.dom")));
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verifyZeroInteractions(compiler);
  }

  @Test
  public void testScopeCallWithAliasRedefinition() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(new Node(Token.ASSIGN, null, new Node(Token.IDENT, "dom"), new Node(Token.IDENT, "goog.dom")));
    scopeCall.getLastChild().addChild(new Node(Token.ASSIGN, null, new Node(Token.IDENT, "dom"), new Node(Token.IDENT, "someOtherAlias")));
    scopeCall.addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_ALIAS_REDEFINED), eq("dom"));
  }

  @Test
  public void testScopeCallWithNonAliasLocal() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Traversal traversal = new Traversal();
    Node root = new Node(Token.PROGRAM);
    Node scopeCall = new Node(Token.CALL, new Node(Token.IDENT, "goog.scope"), null);
    scopeCall.addChild(new Node(Token.LP, null));
    scopeCall.addChild(new Node(Token.FUNCTION, null, Collections.emptyList(), null));
    scopeCall.getLastChild().addChild(new Node(Token.VAR, null, "someVar", null, null));
    scopeCall.getLastChild().addChild(new Node(Token.RP, null));
    root.addChild(scopeCall);
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);
    verify(compiler).report(eq(GOOG_SCOPE_NON_ALIAS_LOCAL), eq("someVar"));
  }
}