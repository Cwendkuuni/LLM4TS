package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorsymbolTableImpl();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public void reportError(SourceFile sourceFile, int line, int column,
          String message, Object... arguments) {
        throw new RuntimeException(String.format(message, arguments));
      }

      @Override
      public AliasTransformation logAliasTransformation(String file, SourcePosition position) {
        return new AliasTransformation();
      }
    };
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testFindAliases() {
    Node root = new Node("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; goog.dom.createElement(DIV); })");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom", aliasDefinitions.get(1).getString());
  }

  @Test
  public void testFindAliasesWithJSDoc() {
    String code = "/**\n" +
        " * @namespace goog.dom\n" +
        " */\n" +
        "goog.scope(function() {\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "})";
    Node root = new Node(code);
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertEquals("goog.dom", aliasDefinitions.get(0).getString());
    assertEquals("DIV", aliasDefinitions.get(1).getString());
  }

  @Test
  public void testErrorWhenMultipleDefinitions() {
    Node root = new Node("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var dom = 'google'\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenNonAliasLocalVariable() {
    Node root = new Node("goog.scope(function() {\n" +
        "  var x = 42;\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenGoogScopeUsedImproperly() {
    Node root = new Node("goog.scope { foo(); }");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenGoogScopeHasBadParameters() {
    Node root = new Node("goog.scope(function(x) { foo(); })");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenGoogScopeRefersToThis() {
    Node root = new Node("goog.scope(function() {\n" +
        "  this.x = 42;\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenGoogScopeUsesReturn() {
    Node root = new Node("goog.scope(function() {\n" +
        "  return 42;\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", traversal.getLastError().getType().getName());
  }

  @Test
  public void testErrorWhenGoogScopeUsesThrow() {
    Node root = new Node("goog.scope(function() {\n" +
        "  throw new Error('Test');\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", traversal.getLastError().getType().getName());
  }

  @Test
  public void testApplyAliases() {
    Node root = new Node("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "})");
    NodeTraversal traversal = new NodeTraversal(root, null, null);
    scopedAliases.process(null, root);
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    AliasUsage aliasedNode1 = aliasUsages.get(0);
    Node originalNode1 = aliasedNode1.aliasReference;
    assertEquals("dom", originalNode1.getString());
    assertEquals("goog.dom", aliasedNode1.aliasDefinition.getString());
    AliasUsage aliasedNode2 = aliasUsages.get(1);
    Node originalNode2 = aliasedNode2.aliasReference;
    assertEquals("DIV", originalNode2.getString());
    assertEquals("goog.dom.TagName.DIV", aliasedNode2.aliasDefinition.getString());
  }
}

class PreprocessorsymbolTableImpl implements PreprocessorSymbolTable {
  @Override
  public void addReference(Node node) {
    // do nothing
  }
}

class AliasTransformationHandlerImpl implements AliasTransformationHandler {
  @Override
  public void reportError(SourceFile sourceFile, int line, int column, String message, Object... arguments) {
    throw new RuntimeException(String.format(message, arguments));
  }

  @Override
  public AliasTransformation logAliasTransformation(String file, SourcePosition position) {
    return new AliasTransformation();
  }
}

class AliasTransformation {
  // empty
}