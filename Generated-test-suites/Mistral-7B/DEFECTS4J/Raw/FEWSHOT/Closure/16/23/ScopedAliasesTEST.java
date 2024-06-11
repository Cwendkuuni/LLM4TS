package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createNode("goog.scope(function() { var x = goog.x; })");

    scopedAliases.process(null, root);

    // Check that the alias definition was removed.
    assertFalse(root.hasChildWithName("goog.x"));

    // Check that the alias usage was replaced with the definition.
    Node xNode = root.getChildWithName("x");
    assertSame(root.getChildWithName("goog.x"), xNode);
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = goog.x; })");
    Node originalRoot = root;

    scopedAliases.hotSwapScript(root, originalRoot);

    // Check that the alias definition was removed.
    assertFalse(root.hasChildWithName("goog.x"));

    // Check that the alias usage was replaced with the definition.
    Node xNode = root.getChildWithName("x");
    assertSame(root.getChildWithName("goog.x"), xNode);
  }

  @Test
  public void testFindAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createNode("goog.scope(function() { var x = goog.x; var y = goog.y; })");

    scopedAliases.process(null, root);

    // Check that the aliases were added to the map.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.x", aliasDefinitionsInOrder.get(0).getQualifiedName());
    assertEquals("goog.y", aliasDefinitionsInOrder.get(1).getQualifiedName());
  }

  @Test
  public void testApplyAlias() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasDefinition = createNode("goog.x");
    Node aliasReference = createNode("x");

    Node aliasUsage = new AliasedNode(aliasReference, aliasDefinition);
    scopedAliases.getAliasUsages().add(aliasUsage);

    aliasUsage.applyAlias();

    // Check that the alias reference was replaced with the definition.
    assertEquals(aliasDefinition, aliasReference.getParent());
  }

  @Test
  public void testApplyAliasType() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasDefinition = createNode("goog.Foo");
    Node aliasReference = createNode("Foo");

    Node aliasUsage = new AliasedTypeNode(aliasReference, "BarFoo");
    scopedAliases.getAliasUsages().add(aliasUsage);

    aliasUsage.applyAlias();

    // Check that the alias reference was replaced with the definition.
    assertEquals("BarFoo", aliasReference.getString());
  }

  @Test
  public void testHasErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { return; })");
    scopedAliases.process(null, root);

    // Check that there were no errors.
    assertFalse(scopedAliases.hasErrors());

    root.setString("goog.scope(function() { var x = goog.x; })");
    scopedAliases.process(null, root);

    // Check that there was an error for the bad return statement.
    assertTrue(scopedAliases.hasErrors());
  }

  private Node createNode(String code) {
    RhinoCompiler compiler = new RhinoCompiler();
    compiler.compile(code);
    return compiler.getRootNode();
  }
}