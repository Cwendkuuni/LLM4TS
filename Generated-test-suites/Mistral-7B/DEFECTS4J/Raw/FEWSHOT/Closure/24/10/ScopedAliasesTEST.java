package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = mockNode("function foo() { goog.scope(function() { var a = 'foo'; var b = goog.dom; goog.dom.createElement('div'); }); }");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(
        "function() { return goog.dom; }",
        aliasDefinitionsInOrder.get(0).getQualifiedName());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(
        "goog.scope(function() { var a = 'foo'; var b = goog.dom; goog.dom.createElement('div'); })",
        scopeCalls.get(0).getQualifiedName());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(3, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertEquals("a", ((AliasedNode) aliasUsage1).getAliasReference().getString());
    assertEquals(
        "function() { return 'foo'; }",
        ((AliasedNode) aliasUsage1).getAliasDefinition().getQualifiedName());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertEquals("b", ((AliasedNode) aliasUsage2).getAliasReference().getString());
    assertEquals(
        "function() { return goog.dom; }",
        ((AliasedNode) aliasUsage2).getAliasDefinition().getQualifiedName());

    AliasUsage aliasUsage3 = aliasUsages.get(2);
    assertEquals("goog.dom.createElement", ((AliasedTypeNode) aliasUsage3).getAliasReference().getString());
    assertEquals(
        "function() { return goog.dom.createElement; }",
        ((AliasedTypeNode) aliasUsage3).getCorrectedType());
  }

  private Node mockNode(String code) {
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setQualifiedName("test.foo");
    node.setCode(code);
    return node;
  }

  private Traversal getTraversal() {
    return (Traversal) ((ScopedAliases) testObject).getTraversal();
  }
}

class TestObject {
  ScopedAliases testObject;

  @Before
  public void setup() {
    testObject = new ScopedAliases(mock(AbstractCompiler.class), mock(PreprocessorSymbolTable.class), mock(AliasTransformationHandler.class));
  }
}