package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createTestNode("test.js");
    Node externs = createTestNode("externs.js");

    scopedAliases.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createTestNode("test.js");
    Node originalRoot = createTestNode("test.js");

    scopedAliases.hotSwapScript(root, originalRoot);
  }

  @Test
  public void testApplyAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");

    Node aliasCall = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition);

    List<Node> scopeCalls = Lists.newArrayList();

    Map<String, Var> aliases = Maps.newHashMap();
    aliases.put("goog", new Var("goog", null, null, null, null, null, null, null, null, null));

    when(preprocessorSymbolTable.getVar("goog")).thenReturn(aliases.get("goog"));

    NodeTraversal traversal = createMock(NodeTraversal.class);

    when(compiler.reportCodeChange()).thenReturn(true);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, traversal);

    Node aliasUsage = traversal.getAliasUsages().get(0);

    assertEquals(aliasDefinition, aliasUsage.getAliasedNode());

    Node aliasDefinitionInOrder = aliasDefinitionsInOrder.get(0);
    assertEquals(aliasDefinitionInOrder, aliasUsage.getAliasedNode());
  }

  @Test
  public void testApplyAliasType() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");

    Node aliasCall = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition);

    Map<String, Var> aliases = Maps.newHashMap();
    aliases.put("goog.dom", new Var("goog.dom", null, null, null, null, null, null, null, null, null));

    when(preprocessorSymbolTable.getVar("goog.dom")).thenReturn(aliases.get("goog.dom"));

    NodeTraversal traversal = createMock(NodeTraversal.class);

    when(compiler.reportCodeChange()).thenReturn(true);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, traversal);

    Node aliasUsage = traversal.getAliasUsages().get(0);

    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;

    assertEquals("goog.dom", aliasedTypeNode.getAliasedType());
  }

  @Test
  public void testNoAliasDefinition() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");

    Node aliasCall = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    assertNull(scopedAliases.getAliasDefinitionsInOrder().get(0));
  }

  @Test
  public void testNoAliasUsage() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");

    Node aliasCall = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    assertNull(scopedAliases.getAliasUsages().get(0));
  }

  @Test
  public void testNoScopeCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");

    Node aliasCall = createTestNode("test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    assertNull(scopedAliases.getScopeCalls().get(0));
  }

  @Test
  public void testScopeCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition = createTestNode("alias.js");
    Node aliasReference = createTestNode("test.js");
    Node scopeCall = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition);

    List<Node> scopeCalls = Lists.newArrayList();
    scopeCalls.add(scopeCall);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    assertEquals(scopeCall, scopedAliases.getScopeCalls().get(0));
  }

  @Test
  public void testErrorGoogScopeUsedImproperly() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node googScopeCall = createTestNode("test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), anyString());
  }

  @Test
  public void testErrorGoogScopeHasBadParameters() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node googScopeCall = createTestNode("test.js", new String[]{"param"}, new int[]{-1});

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), anyString());
  }

  @Test
  public void testErrorGoogScopeRefersToThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node googScopeCall = createTestNode("test.js", null, null);
    Node thisReference = createTestNode("test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), anyString());
  }

  @Test
  public void testErrorGoogScopeUsesReturn() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node googScopeCall = createTestNode("test.js", null, null);
    Node returnStatement = createTestNode("test.js", null, Token.RETURN);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), anyString());
  }

  @Test
  public void testErrorGoogScopeUsesThrow() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node googScopeCall = createTestNode("test.js", null, null);
    Node throwStatement = createTestNode("test.js", null, Token.THROW);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), anyString());
  }

  @Test
  public void testErrorAliasRedefined() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node aliasDefinition1 = createTestNode("alias.js", null, null);
    Node aliasDefinition2 = createTestNode("alias.js", null, null);
    Node aliasReference = createTestNode("test.js");

    Node aliasUsage1 = createTestNode("test.js");
    Node aliasUsage2 = createTestNode("test.js");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    aliasDefinitionsInOrder.add(aliasDefinition1);
    aliasDefinitionsInOrder.add(aliasDefinition2);

    List<AliasUsage> aliasUsages = Lists.newArrayList();
    aliasUsages.add(new AliasedNode(aliasUsage1, aliasDefinition1));
    aliasUsages.add(new AliasedNode(aliasUsage2, aliasDefinition2));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(eq(GOOG_SCOPE_ALIAS_REDEFINED), containsString("alias"));
  }

  @Test
  public void testErrorNonAliasLocal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    Node root = createTestNode("test.js");
    Node nonAliasLocal = createTestNode("test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.traverse(root, createMock(NodeTraversal.class));

    verify(compiler).report(any(DiagnosticType.class), containsString("local variable"));
  }

  private Node createTestNode(String fileName, String[] parameters, int[] lineAndCol) {
    Node node = new Node();
    node.setSourceFileName(fileName);
    node.setType(Token.CALL);
    Node nameNode = new Node();
    nameNode.setString("goog.scope");
    node.setFirstChild(nameNode);

    if (parameters != null) {
      Node anonymousFnNode = new Node();
      anonymousFnNode.setType(Token.FUNCTION);
      anonymousFnNode.setName("anonymousFunction");
      Node parametersNode = new Node();
      parametersNode.setType(Token.LIST);
      for (String parameter : parameters) {
        Node parameterNode = new Node();
        parameterNode.setType(Token.NAME);
        parameterNode.setString(parameter);
        parametersNode.addChild(parameterNode);
      }
      anonymousFnNode.setFirstChild(parametersNode);
      node.setLastChild(anonymousFnNode);
    }

    if (lineAndCol != null) {
      node.setSourcePosition(new SourcePosition<Object>(null, lineAndCol[0], lineAndCol[1]));
    }

    return node;
  }

  private Node createTestNode(String fileName) {
    Node node = new Node();
    node.setSourceFileName(fileName);
    return node;
  }

  private Node createMock(Class<?> clazz) {
    return mock(clazz);
  }
}