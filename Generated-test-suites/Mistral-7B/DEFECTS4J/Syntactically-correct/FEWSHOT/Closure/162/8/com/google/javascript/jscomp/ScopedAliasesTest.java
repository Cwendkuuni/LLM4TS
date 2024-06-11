package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCallback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private static final String SCOPING_METHOD_NAME = "goog.scope";

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private Traversal traversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    traversal = new Traversal();

    when(compiler.report(any(DiagnosticType.class), anyString(), anyVarArgs())).thenCallRealMethod();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_noAliases() {
    Node root = Node.createString("// No goog.scope calls present.");
    scopedAliases.process(null, root);
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_simpleAlias() {
    Noderoot =Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");

    scopedAliases.process(null, root);

    verify(compiler).report(eq(GOOG_SCOPE_USED_IMPROPERLY), anyString());
    verify(transformationHandler).logAliasTransformation(
        anyString(), any(SourcePosition.class));
    verify(compiler).reportCodeChange();

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getQualifiedName());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
    assertEquals(Token.NAME, aliasReference.getType());
    assertEquals("dom", aliasReference.getString());
    Node aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
    assertEquals(Token.NAME, aliasDefinition.getType());
    assertEquals("goog.dom", aliasDefinition.getQualifiedName());

    Map<String, Var> aliases = traversal.getAliases();
    assertEquals(1, aliases.size());
    Var aliasVar = aliases.get("dom");
    assertNotNull(aliasVar);
    assertEquals("goog.dom", aliasVar.getInitialValue().getQualifiedName());
  }

  @Test
  public void testProcess_multipleAliases() {
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var domElements = goog.dom.Elements;" +
        "  goog.dom.createElement(domElements.DIV);" +
        "});");

    scopedAliases.process(null, root);

    verify(compiler, times(2)).report(eq(GOOG_SCOPE_USED_IMPROPERLY), anyString());
    verify(transformationHandler, times(2)).logAliasTransformation(
        anyString(), any(SourcePosition.class));
    verify(compiler, times(2)).reportCodeChange();

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getQualifiedName());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertTrue(aliasUsage1 instanceof AliasedNode);
    Node aliasReference1 = ((AliasedNode) aliasUsage1).aliasReference;
    assertEquals(Token.NAME, aliasReference1.getType());
    assertEquals("dom", aliasReference1.getString());
    Node aliasDefinition1 = ((AliasedNode) aliasUsage1).aliasDefinition;
    assertEquals(Token.NAME, aliasDefinition1.getType());
    assertEquals("goog.dom", aliasDefinition1.getQualifiedName());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertTrue(aliasUsage2 instanceof AliasedTypeNode);
    Node aliasReference2 = ((AliasedTypeNode) aliasUsage2).aliasReference;
    assertEquals(Token.NAME, aliasReference2.getType());
    assertEquals("domElements", aliasReference2.getString());
    String correctedType = ((AliasedTypeNode) aliasUsage2).correctedType;
    assertEquals("goog.dom.Elements", correctedType);

    Node aliasDefinition2 = ((AliasedTypeNode) aliasUsage2).aliasDefinition;
    assertEquals(Token.NAME, aliasDefinition2.getType());
    assertEquals("goog.dom.Elements", aliasDefinition2.getQualifiedName());

    Map<String, Var> aliases = traversal.getAliases();
    assertEquals(2, aliases.size());
    Var aliasVar1 = aliases.get("dom");
    assertNotNull(aliasVar1);
    assertEquals("goog.dom", aliasVar1.getInitialValue().getQualifiedName());

    Var aliasVar2 = aliases.get("domElements");
    assertNotNull(aliasVar2);
    assertEquals("goog.dom.Elements", aliasVar2.getInitialValue().getQualifiedName());
  }

  @Test
  public void testProcess_scopeCallsInsideFunction() {
    Node root = Node.createString(
        "function foo() {" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "}");

    scopedAliases.process(null, root);

    verify(compiler, times(2)).report(eq(GOOG_SCOPE_USED_IMPROPERLY), anyString());
    verify(transformationHandler, times(2)).logAliasTransformation(
        anyString(), any(SourcePosition.class));
    verify(compiler, times(2)).reportCodeChange();

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(2, scopeCalls.size());
    for (Node scopeCall : scopeCalls) {
      assertEquals(Token.CALL, scopeCall.getType());
      assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getQualifiedName());
    }
  }

  @Test
  public void testProcess_aliasRedefinition() {
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = 'google';" + // Alias redefinition
        "});");

    scopedAliases.process(null, root);

    verify(compiler).report(eq(GOOG_SCOPE_ALIAS_REDEFINED), "dom", anyString());
  }

  @Test
  public void testProcess_aliasWithReturn() {
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});");

    scopedAliases.process(null, root);

    verify(compiler).report(eq(GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testProcess_aliasWithThrow() {
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});");

    scopedAliases.process(null, root);

    verify(compiler).report(eq(GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testProcess_aliasWithThis() {
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  this.foo = 'bar';" + // Alias cannot use 'this'
        "});");

    scopedAliases.process(null, root);

    verify(compiler).report(eq(GOOG_SCOPE_REFERENCES_THIS));
  }
}