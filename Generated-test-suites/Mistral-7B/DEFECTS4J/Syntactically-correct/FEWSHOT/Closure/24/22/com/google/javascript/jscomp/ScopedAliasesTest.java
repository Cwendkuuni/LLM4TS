package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private Traversal traversal;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.EXTERNAL_REASSIGN);
    compiler = new Compiler();
    compiler.init(options);

    sourceFile = new SourceFile("test.js", "test");
  }

  @Test
  public void testEmpty() {
    Node root = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testUsedImproperly() {
    Node root = Node.newBlock(Node.newCall(
        Node.newIdentifier("goog"),
        Node.newIdentifier("scope"),
        Node.newCall(Node.newIdentifier("console"), Node.newIdentifier("log"))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));
  }

  @Test
  public void testHasBadParameters() {
    Node root = Node.newBlock(Node.newCall(
        Node.newIdentifier("goog"),
        Node.newIdentifier("scope"),
        Node.newCall(Node.newIdentifier("console"), Node.newIdentifier("log")),
        Node.newIdentifier("param")));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));
  }

  @Test
  public void testReferencesThis() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newLambda(Node.newIdentifier("this"))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));
  }

  @Test
  public void testUsesReturn() {
    Node root = Node.newBlock(Node.newCall(Node.newIdentifier("goog"),
        Node.newIdentifier("scope"), Node.newReturn(Node.newString("foo"))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));
  }

  @Test
  public void testUsesThrow() {
    Node root = Node.newBlock(Node.newCall(Node.newIdentifier("goog"),
        Node.newIdentifier("scope"), Node.newThrow(Node.newString("foo"))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));
  }

  @Test
  public void testAliasRedefined() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newVar(Node.newIdentifier("foo"), Node.newString("bar")),
                Node.newReturn(Node.newIdentifier("foo")))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(root, aliasDefinitionsInOrder.get(0));

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    assertTrue(scopedAliases.hasErrors());
    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("foo", aliasUsage.getIdentifier().getName());

    Node aliasDefinition = aliasUsage.getNode();
    assertEquals("bar", aliasDefinition.getLastChild().getString());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diag = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diag.getTypeName());
    assertEquals("foo", diag.getMessage());
  }

  @Test
  public void testAliasInLocalVariable() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newVar(Node.newIdentifier("x"), Node.newIdentifier("foo")),
                Node.newReturn(Node.newIdentifier("x")))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    assertTrue(scopedAliases.hasErrors());
    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diag = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diag.getTypeName());
    assertEquals("x", diag.getMessage());
  }

  @Test
  public void testApplyAlias() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newVar(Node.newIdentifier("foo"), Node.newString("bar")),
                Node.newReturn(Node.newIdentifier("foo")))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node clonedRoot = scopedAliases.processAndCloned(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(root, aliasDefinitionsInOrder.get(0));

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("foo", aliasUsage.getIdentifier().getName());

    Node aliasDefinition = aliasUsage.getNode();
    assertEquals("bar", aliasDefinition.getLastChild().getString());

    Node alias = aliasDefinition.getParent();
    assertEquals("foo", alias.getIdentifier().getName());

    assertEquals("bar", aliasDefinition.getParent().getChildAtIndex(0).getString());

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        clonedRoot.toString());
  }

  @Test
  public void testApplyAliasAndCollapseScope() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newVar(Node.newIdentifier("foo"), Node.newIdentifier("dom")),
                Node.newBlock(Node.newCall(Node.newIdentifier("foo"), Node.newIdentifier("createElement"),
                    Node.newIdentifier("goog.dom.TagName.DIV"))))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node clonedRoot = scopedAliases.processAndCloned(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(root, aliasDefinitionsInOrder.get(0));

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("foo", aliasUsage.getIdentifier().getName());

    Node aliasDefinition = aliasUsage.getNode();
    assertEquals("dom", aliasDefinition.getLastChild().getString());

    Node alias = aliasDefinition.getParent();
    assertEquals("foo", alias.getIdentifier().getName());

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        clonedRoot.toString());

    assertEquals(0, clonedRoot.getChildCount());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        clonedRoot.getFirstChild().toString());
  }

  @Test
  public void testApplyAliasAndCollapseScopeWithFunction() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newLambda(Node.newIdentifier("x"),
                Node.newBlock(Node.newCall(Node.newIdentifier("x"), Node.newIdentifier("toUpperCase"),
                    Node.newString("foo")))))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node clonedRoot = scopedAliases.processAndCloned(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(root, aliasDefinitionsInOrder.get(0));

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    assertEquals("goog.string.toUpperCase(\"foo\")", clonedRoot.toString());

    assertEquals(0, clonedRoot.getChildCount());
    assertEquals("goog.string.toUpperCase(\"foo\")", clonedRoot.getFirstChild().toString());
  }

  @Test
  public void testApplyAliasAndCollapseScopeWithThrow() {
    Node root = Node.newBlock(
        Node.newCall(Node.newIdentifier("goog"), Node.newIdentifier("scope"),
            Node.newBlock(Node.newThrow(Node.newString("foo")))));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node clonedRoot = scopedAliases.processAndCloned(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diag = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diag.getTypeName());

    assertEquals(0, clonedRoot.getChildCount());
    assertEquals("throw \"foo\";", clonedRoot.getFirstChild().toString());
  }
}