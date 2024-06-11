package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.test.testcases.CompilationTestFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Test the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    preprocessorSymbolTable = new PreprocessorSymbolTable(compiler);
    transformationHandler = new AliasTransformationHandler(options);
  }

  @Test
  public void testProcess_NoAliases() {
    Node root = CompilationTestFactory.getTestNode("test_no_aliases.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_AliasesDefinedInsideFunction() {
    Node root = CompilationTestFactory.getTestNode("test_aliases_defined_inside_function.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    Node aliasDefinition1 = aliasDefinitions.get(0);
    assertEquals("goog_test", aliasDefinition1.getString());
    Node aliasDefinition2 = aliasDefinitions.get(1);
    assertEquals("goog_dom", aliasDefinition2.getString());
    Collection<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof ScopedAliases.AliasedNode) {
        ScopedAliases.AliasedNode aliasedNode = (ScopedAliases.AliasedNode) aliasUsage;
        Node aliasDefinition = aliasedNode.getAliasDefinition();
        assertEquals("goog_" + aliasDefinition.getString(), aliasedNode.getAliasReference().getString());
      } else if (aliasUsage instanceof ScopedAliases.AliasedTypeNode) {
        ScopedAliases.AliasedTypeNode aliasedTypeNode = (ScopedAliases.AliasedTypeNode) aliasUsage;
        Node aliasDefinition = aliasedTypeNode.getAliasDefinition();
        String typeName = aliasedTypeNode.getCorrectedType();
        String expectedAlias = "";
        if (aliasDefinition != null) {
          expectedAlias = aliasDefinition.getString();
        }
        assertEquals("goog_" + expectedAlias, typeName);
      }
    }
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_CallToGoogScopeWithParameters() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_parameters.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    Node body = anonymousFnNode.getFirstChild();
    assertEquals(1, body.getChildCount());
    Node parameterNode = body.getChildAtIndex(0);
    assertEquals("param1", parameterNode.getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_CallToGoogScopeWithBlock() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_block.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    Node scopeClosureBlock = scopeCall.getLastChild();
    assertEquals(2, scopeClosureBlock.getChildCount());
    Node returnStatement = scopeClosureBlock.getLastChild();
    assertEquals(1, returnStatement.getChildCount());
    Node returnValueNode = returnStatement.getChildAtIndex(0);
    assertEquals("100", returnValueNode.getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_CallToGoogScopeWithInvalidParameters() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_invalid_parameters.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithNameConflicts() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_name_conflicts.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasDefinitions.size());
    assertEquals("goog_test", aliasDefinitions.get(0).getString());
    assertEquals("goog_dom", aliasDefinitions.get(1).getString());
    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof ScopedAliases.AliasedNode) {
        ScopedAliases.AliasedNode aliasedNode = (ScopedAliases.AliasedNode) aliasUsage;
        Node aliasDefinition = aliasedNode.getAliasDefinition();
        assertEquals("goog_" + aliasDefinition.getString(), aliasedNode.getAliasReference().getString());
      } else if (aliasUsage instanceof ScopedAliases.AliasedTypeNode) {
        ScopedAliases.AliasedTypeNode aliasedTypeNode = (ScopedAliases.AliasedTypeNode) aliasUsage;
        Node aliasDefinition = aliasedTypeNode.getAliasDefinition();
        String typeName = aliasedTypeNode.getCorrectedType();
        String expectedAlias = "";
        if (aliasDefinition != null) {
          expectedAlias = aliasDefinition.getString();
        }
        assertEquals("goog_" + expectedAlias, typeName);
      }
    }
    List<DiagnosticInfo> diagnostics = compiler.getDiagnostics();
    assertEquals(2, diagnostics.size());
    DiagnosticInfo firstDiagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", firstDiagnostic.getMessage());
    DiagnosticInfo secondDiagnostic = diagnostics.get(1);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", secondDiagnostic.getMessage());
  }

  @Test
  public void testProcess_CallToGoogScopeWithInvalidFunction() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_invalid_function.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithNonExpressionStatement() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_non_expression_statement.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithThisKeyword() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_this_keyword.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithReturnStatement() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_return_statement.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithThrowStatement() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_throw_statement.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testProcess_CallToGoogScopeWithImproperUsage() {
    Node root = CompilationTestFactory.getTestNode("test_call_to_goog_scope_with_improper_usage.js");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    // The following line causes an error
    // Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }
}