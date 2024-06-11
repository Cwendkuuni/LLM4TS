package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testProcess() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    Compiler compiler = new Compiler(options);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "})();";

    Node root = compiler.parseTree(input, "test.js", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom", aliasDefinitions.get(0).getLastChild().getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("dom", aliasedNode.aliasReference.getString());
        assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("dom", aliasedTypeNode.aliasReference.getString());
        assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);
      }
    }

    assertFalse(scopedAliases.getTraversal().hasErrors());
  }

  @Test
  public void testProcessWithError() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    Compiler compiler = new Compiler(options);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "dom.createElement(DIV);" +
        "});" +
        "})();";

    Node root = compiler.parseTree(input, "test.js", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom", aliasDefinitions.get(0).getLastChild().getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("dom", aliasedNode.aliasReference.getString());
        assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("dom", aliasedTypeNode.aliasReference.getString());
        assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);
      }
    }

    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(1, scopedAliases.getTraversal().getErrorCount());
    DiagnosticType error =
        scopedAliases.getTraversal().getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", error.getName());
  }

  @Test
  public void testProcessWithNoAliasDefinitions() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    Compiler compiler = new Compiler(options);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {})();" +
        "dom.createElement(DIV);" +
        "})();";

    Node root = compiler.parseTree(input, "test.js", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(0, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());

    assertFalse(scopedAliases.getTraversal().hasErrors());
  }

  @Test
  public void testProcessWithBadAliasDefinition() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    Compiler compiler = new Compiler(options);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "dom.createElement(DIV);" +
        "var dom = goog.dom;" +
        "})();" +
        "})();";

    Node root = compiler.parseTree(input, "test.js", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom", aliasDefinitions.get(0).getLastChild().getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("dom", aliasedNode.aliasReference.getString());
        assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("dom", aliasedTypeNode.aliasReference.getString());
        assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);
      }
    }

    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(1, scopedAliases.getTraversal().getErrorCount());
    DiagnosticType error =
        scopedAliases.getTraversal().getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testProcessWithUnusedAliasDefinition() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    Compiler compiler = new Compiler(options);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {});" +
        "dom.createElement(DIV);" +
        "var unused = goog.dom.createElement(DIV);" +
        "})();";

    Node root = compiler.parseTree(input, "test.js", null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom", aliasDefinitions.get(0).getLastChild().getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    assertEquals(2, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("dom", aliasedNode.aliasReference.getString());
        assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("dom", aliasedTypeNode.aliasReference.getString());
        assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);
      }
    }

    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(1, scopedAliases.getTraversal().getErrorCount());
    DiagnosticType error =
        scopedAliases.getTraversal().getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getName());
  }
}