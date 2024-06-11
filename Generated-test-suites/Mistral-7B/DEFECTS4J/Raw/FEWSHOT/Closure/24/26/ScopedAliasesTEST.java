package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    Node traversedRoot = NodeTraversal.traverse(compiler, root, new NodeTraversal.EmptyCallback()).getCurrentNode();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    assertEquals(1, scopeCalls.size());
    assertTrue(scopeCalls.get(0).isCall());
    assertTrue(SCOPED_ALIASES_SCOPING_METHOD_NAME.equals(scopeCalls.get(0).getFirstChild().getQualifiedName()));

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitionsInOrder.size());

    Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, domAliasDefinition.getType());
    assertEquals("dom", domAliasDefinition.getString());

    Node DIVAliasDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals(Token.VAR, DIVAliasDefinition.getType());
    assertEquals("DIV", DIVAliasDefinition.getString());

    Node googAliasDefinition = aliasDefinitionsInOrder.get(2);
    assertEquals(Token.VAR, googAliasDefinition.getType());
    assertEquals("goog", googAliasDefinition.getString());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(3, aliasUsages.size());

    AliasUsage domAliasUsage = aliasUsages.get(0);
    assertEquals(domAliasDefinition, domAliasUsage.getAliasDefinition());
    assertEquals(NodeUtil.findChildByQualifiedName(traversedRoot, "dom"), domAliasUsage.getAliasReference());

    AliasUsage DIVAliasUsage = aliasUsages.get(1);
    assertEquals(DIVAliasDefinition, DIVAliasUsage.getAliasDefinition());
    assertEquals(NodeUtil.findChildByQualifiedName(traversedRoot, "dom.TagName.DIV"), DIVAliasUsage.getAliasReference());

    AliasUsage googAliasUsage = aliasUsages.get(2);
    assertEquals(googAliasDefinition, googAliasUsage.getAliasDefinition());
    assertEquals(NodeUtil.findChildByQualifiedName(traversedRoot, "goog"), googAliasUsage.getAliasReference());

    // When we apply the aliases
    scopedAliases.getTraversal().applyAliases();

    // Then the alias nodes replace the original nodes
    Node traversedRootAfterAliases = NodeUtil.tryMergeBlock(traversedRoot);
    Node domAliasNode = NodeUtil.findChildByQualifiedName(traversedRootAfterAliases, "dom");
    Node DIVAliasNode = NodeUtil.findChildByQualifiedName(traversedRootAfterAliases, "goog.dom.TagName.DIV");
    Node googAliasNode = NodeUtil.findChildByQualifiedName(traversedRootAfterAliases, "goog");

    assertEquals(Token.CALL, domAliasNode.getType());
    assertEquals("goog.dom.createElement", domAliasNode.getQualifiedName());
    assertNull(domAliasNode.getFirstChild());
    assertNull(domAliasNode.getLastChild());

    assertEquals(Token.NAME, DIVAliasNode.getType());
    assertEquals("goog.dom.TagName.DIV", DIVAliasNode.getQualifiedName());

    assertEquals(Token.NAME, googAliasNode.getType());
    assertEquals("goog", googAliasNode.getQualifiedName());

    // And the original nodes have been removed
    Node originalDomNode = NodeUtil.findChildByQualifiedName(traversedRoot, "dom");
    Node originalDIVNode = NodeUtil.findChildByQualifiedName(traversedRoot, "dom.TagName.DIV");
    Node originalGoogNode = NodeUtil.findChildByQualifiedName(traversedRoot, "goog");

    assertNull(originalDomNode);
    assertNull(originalDIVNode);
    assertNull(originalGoogNode);
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithInvalidParameters() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  goog.scope(function(a) {});\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithThisReference() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  this.test = 1;\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  return 1;\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  throw new Error(\"test\");\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithDuplicateAliasDefinition() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var DIV = 1;\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getTypeName());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocalVariable() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {\n" +
        "  var test = 1;\n" +
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null, null);

    // When
    scopedAliases.process(null, root);

    // Then
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getTypeName());
  }
}