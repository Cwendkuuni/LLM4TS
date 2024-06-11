package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for the {@code ScopedAliases} class.
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
  public void testProcess_NoScopedAliases() {
    Node root = Node.createTree("");
    scopedAliases.process(null, root);
    verifyNoErrors();
  }

  @Test
  public void testProcess_ScopedAliases() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; })"),
        "bar.js", 1);

    scopedAliases.process(null, root);
    verifyAliasTransformation("foo", "bar");
  }

  @Test
  public void testHotSwapScript_NoScopedAliases() {
    Node root = Node.createTree("function foo() {}");
    scopedAliases.hotSwapScript(root, null);
    verifyNoErrors();
  }

  @Test
  public void testHotSwapScript_ScopedAliases() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; })"),
        "bar.js", 1);

    Node originalRoot = Node.createTree("function foo() {}");
    scopedAliases.hotSwapScript(root, originalRoot);
    verifyAliasTransformation("foo", "bar");
  }

  @Test
  public void testApplyAliasUsages() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; foo.baz = 42; })"),
        "bar.js", 1);

    scopedAliases.process(null, root);

    verifyAliasApplication("foo", Node.createTree("bar"));
    verifyAliasApplication("baz", Node.createTree("baz"));
  }

  @Test
  public void testRemoveAliasDefinitions() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; })"),
        "bar.js", 1);

    Node aliasDefinition = Node.createTree("var bar = 42;");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertSame(aliasDefinition, aliasDefinitionsInOrder.get(0));

    scopedAliases.hotSwapScript(root, null);

    List<Node> aliasDefinitionsInOrderAfterRemoval =
        scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrderAfterRemoval.size());
  }

  @Test
  public void testCollapseScopes() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; })"),
        "bar.js", 1);

    Node aliasDefinition = Node.createTree("var bar = goog.foo;");
    Node scopeCall = Node.createTree("goog.scope(function() { foo.baz = 42; })");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    listEquals(aliasDefinitionsInOrder, Lists.newArrayList(aliasDefinition));

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    listEquals(scopeCalls, Lists.newArrayList(scopeCall));

    scopedAliases.hotSwapScript(root, null);

    List<Node> aliasDefinitionsInOrderAfterCollapse =
        scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrderAfterCollapse.size());

    List<Node> scopeCallsAfterCollapse = scopedAliases.getScopeCalls();
    listEquals(
        scopeCallsAfterCollapse,
        Lists.newArrayList(Node.createTree("goog.foo.baz = 42;")));
  }

  @Test
  public void testReportErrors() {
    Node root = Node.createTree("goog.scope(function() { this.foo = 42; })");

    scopedAliases.process(null, root);

    verifyError("JSC_GOOG_SCOPE_REFERENCES_THIS");
  }

  @Test
  public void testReportErrors_Multiple() {
    Node root = Node.createTree(
        "goog.scope(function() { var foo = bar; this.foo = bar; })");

    scopedAliases.process(null, root);

    verifyError("JSC_GOOG_SCOPE_REFERENCES_THIS");
    verifyError("JSC_GOOG_SCOPE_USED_IMPROPERLY");
  }

  @Test
  public void testLogAliasTransformation() {
    Node root = createTestNode(
        Node.createTree("goog.scope(function() { var foo = bar; })"),
        "bar.js", 1);

    AliasTransformation aliasTransformation = mock(AliasTransformation.class);

    when(transformationHandler.logAliasTransformation(
        "bar.js", Node.createTree("goog.scope(function() { var foo = bar; })")))
        .thenReturn(aliasTransformation);

    scopedAliases.process(null, root);

    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);

    verify(transformationHandler).logAliasTransformation(
        eq("bar.js"), eq(Node.createTree("goog.scope(function() { var foo = bar; })")),
        aliasTransformationCaptor.capture());

    AliasTransformation capturedAliasTransformation =
        aliasTransformationCaptor.getValue();

    assertEquals("foo", capturedAliasTransformation.getAlias());
    assertEquals("bar", capturedAliasTransformation.getReplacement());
  }

  private void verifyNoErrors() {
    verify(compiler, never()).report(any());
  }

  private void verifyError(String errorMessage) {
    verify(compiler).report(eq(new DiagnosticType(errorMessage)));
  }

  private void verifyAliasTransformation(String alias, String replacement) {
    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);

    verify(transformationHandler).logAliasTransformation(
        anyString(), eq(Node.createTree("goog.scope(function() { var foo = bar; })")),
        aliasTransformationCaptor.capture());

    AliasTransformation aliasTransformation =
        aliasTransformationCaptor.getValue();

    assertEquals(alias, aliasTransformation.getAlias());
    assertEquals(replacement, aliasTransformation.getReplacement());
  }

  private void verifyAliasApplication(String alias, Node expected) {
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    AliasUsage aliasUsage = aliasUsages.get(0);

    assertEquals(alias, aliasUsage.getAlias());
    assertEquals(expected, aliasUsage.getAliasedNode());
  }

  private void listEquals(List<Node> list1, List<Node> list2) {
    assertEquals(list1.size(), list2.size());
    for (int i = 0; i < list1.size(); i++) {
      Node node1 = list1.get(i);
      Node node2 = list2.get(i);
      assertEquals(node1.getQualifiedName(), node2.getQualifiedName());
    }
  }

  private Node createTestNode(Node node, String fileName, int lineNumber) {
    SourcePosition sourcePosition = new SourcePosition();
    sourcePosition.setPositionInformation(fileName, lineNumber);
    node.setSourcePosition(sourcePosition);
    return node;
  }
}