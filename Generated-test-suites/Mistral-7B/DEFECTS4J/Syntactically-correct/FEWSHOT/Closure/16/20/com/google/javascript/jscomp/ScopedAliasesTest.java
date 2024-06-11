package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.util.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test no scoped aliases.
    String sourceCode1 = "function foo() {}";
    Node root1 = NodeUtil.parseString(sourceCode1, null, null);
    scopedAliases.process(null, root1);
    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().isEmpty());

    // Test simple scoped aliases.
    String sourceCode2 = "goog.scope(function() {" +
                         "  var dom = goog.dom;" +
                         "  var DIV = dom.TagName.DIV;" +
                         "  dom.createElement(DIV);" +
                         "});";
    Node root2 = NodeUtil.parseString(sourceCode2, null, null);
    scopedAliases.process(null, root2);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("com.google.dom", aliasDefinition.getLastChild().getQualifiedName());
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
    assertEquals("dom", aliasedNode.getString());
    assertEquals("com.google.dom", aliasedNode.getLastChild().getQualifiedName());
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    // Test multiple scoped aliases.
    String sourceCode3 = "goog.scope(function() {" +
                         "  var dom = goog.dom;" +
                         "  var DIV = dom.TagName.DIV;" +
                         "  var goog_dom = dom;" +
                         "  dom.createElement(DIV);" +
                         "});";
    Node root3 = NodeUtil.parseString(sourceCode3, null, null);
    scopedAliases.process(null, root3);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    aliasDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("com.google.dom", aliasDefinition.getLastChild().getQualifiedName());
    aliasUsage = aliasUsages.get(1);
    assertTrue(aliasUsage instanceof AliasedNode);
    aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
    assertEquals("dom", aliasedNode.getString());
    assertEquals("com.google.dom", aliasedNode.getLastChild().getQualifiedName());

    // Test scoped aliases with errors.
    String sourceCode4 = "goog.scope(function() {" +
                         "  var this = goog.dom;" + // GOOG_SCOPE_REFERENCES_THIS
                         "  var x = goog.x;" + // GOOG_SCOPE_ALIAS_REDEFINED
                         "  var x = goog.y;" + // GOOG_SCOPE_ALIAS_REDEFINED
                         "});";
    Node root4 = NodeUtil.parseString(sourceCode4, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root4);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());
    aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());
    scopeCalls = scopedAliases.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());
    assertEquals(3, scopedAliases.getDiagnostics().size());

    // Test scoped aliases with type nodes.
    String sourceCode5 = "goog.scope(function() {" +
                         "  var dom = goog.dom;" +
                         "  /** @type {number} */" +
                         "  var x = goog.dom. TagName.DIV;" +
                         "  x = 5;" +
                         "});";
    Node root5 = NodeUtil.parseString(sourceCode5, null, null);
    scopedAliases.process(null, root5);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("com.google.dom", aliasDefinition.getLastChild().getQualifiedName());
    aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedTypeNode);
    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
    assertEquals("x", aliasedTypeNode.typeReference.getString());
    assertEquals("com.google.dom.TagName.DIV", aliasedTypeNode.aliasName);
  }

  private void assertEquals(int expectedSize, Collection<Node> collection) {
    assertEquals(expectedSize, collection.size());
  }

  private void assertEquals(Object expected, Object actual) {
    assertEquals(expected, actual);
  }

  private void assertEquals(String expected, Node node) {
    assertEquals(expected, node.getString());
  }

  private void assertEquals(String expected, String nodeString) {
    assertEquals(expected, nodeString);
  }

  private void assertEquals(String expected, String nodeString, String... arguments) {
    assertEquals(expected, String.format(nodeString, arguments));
  }

  private void assertNull(Node node) {
    assertNull(node);
  }

  private void assertTrue(boolean condition) {
    assertTrue(condition);
  }

  private void assertFalse(boolean condition) {
    assertFalse(condition);
  }
}