package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ScopedAliases class.
 */
public class ScopedAliasesTest {
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    options = new CompilerOptions();
    options.setIgnoreTypes(true);
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation getTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation();
      }

      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String alias,
          String qualifiedName) {
        // Logging not implemented in tests.
      }
    };
    scopedAliases = new ScopedAliases(null, null, transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    // Given a simple goog.scope block
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then the alias should be applied
    Node googDom = root.lookupByName("goog.dom");
    assertEquals(googDom, root.lookupByName("dom"));

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testNestedAlias() {
    // Given a nested goog.scope block
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function() {" +
        "    var DIV = dom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then the nested alias should be applied
    Node googDom = root.lookupByName("goog.dom");
    Node nestedDom = root.lookupByName("dom");
    assertEquals(googDom, nestedDom);

    // And the outer goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));

    // And the inner goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testAliasWithArguments() {
    // Given a goog.scope block with function arguments
    String sourceCode = "goog.scope(function(a, b) {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV, a, b);" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then the alias should not be applied to the arguments
    Node googDom = root.lookupByName("goog.dom");
    assertNull(root.lookupByName("dom"));

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testAliasWithReturnStatement() {
    // Given a goog.scope block with a return statement
    String sourceCode = "goog.scope(function() {" +
        "  return goog.dom.createElement(Node.DIV);" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then an error should be reported
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases.compiler, root, traversal);
    assertTrue(traversal.hasErrors());

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testAliasWithThrowStatement() {
    // Given a goog.scope block with a throw statement
    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('An error occurred');" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then an error should be reported
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases.compiler, root, traversal);
    assertTrue(traversal.hasErrors());

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testAliasWithThisKeyword() {
    // Given a goog.scope block with a this keyword
    String sourceCode = "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "  this.DOM = goog.dom.TagName.DIV;" +
        "  this.createElement = goog.dom.createElement;" +
        "  this.createElement(this.DOM);" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then an error should be reported
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases.compiler, root, traversal);
    assertTrue(traversal.hasErrors());

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }

  @Test
  public void testAliasWithMultipleDefinitions() {
    // Given a goog.scope block with multiple definitions for the same alias
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = 42;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // When processing the code
    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then an error should be reported
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases.compiler, root, traversal);
    assertTrue(traversal.hasErrors());

    // And the goog.scope block should be removed
    assertNull(root.lookupByName("goog.scope"));
  }
}