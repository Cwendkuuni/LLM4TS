package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Test cases for ScopedAliases class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases_BasicFunctionality() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});");

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    // Check the order of aliasDefinitionsInOrder.
    for (int i = 0; i < aliasDefinitionsInOrder.size(); ++i) {
      Node aliasDefinition = aliasDefinitionsInOrder.get(i);
      String aliasName = aliasDefinition.getString();
      Var aliasVar = preprocessorSymbolTable.getVar(aliasName);
      String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
      AliasTransformation aliasTransformation = transformationHandler.getAliasTransformation(aliasName);
      assertTrue(aliasTransformation.getAliases().contains(aliasName));
      assertTrue(aliasTransformation.getAliases().get(aliasName).equals(qualifiedName));
      Node aliasedNode = aliasUsages.get(i).applyAlias();
      assertEquals(qualifiedName, aliasedNode.getQualifiedName());
    }

    // Check the scopeCalls.
    for (Node scopeCall : scopeCalls) {
      assertTrue(isCallToScopeMethod(scopeCall));
    }

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliases_ImproperlyUsed() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.scope(function() {\n" +
        "    goog.dom.createElement(DIV);\n" +
        "  });\n" +
        "});");

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrorNodes();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getDiagnosticType());
  }

  @Test
  public void testScopedAliases_HasBadParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function(x) {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});");

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrorNodes();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0).getDiagnosticType());
  }

  @Test
  public void testScopedAliases_RefersToThis() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "  goog.dom.createElement('DIV');\n" +
        "});");

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrorNodes();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0).getDiagnosticType());
  }

  @Test
  public void testScopedAliases_UsesReturn() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function() {\n" +
        "  return goog.dom.createElement('DIV');\n" +
        "});");

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrorNodes();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0).getDiagnosticType());
  }

  @Test
  public void testScopedAliases_UsesThrow() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newTextNode("goog.scope(function() {\n" +
        "  throw new Error('Test error');\n" +
        "});");

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrorNodes();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0).getDiagnosticType());
  }

  private static boolean isCallToScopeMethod(Node node) {
    return node.getType() == Token.CALL &&
        SCOPED_ALIASES_CLASS.getName().equals(node.getFirstChild().getQualifiedName());
  }

  private static final Class<ScopedAliases> SCOPED_ALIASES_CLASS = ScopedAliases.class;

  private static DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY;
  private static DiagnosticType GOOG_SCOPE_HAS_BAD_PARAMETERS =
      ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS;
  private static DiagnosticType GOOG_SCOPE_REFERENCES_THIS =
      ScopedAliases.GOOG_SCOPE_REFERENCES_THIS;
  private static DiagnosticType GOOG_SCOPE_USES_RETURN =
      ScopedAliases.GOOG_SCOPE_USES_RETURN;
  private static DiagnosticType GOOG_SCOPE_USES_THROW =
      ScopedAliases.GOOG_SCOPE_USES_THROW;
}