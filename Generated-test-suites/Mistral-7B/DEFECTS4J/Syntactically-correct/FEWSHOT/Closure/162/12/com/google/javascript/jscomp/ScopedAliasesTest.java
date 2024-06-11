package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ScriptLoader;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.UniqueIdGenerator;

import javax.annotation.Nullable;

/**
 * Test class for ScopedAliases class.
 */
public class ScopedAliasesTest {

  /**
   * Test case for valid usage of goog.scope.
   */
  @Test
  public void testValidUsage() {
    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "})();";

    ScriptLoader loader = new ScriptLoader();
    UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator();
    Node rootNode = loader.compile(sourceCode, "test.js", 1, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(new AbstractCompiler(null, null), rootNode, traversal);
    assertTrue(traversal.hasErrors());
    assertFalse(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertFalse(traversal.getScopeCalls().isEmpty());

    // Check if the aliases are defined in the correct order
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    String[] expectedAliases = {"goog.dom", "dom.TagName"};
    for (int i = 0; i < expectedAliases.length; ++i) {
      assertEquals(expectedAliases[i], aliasDefinitions.get(i).getString());
    }

    // Check if the scope calls are in the correct order
    List<Node> scopeCalls = traversal.getScopeCalls();
    Node expectedScopeCall = scopeCalls.get(0);
    Node actualScopeCall = traversal.getScopeCalls().get(0);
    assertEquals(expectedScopeCall, actualScopeCall);

    // Check if the aliases are used correctly
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        Node aliasReference = aliasedNode.aliasReference;
        Node aliasDefinition = aliasedNode.aliasDefinition;
        assertEquals(aliasDefinition.getQualifiedName(),
            aliasReference.getParent().getFirstChild().getQualifiedName());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        Node aliasReference = aliasedTypeNode.aliasReference;
        String expectedType = aliasDefinition.getQualifiedName() +
            aliasedTypeNode.correctedType.substring(aliasDefinition.getQualifiedName().length());
        assertEquals(expectedType, aliasReference.getString());
      }
    }
  }

  /**
   * Test case for invalid usage of goog.scope.
   */
  @Test
  public void testInvalidUsage() {
    String sourceCode =
        "goog.scope(function() {" +
            "//valid usage comment 1" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            //invalid usage comment 1
            "goog.scope(function() {" +
            "});" +
            //invalid usage comment 2
            "var x = 5;" +
            "})();";

    ScriptLoader loader = new ScriptLoader();
    UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator();
    Node rootNode = loader.compile(sourceCode, "test.js", 1, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(new AbstractCompiler(null, null), rootNode, traversal);
    assertTrue(traversal.hasErrors());

    // Check if the errors are reported correctly
    List<Node> errors = traversal.getErrors();
    assertEquals(3, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        errors.get(0).getMessage());
    assertEquals(1, errors.get(0).getLineNumber());
    assertEquals(1, errors.get(0).getColumnNumber());

    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(1).getType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        errors.get(1).getMessage());
    assertEquals(3, errors.get(1).getLineNumber());
    assertEquals(1, errors.get(1).getColumnNumber());

    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(2).getType());
    assertEquals("The local variable x is in a goog.scope and is not an alias.",
        errors.get(2).getMessage());
    assertEquals(5, errors.get(2).getLineNumber());
    assertEquals(5, errors.get(2).getColumnNumber());
  }

  private DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = new DiagnosticType(
      "JSC_GOOG_SCOPE_USED_IMPROPERLY");
  private DiagnosticType GOOG_SCOPE_NON_ALIAS_LOCAL = new DiagnosticType(
      "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");

  private static final DiagnosticType[] GOOG_SCOPE_ERRORS = {
    GOOG_SCOPE_USED_IMPROPERLY,
    GOOG_SCOPE_HAS_BAD_PARAMETERS,
    GOOG_SCOPE_REFERENCES_THIS,
    GOOG_SCOPE_USES_RETURN,
    GOOG_SCOPE_USES_THROW,
    GOOG_SCOPE_ALIAS_REDEFINED,
    GOOG_SCOPE_NON_ALIAS_LOCAL
  };

  private static final SourcePosition<DiagnosticType> GOOG_SCOPE_POSITION =
      new SourcePosition<DiagnosticType>() {};

  private static final int GOOG_SCOPE_LINE_NUMBER = 1;
  private static final int GOOG_SCOPE_COLUMN_NUMBER = 1;

  /**
   * Diagnostic type enum.
   */
  private static enum DiagnosticType {
    GOOG_SCOPE_USED_IMPROPERLY,
    GOOG_SCOPE_HAS_BAD_PARAMETERS,
    GOOG_SCOPE_REFERENCES_THIS,
    GOOG_SCOPE_USES_RETURN,
    GOOG_SCOPE_USES_THROW,
    GOOG_SCOPE_ALIAS_REDEFINED,
    GOOG_SCOPE_NON_ALIAS_LOCAL
  }
}