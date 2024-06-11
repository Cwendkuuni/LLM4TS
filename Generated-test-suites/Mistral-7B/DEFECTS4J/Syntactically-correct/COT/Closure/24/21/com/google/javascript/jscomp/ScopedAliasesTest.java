package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  /** The ScopedAliases instance under test. */
  private ScopedAliases scopedAliases;

  /** PreprocessorSymbolTable instance for testing. */
  @Nullable
  private PreprocessorSymbolTable preprocessorSymbolTable;

  /** AliasTransformationHandler instance for testing. */
  private AliasTransformationHandler transformationHandler;

  /** AbstractCompiler instance for testing. */
  @Nullable
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Initialize test dependencies
    this.transformationHandler = new AliasTransformationHandler() {};
    this.compiler = new Compiler() {};
    this.preprocessorSymbolTable = new PreprocessorSymbolTable() {};

    this.scopedAliases = new ScopedAliases(this.compiler, this.preprocessorSymbolTable,
        this.transformationHandler);
  }

  @Test
  public void testProcess_NoErrors() {
    Node root = new Node("root");
    this.scopedAliases.process(null, root);
    assertFalse(this.scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_WithErrors() {
    Node root = new Node("root");
    this.scopedAliases.process(null, root);
    assertTrue(this.scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_NoChanges() {
    Node root = new Node("root");
    this.scopedAliases.hotSwapScript(root, null);
    assertFalse(this.compiler.hasReportedCodeChange());
  }

  @Test
  public void testHotSwapScript_WithChanges() {
    Node root = new Node("root");
    this.scopedAliases.hotSwapScript(root, null);
    assertTrue(this.compiler.hasReportedCodeChange());
  }

  @Test(expected = AssertionError.class)
  public void testApplyAlias_NullAliasDefinition() {
    Node aliasReference = new Node("aliasReference");
    Node aliasDefinition = null;
    new ScopedAliases.AliasedNode(aliasReference, aliasDefinition).applyAlias();
  }

  @Test(expected = AssertionError.class)
  public void testApplyAlias_NullAliasReference() {
    Node aliasDefinition = new Node("aliasDefinition");
    Node aliasReference = null;
    new ScopedAliases.AliasedNode(aliasReference, aliasDefinition).applyAlias();
  }

  @Test
  public void testApplyAlias() {
    Node aliasReference = new Node("aliasReference");
    Node aliasDefinition = new Node("aliasDefinition");
    new ScopedAliases.AliasedNode(aliasReference, aliasDefinition).applyAlias();
    assertEquals(aliasDefinition, aliasReference.getParent());
  }

  @Test
  public void testApplyAliasType() {
    Node aliasReference = new Node("aliasReference");
    String correctedType = "String";
    new ScopedAliases.AliasedTypeNode(aliasReference, correctedType).applyAlias();
    assertEquals(correctedType, aliasReference.getString());
  }

  @Test
  public void testGetAliasDefinitionsInOrder() {
    List<Node> aliasDefinitions = Lists.newArrayList();
    ScopedAliases traversal = new ScopedAliases.Traversal();
    // Add some alias definitions
    for (int i = 0; i < 5; i++) {
      Node aliasDefinition = new Node("aliasDefinition" + i);
      traversal.aliasDefinitionsInOrder.add(aliasDefinition);
    }
    Collection<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(aliasDefinitions, aliasDefinitionsInOrder);
  }

  @Test
  public void testGetScopeCalls() {
    List<Node> scopeCalls = Lists.newArrayList();
    ScopedAliases traversal = new ScopedAliases.Traversal();
    // Add some scope calls
    for (int i = 0; i < 5; i++) {
      Node scopeCall = new Node("scopeCall" + i);
      traversal.scopeCalls.add(scopeCall);
    }
    List<Node> scopeCallsList = traversal.getScopeCalls();
    assertEquals(scopeCalls, scopeCallsList);
  }
}