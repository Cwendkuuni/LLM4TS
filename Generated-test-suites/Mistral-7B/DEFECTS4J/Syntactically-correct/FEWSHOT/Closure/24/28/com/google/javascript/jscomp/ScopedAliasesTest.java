package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock private AbstractCompiler compiler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock private AliasTransformationHandler transformationHandler;
  @InjectMocks private ScopedAliases scopedAliases;

  private Node rootNode;

  @Before
  public void setup() {
    rootNode = Node.objectLiteral();
  }

  @Test
  public void testProcessEmpty() {
    scopedAliases.process(null, rootNode);
    // No assertions here as the method is empty.
  }

  @Test
  public void testProcessWithScopeCall() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node scopeCallNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    scopeCallNode.addChild(Node.newFunction(null));
    rootNode.addChild(scopeCallNode);

    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(scopeCallNode, scopeCalls.get(0));
  }

  @Test
  public void testProcessWithAliasDefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode, aliasDefinitionsInOrder.get(0));
  }

  @Test
  public void testProcessWithAliasUsage() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    Node aliasUsageNode = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode);

    scopedAliases.process(null, rootNode);

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode, aliasUsages.get(0));
  }

  @Test
  public void testProcessWithInvalidScopeCall() {
    Node invalidScopeCallNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    rootNode.addChild(invalidScopeCallNode);

    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(0, scopeCalls.size());
  }

  @Test
  public void testProcessWithInvalidAliasDefinition() {
    Node invalidAliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    anonymousFunctionNode.addChild(Node.newReturn(Node.IDENTIFIER("this")));
    invalidAliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(invalidAliasDefinitionNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());
  }

  @Test
  public void testProcessWithInvalidAliasUsage() {
    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    Node invalidAliasUsageNode = Node.newCall(Node.IDENTIFIER("testAlias"), null);
    invalidAliasUsageNode.addChild(Node.newReturn(Node.IDENTIFIER("this")));
    rootNode.addChild(invalidAliasUsageNode);

    scopedAliases.process(null, rootNode);

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());
  }

  @Test
  public void testProcessWithAliasRedefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode1 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode1 = Node.newFunction(null);
    anonymousFunctionNode1.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode1.addChild(anonymousFunctionNode1);
    rootNode.addChild(aliasDefinitionNode1);

    Node aliasDefinitionNode2 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode2 = Node.newFunction(null);
    anonymousFunctionNode2.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode2.addChild(anonymousFunctionNode2);
    rootNode.addChild(aliasDefinitionNode2);

    scopedAliases.process(null, rootNode);

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode2, aliasDefinitionsInOrder.get(0));
  }

  @Test
  public void testProcessWithInvalidAliasName() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("1testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());
  }

  @Test
  public void testProcessWithAliasUsageAfterDefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    Node aliasUsageNode = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode, aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode, aliasUsages.get(0));
  }

  @Test
  public void testProcessWithAliasUsageBeforeDefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasUsageNode = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode);

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode, aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode, aliasUsages.get(0));
  }

  @Test
  public void testProcessWithAliasUsageBeforeDefinitionAndRedefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasUsageNode1 = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode1);

    Node aliasDefinitionNode1 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode1 = Node.newFunction(null);
    anonymousFunctionNode1.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode1.addChild(anonymousFunctionNode1);
    rootNode.addChild(aliasDefinitionNode1);

    Node aliasDefinitionNode2 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode2 = Node.newFunction(null);
    anonymousFunctionNode2.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode2.addChild(anonymousFunctionNode2);
    rootNode.addChild(aliasDefinitionNode2);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode2, aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode1, aliasUsages.get(0));
  }

  @Test
  public void testProcessWithAliasUsageAfterDefinitionAndRedefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode1 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode1 = Node.newFunction(null);
    anonymousFunctionNode1.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode1.addChild(anonymousFunctionNode1);
    rootNode.addChild(aliasDefinitionNode1);

    Node aliasUsageNode1 = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode1);

    Node aliasDefinitionNode2 = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode2 = Node.newFunction(null);
    anonymousFunctionNode2.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode2.addChild(anonymousFunctionNode2);
    rootNode.addChild(aliasDefinitionNode2);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode2, aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode1, aliasUsages.get(0));
  }

  @Test
  public void testProcessWithAliasUsageAfterDefinitionAndNoRedefinition() {
    when(preprocessorSymbolTable.logAliasTransformation("test.js", null))
        .thenReturn(new AliasTransformation("testAlias", "TestAlias"));

    Node aliasDefinitionNode = Node.newCall(Node.IDENTIFIER("goog.scope"), null);
    Node anonymousFunctionNode = Node.newFunction(null);
    anonymousFunctionNode.addChild(Node.newVariable("testAlias", null, null));
    aliasDefinitionNode.addChild(anonymousFunctionNode);
    rootNode.addChild(aliasDefinitionNode);

    Node aliasUsageNode = Node.newCall(Node.IDENTIFIER("TestAlias"), null);
    rootNode.addChild(aliasUsageNode);

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionNode, aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(aliasUsageNode, aliasUsages.get(0));
  }
}