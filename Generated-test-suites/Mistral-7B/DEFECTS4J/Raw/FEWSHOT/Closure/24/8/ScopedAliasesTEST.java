package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.NodeTraversal;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.Traversal;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    this.compiler = new AbstractCompiler(options, handler);
    this.preprocessorSymbolTable = new PreprocessorSymbolTable();
    this.scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, handler);
  }

  @Test
  public void testApplyAliases() {
    CompilationUnit unit = new CompilationUnit();
    Node root = unit.getRoot();
    NodeTraversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);

    if (!traversal.hasErrors()) {
      // Apply the aliases.
      for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
        aliasUsage.applyAlias();
      }

      // Verify that the aliases have been applied.
      for (Node aliasUsage : traversal.getAliasUsages()) {
        Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
        Node aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
        assertEquals(aliasDefinition, aliasReference.getParent());
      }

      // Verify that the alias definitions have been removed.
      for (Node aliasDefinition : traversal.getAliasDefinitionsInOrder()) {
        assertNull(aliasDefinition.getParent());
      }

      // Verify that the scopes have been collapsed.
      for (Node scopeCall : traversal.getScopeCalls()) {
        Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
        Node scopeClosureBlockParent = scopeClosureBlock.getParent();
        Node expressionWithScopeCall = scopeCall.getParent();

        // Verify that the scope closure block has been merged into its parent.
        assertTrue(scopeClosureBlockParent.getChildren().contains(scopeClosureBlock));
        assertEquals(expressionWithScopeCall, scopeClosureBlockParent);
      }

      // Verify that the code change has been reported.
      assertTrue(compiler.hasCodeChange());
    }
  }

  @Test
  public void testNoAliases() {
    CompilationUnit unit = new CompilationUnit();
    Node root = unit.getRoot();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // No aliases should have been found.
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
  }

  @Test
  public void testAliasesWithSingleParameter() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    if (!traversal.hasErrors()) {
      // Apply the aliases.
      for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
        aliasUsage.applyAlias();
      }

      // Verify that the aliases have been applied.
      for (Node aliasUsage : traversal.getAliasUsages()) {
        Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
        Node aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
        assertEquals(aliasDefinition, aliasReference.getParent());
      }

      // Verify that the alias definitions have been removed.
      for (Node aliasDefinition : traversal.getAliasDefinitionsInOrder()) {
        assertNull(aliasDefinition.getParent());
      }

      // Verify that the scopes have been collapsed.
      for (Node scopeCall : traversal.getScopeCalls()) {
        Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
        Node scopeClosureBlockParent = scopeCall.getParent();
        Node expressionWithScopeCall = scopeCall.getParent();

        // Verify that the scope closure block has been merged into its parent.
        assertTrue(scopeClosureBlockParent.getChildren().contains(scopeClosureBlock));
        assertEquals(expressionWithScopeCall, scopeClosureBlockParent);
      }

      // Verify that the code change has been reported.
      assertTrue(compiler.hasCodeChange());
    }
  }

  @Test
  public void testAliasesWithMultipleParameters() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function(foo, bar) {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV, foo, bar);" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // No aliases should have been found.
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
  }

  @Test
  public void testAliasesWithThis() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  this.foo = 'bar';" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The use of 'this' should have caused an error.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, traversal.getErrors().get(0).getType());
  }

  @Test
  public void testAliasesWithReturn() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  return 'foo';" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The use of 'return' should have caused an error.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_USES_RETURN, traversal.getErrors().get(0).getType());
  }

  @Test
  public void testAliasesWithThrow() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  throw new Error('foo');" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The use of 'throw' should have caused an error.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_USES_THROW, traversal.getErrors().get(0).getType());
  }

  @Test
  public void testAliasesWithAliasRedefinition() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  var foo = 42;" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The redefinition of the alias 'foo' should have caused an error.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getErrors().get(0).getType());
  }

  @Test
  public void testAliasesWithNonAliasLocal() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = 42;" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The presence of a non-alias local variable should have caused an error.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.getErrors().get(0).getType());
  }

  @Test
  public void testAliasesWithCircularDependencies() {
    CompilationUnit unit = new CompilationUnit();
    String sourceCode =
        "goog.scope(function() {" +
        "  var a = goog.scope(function() { return {}; });" +
        "  var b = a.a;" +
        "});";
    unit.setSource(sourceCode, "Test.java");
    Node root = unit.parse();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Circular dependencies should not be allowed.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getErrors().get(0).getType());
  }
}