package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.SimpleCompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsCollector;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Test class for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = "(" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var element = goog.dom.createElement(DIV);" +
        "});" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var element = goog.dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler() {};
    SimpleCompilerOptions options = new SimpleCompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler() {};

    DiagnosticsCollector diagnosticsCollector = new DiagnosticsCollector();
    Diagnostics diagnostics = diagnosticsCollector;

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Node.parseTree(sourceCode, null, null, null, null);
    scopedAliases.process(null, root);

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    List<Node> aliasDefinitionsInOrder =
        scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Check that exactly one alias definition is created for each alias.
    Map<String, Integer> aliasCounts = Maps.newHashMap();
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      String name = aliasDefinition.getString();
      aliasCounts.put(name, aliasCounts.getOrDefault(name, 0) + 1);
    }

    for (AliasUsage aliasUsage : aliasUsages) {
      String name = ((AliasedNode) aliasUsage).aliasReference.getString();
      assertTrue(aliasCounts.get(name) > 0);
      aliasCounts.put(name, aliasCounts.get(name) - 1);
    }

    // Check that there are no remaining alias definitions.
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    // Check that there are no alias usages outside of scopes.
    for (Node node : NodeUtil.getDescendants(root)) {
      if (node.getType() == Token.NAME &&
          ((AliasedNode) scopedAliases.getTraversal().getAliasUsage(node)).aliasDefinition != null) {
        Node parent = node.getParent();
        if (parent != null && parent.getType() != Token.CALL &&
            parent.getType() != Token.VAR) {
          fail("Alias usage found outside of scope: " + node.toString());
        }
      }
    }

    // Check that the alias definitions are in the correct order.
    List<String> expectedAliasOrder = Lists.newArrayList("dom", "DIV");
    List<String> actualAliasOrder = Lists.newArrayList();
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      actualAliasOrder.add(aliasDefinition.getString());
    }
    assertTrue(actualAliasOrder.equals(expectedAliasOrder));

    // Check that the scope calls are processed in the correct order.
    List<Node> expectedScopeCallsOrder = Lists.newArrayList(
        NodeUtil.findNodeBySourcePosition(root,
            new SourcePosition<AliasTransformation>(0, 0)),
        NodeUtil.findNodeBySourcePosition(root,
            new SourcePosition<AliasTransformation>(0, 31)));
    List<Node> actualScopeCallsOrder = Lists.newArrayList();
    for (Node scopeCall : scopeCalls) {
      actualScopeCallsOrder.add(scopeCall);
    }
    assertTrue(actualScopeCallsOrder.equals(expectedScopeCallsOrder));

    // Check that no errors were reported.
    assertFalse(diagnostics.getDiagnostics().isEmpty());
    for (JSError diagnostic : diagnostics.getDiagnostics()) {
      fail("Unexpected error: " + diagnostic.getMessage());
    }
  }
}