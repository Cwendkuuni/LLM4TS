package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testProcess_NoErrors() {
    Node root = NodeUtil.createRootNode("test.js");

    scopedAliases.process(NodeUtil.EMPTY_NODE_LIST, root);

    // No errors reported
    verifyNoErrorsReported(compiler);

    // Aliases applied correctly
    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler).logAliasTransformation(
        eq("test.js"), eq(any(SourcePosition.class)),
        eq(eq("goog.scope")), eq("goog"),
        eq(eq("dom")), eq("goog.dom"));
  }

  @Test
  public void testProcess_Error_GoogScopeUsedImproperly() {
    Node root = NodeUtil.createRootNode("test.js");
    Node googScopeCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node invalidCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node invalidStatement = NodeUtil.createStatementNode(invalidCall, root);
    Node statementWithScopeCall = NodeUtil.createStatementNode(googScopeCall, root);

    when(preprocessorSymbolTable.addReference(googScopeCall.getFirstChild()))
        .thenReturn(null);

    scopedAliases.process(NodeUtil.nodeList(root), root);

    // Error reported for invalid call to goog.scope
    verifyDiagnostic(compiler, GOOG_SCOPE_USED_IMPROPERLY, invalidCall);
  }

  @Test
  public void testProcess_Error_GoogScopeHasBadParameters() {
    Node root = NodeUtil.createRootNode("test.js");
    Node googScopeCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node invalidFnNode = NodeUtil.createExpressionNode(
        NodeUtil.createFunctionNode("invalidFn", null, null, null), root);
    Node invalidStatement = NodeUtil.createStatementNode(googScopeCall, root);
    Node statementWithInvalidFnNode = NodeUtil.createStatementNode(
        NodeUtil.createCallNode("goog.scope", invalidFnNode, root), root);

    scopedAliases.process(NodeUtil.nodeList(root), root);

    // Error reported for invalid function passed to goog.scope
    verifyDiagnostic(compiler, GOOG_SCOPE_HAS_BAD_PARAMETERS, invalidFnNode);
  }

  @Test
  public void testProcess_Error_GoogScopeRefersToThis() {
    Node root = NodeUtil.createRootNode("test.js");
    Node googScopeCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node statementWithThis = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(
            NodeUtil.createExpressionNode(
                NodeUtil.createNameNode("this"), root)), root);
    Node statementWithScopeCall = NodeUtil.createStatementNode(
        NodeUtil.createCallNode("goog.scope", null, root), root);

    Node googScopeBlock = NodeUtil.createBlockNode(root);
    Node statementWithThisInScope = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(googScopeBlock), root);
    Node statementWithScopeCallInScope = NodeUtil.createStatementNode(
        NodeUtil.createCallNode("goog.scope", null, root), root);
    Node statementWithThisInScopeAndScopeCall = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(
            NodeUtil.createCallNode("goog.scope", null, root)), root);

    Node aliasDefinition = NodeUtil.createStatementNode(
        NodeUtil.createVariableDeclarationStatement(
            NodeUtil.createVariableDeclarationList(
                NodeUtil.createVariableDeclarationNode(
                    "alias", NodeUtil.createNameNode("alias"), null, null),
                null), null), root);
    Node statementWithAssignment = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(aliasDefinition), root);

    Node statementWithThisAndAssignment = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(statementWithAssignment), root);

    Node statementWithScopeCallInScopeAndAssignment = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(statementWithAssignment), root);

    Node statementWithThisInScopeAndScopeCallAndAssignment = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(statementWithAssignment), root);

    Node statements = NodeUtil.createBlockNode(root);
    Node statementBlock = NodeUtil.createBlockNode(root);

    statements.addChildren(statementWithThisInScopeAndScopeCallAndAssignment,
        statementWithScopeCallInScopeAndAssignment,
        statementWithThisInScopeAndScopeCall, statementWithScopeCall,
        statementWithThis, statementWithStatementWithScopeCall,
        statementWithThisInScope, statementWithStatementWithThis,
        statementWithStatementWithThisInScope, statementWithStatementWithStatementWithThis,
        statementWithThisInScopeAndAssignment, statementWithStatementWithThisAndAssignment,
        statementWithStatementWithThisInScopeAndAssignment,
        statementWithStatementWithStatementWithThisInScopeAndAssignment);

    Node rootWithStatements = NodeUtil.createStatementNode(statements, root);

    scopedAliases.process(NodeUtil.nodeList(root), rootWithStatements);

    // Error reported for usage of 'this' inside goog.scope
    verifyDiagnostic(compiler, GOOG_SCOPE_REFERENCES_THIS, statementWithThis);

    // Error reported for usage of 'this' inside goog.scope block
    verifyDiagnostic(compiler, GOOG_SCOPE_REFERENCES_THIS, statementWithThisInScope);

    // Error reported for usage of 'this' inside goog.scope and usage of return
    verifyDiagnostic(compiler, GOOG_SCOPE_USES_RETURN, statementWithThisAndAssignment);

    // Error reported for usage of 'this' inside goog.scope and usage of throw
    verifyDiagnostic(compiler, GOOG_SCOPE_USES_THROW, statementWithThisInScopeAndAssignment);

    // Error reported for usage of 'this' inside goog.scope, usage of return, and usage of throw
    verifyDiagnostic(compiler, GOOG_SCOPE_USES_THROW, statementWithThisInScopeAndAssignment);
  }

  @Test
  public void testProcess_Error_AliasRedefined() {
    Node root = NodeUtil.createRootNode("test.js");

    Node googScopeCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node aliasDefinition1 = NodeUtil.createStatementNode(
        NodeUtil.createVariableDeclarationStatement(
            NodeUtil.createVariableDeclarationList(
                NodeUtil.createVariableDeclarationNode(
                    "alias1", NodeUtil.createNameNode("alias1"), null, null),
                null), null), root);
    Node aliasDefinition2 = NodeUtil.createStatementNode(
        NodeUtil.createVariableDeclarationStatement(
            NodeUtil.createVariableDeclarationList(
                NodeUtil.createVariableDeclarationNode(
                    "alias1", NodeUtil.createNameNode("alias1"), null, null),
                null), null), root);
    Node statementWithAliasDefinition1 = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(aliasDefinition1), root);
    Node statementWithAliasDefinition2 = NodeUtil.createStatementNode(
        NodeUtil.createExpressionStatement(aliasDefinition2), root);
    Node statementWithScopeCall = NodeUtil.createStatementNode(
        NodeUtil.createCallNode("goog.scope", null, root), root);

    Node statements = NodeUtil.createBlockNode(root);
    statements.addChildren(statementWithAliasDefinition1,
        statementWithAliasDefinition2, statementWithScopeCall);

    Node rootWithStatements = NodeUtil.createStatementNode(statements, root);

    scopedAliases.process(NodeUtil.nodeList(root), rootWithStatements);

    // Error reported for alias redefinition
    verifyDiagnostic(compiler, GOOG_SCOPE_ALIAS_REDEFINED, aliasDefinition2,
        "alias1");
  }

  @Test
  public void testProcess_Error_NonAliasLocalVariable() {
    Node root = NodeUtil.createRootNode("test.js");

    Node googScopeCall = NodeUtil.createCallNode("goog.scope", null, root);
    Node nonAliasLocalVariableDefinition = NodeUtil.createStatementNode(
        NodeUtil.createVariableDeclarationStatement(
            NodeUtil.createVariableDeclarationList(
                NodeUtil.createVariableDeclarationNode(
                    "nonAliasLocalVariable", NodeUtil.createNameNode("nonAliasLocalVariable"),
                    null, null), null), null), root);
    Node statementWithScopeCall = NodeUtil.createStatementNode(
        NodeUtil.createCallNode("goog.scope", null, root), root);

    Node statements = NodeUtil.createBlockNode(root);
    statements.addChildren(nonAliasLocalVariableDefinition, statementWithScopeCall);

    Node rootWithStatements = NodeUtil.createStatementNode(statements, root);

    scopedAliases.process(NodeUtil.nodeList(root), rootWithStatements);

    // Error reported for non-alias local variable
    verifyDiagnostic(compiler, GOOG_SCOPE_NON_ALIAS_LOCAL,
        nonAliasLocalVariableDefinition, "nonAliasLocalVariable");
  }

  // Helper methods

  private void verifyNoErrorsReported(AbstractCompiler compiler) {
    verify(compiler, never()).report(any(DiagnosticType.class), any(Node.class));
  }

  private void verifyDiagnostic(AbstractCompiler compiler, DiagnosticType errorType,
      Node errorNode) {
    verify(compiler).report(any(Diagnostic.class));
    ArgumentCaptor<Diagnostic> diagnosticCaptor = ArgumentCaptor.forClass(Diagnostic.class);
    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(errorType, diagnostic.getType());
    assertEquals(errorNode, diagnostic.getSourceNode());
  }

  private DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USED_IMPROPERLY",
      "The call to goog.scope must be alone in a single statement.");

  private DiagnosticType GOOG_SCOPE_HAS_BAD_PARAMETERS =
      DiagnosticType.error(
          "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
          "The call to goog.scope must take only a single parameter.  It must" +
              " be an anonymous function that itself takes no parameters.");

  private DiagnosticType GOOG_SCOPE_REFERENCES_THIS = DiagnosticType.error(
      "JSC_GOOG_SCOPE_REFERENCES_THIS",
      "The body of a goog.scope function cannot reference 'this'.");

  private DiagnosticType GOOG_SCOPE_USES_RETURN = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USES_RETURN",
      "The body of a goog.scope function cannot use 'return'.");

  private DiagnosticType GOOG_SCOPE_USES_THROW = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USES_THROW",
      "The body of a goog.scope function cannot use 'throw'.");

  private DiagnosticType GOOG_SCOPE_ALIAS_REDEFINED = DiagnosticType.error(
      "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
      "The alias {0} is assigned a value more than once.");

  private DiagnosticType GOOG_SCOPE_NON_ALIAS_LOCAL = DiagnosticType.error(
      "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
      "The local variable {0} is in a goog.scope and is not an alias.");
}