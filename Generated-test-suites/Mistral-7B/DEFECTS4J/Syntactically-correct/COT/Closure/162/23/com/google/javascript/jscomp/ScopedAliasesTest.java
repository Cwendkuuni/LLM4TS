package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    transformationHandler = new AliasTransformationHandler();
    compiler = new AbstractCompiler(options);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testScopedAliases() {
    // Test valid usage of scoped alias
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getQualifiedName());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    Node aliasUsageNode = aliasUsages.get(0);
    assertNull(aliasUsageNode.getParent());
    assertEquals("dom", aliasUsageNode.getQualifiedName());

    // Test invalid usage of scoped alias
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  dom.createElement(goog.dom.TagName.DIV);" +
        "});" +
        "});";

    root = Node.parse(sourceCode);
    assertThrows(IllegalArgumentException.class, () -> scopedAliases.process(null, root));

    sourceCode = "goog.scope(function() {" +
        "goog.scope(function() {});" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCallNode = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals(2, scopeCallNode.getChildCount());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(0, aliasUsages.size());

    // Test alias redefinition
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var dom = goog.dom.createElement;" +
        "});" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = Node.parse(sourceCode);
    assertThrows(AssertionError.class, () -> scopedAliases.process(null, root));

    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var dom = goog.dom.createElement;" +
        "});" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getQualifiedName());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());
    Node firstAliasUsageNode = aliasUsages.get(0);
    assertEquals("dom", firstAliasUsageNode.getQualifiedName());

    Node secondAliasUsageNode = aliasUsages.get(1);
    assertEquals("dom", secondAliasUsageNode.getQualifiedName());

    // Test alias usage outside of the scoped block
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {});" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "return dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = Node.parse(sourceCode);
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));

    sourceCode = "goog.scope(function() {" +
        "function scopedFunction() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "  });" +
        "  return dom.createElement(goog.dom.TagName.DIV);" +
        "}" +
        "scopedFunction();" +
        "});";

    root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    scopeCallNode = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals(2, scopeCallNode.getChildCount());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    aliasUsageNode = aliasUsages.get(0);
    assertEquals("dom", aliasUsageNode.getQualifiedName());

    // Test alias usage with a qualified name
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {});" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    scopeCallNode = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals(2, scopeCallNode.getChildCount());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    aliasUsageNode = aliasUsages.get(0);
    assertEquals("goog.dom.createElement", aliasUsageNode.getQualifiedName());

    // Test alias usage with a type node
    sourceCode = "goog.scope(function() {" +
        "function ScopedFunction() {" +
        "}" +
        "goog.scope(function() {});" +
        "var scopedFunction = new ScopedFunction();" +
        "});";

    root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    scopeCallNode = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals(2, scopeCallNode.getChildCount());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    aliasUsageNode = aliasUsages.get(0);
    assertEquals("ScopedFunction", aliasUsageNode.getQualifiedName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidScopeCall() {
    String sourceCode = "goog.scope(function() {" +
        "goog.scope(function() {});" +
        "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testRedefinedAlias() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var dom = goog.dom.createElement;" +
        "});" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);
  }
}