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
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);

    // Test that no errors are reported when there are no aliases.
    Node root = Node.createString("function main() {}");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test that errors are reported when there are bad uses of goog.scope.
    Node rootWithBadScopeUsage = Node.createString(
        "function main() {" +
            "goog.scope(function() {" +
            "this.x = 1;" +
            "});" +
            "return x;" +
            "}");
    scopedAliases.process(null, rootWithBadScopeUsage);
    assertTrue(scopedAliases.hasErrors());

    // Test that aliases are correctly identified and applied.
    Node rootWithAliases = Node.createString(
        "function main() {" +
            "goog.scope(function() {" +
            "var goog = {};" +
            "var dom = goog.dom; // No error because 'dom' is defined inside the scope." +
            "goog.dom = function() {}; // Error because 'goog.dom' is reassigned." +
            "goog.x = goog.dom; // Error because 'goog.x' is assigned a value more than once." +
            "var y = goog.x; // No error because 'y' is not an alias." +
            "goog.x = 1; // No error because 'goog.x' is reassigned outside of the scope." +
            "return y;" +
            "});" +
            "return x;" +
            "}");
    scopedAliases.process(null, rootWithAliases);
    assertTrue(scopedAliases.hasErrors());
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("function() {}", aliasDefinitionsInOrder.get(1).getChildAtIndex(1).getString());
    assertEquals("goog.x", aliasDefinitionsInOrder.get(2).getString());
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
      assertSame(aliasDefinitionsInOrder.get(1), aliasedNode);
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected type of alias usage.");
    }
  }

  @Test
  public void testTraversalOrder() {
    Node root = Node.createString(
        "function main() {" +
            "goog.scope(function() {" +
            "var x = 1;" +
            "goog.scope(function() {" +
            "var y = 2;" +
            "goog.scope(function() {" +
            "var z = 3;" +
            "})});" +
            "})};" +
            "return x;" +
            "}");
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals("z", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("function() {}", aliasDefinitionsInOrder.get(1).getChildAtIndex(1).getString());
    assertEquals("y", aliasDefinitionsInOrder.get(2).getString());
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
      if (aliasedNode.getParent().isFunction()) {
        Node parentFunction = aliasedNode.getParent();
        Node parentScopeCall = parentFunction.getParent();
        assertEquals("goog.scope", parentScopeCall.getFirstChild().getNodeValue());
        assertEquals("function() {}", parentFunction.getChildAtIndex(0).getString());
      } else {
        fail("Unexpected parent type for alias usage.");
      }
    } else {
      fail("Unexpected type of alias usage.");
    }
  }

  @Test
  public void testHotSwap() {
    // TODO: Implement hot swapping test.
  }

  @Test
  public void testPreprocessorSymbolTable() {
    // TODO: Implement preprocessor symbol table test.
  }
}