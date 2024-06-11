package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Unit tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});" +
        "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Node root = AbstractCompiler.parseInitialScript(sourceCode, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        new NoOpCompiler(null), null, handler);
    scopedAliases.process(null, root);

    // Check no errors.
    assertEquals(0, scopedAliases.getNumErrors());

    // Check the output code.
    Node expectedOutput = AbstractCompiler.parseInitialScript(
        "goog.dom.createElement(goog.dom.TagName.DIV);", null, null);
    Node actualOutput = root.getFirstChild();
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    // Check that the alias definitions have been removed.
    assertFalse(root.hasChildWithName("goog.scope"));

    // Check that the alias uses have been replaced.
    Node actualAliasUse = root.getFirstChild();
    Node expectedAliasUse = AbstractCompiler.parseInitialScript(
        "goog.dom.createElement(goog.dom.TagName.DIV);", null, null)
        .getFirstChild();
    assertEquals(expectedAliasUse.toString(), actualAliasUse.toString());

    // Check that the alias definitions have been processed in the correct order.
    List<Node> aliasDefinitions = Lists.newArrayList();
    for (Node node : scopedAliases.getAliasDefinitionsInOrder()) {
      aliasDefinitions.add(node);
    }
    assertEquals(
        "goog.dom.TagName.DIV",
        ((Var) nodeGetInitialValue(aliasDefinitions.get(0))).getName());
    assertEquals(
        "goog.dom",
        ((Var) nodeGetInitialValue(aliasDefinitions.get(1))).getName());

    // Check that the scope calls have been removed.
    assertFalse(root.hasChildWithName("goog.scope"));
  }

  @Test
  public void testScopedAliasesError() {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var x = goog.x;" +
        "});";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Node root = AbstractCompiler.parseInitialScript(sourceCode, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        new NoOpCompiler(null), null, handler);
    scopedAliases.process(null, root);

    // Check that an error was reported.
    assertEquals(1, scopedAliases.getNumErrors());

    // Check the error message.
    Node errorNode = root.getFirstChild();
    assertEquals(
        "The local variable x is in a goog.scope and is not an alias.",
        errorNode.getJSDocInfo().getMessage());
  }

  @Test
  public void testScopedAliasesReusedAlias() {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var x = goog.x;" +
        "  var y = goog.x;" +
        "});";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Node root = AbstractCompiler.parseInitialScript(sourceCode, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        new NoOpCompiler(null), null, handler);
    scopedAliases.process(null, root);

    // Check that two errors were reported.
    assertEquals(2, scopedAliases.getNumErrors());

    // Check the error messages.
    List<Node> errorNodes = Lists.newArrayList();
    for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
      if (node.isError()) {
        errorNodes.add(node);
      }
    }
    assertEquals(
        "The alias x is assigned a value more than once.",
        errorNodes.get(0).getJSDocInfo().getMessage());
    assertEquals(
        "The local variable y is in a goog.scope and is not an alias.",
        errorNodes.get(1).getJSDocInfo().getMessage());
  }

  @Nullable
  private static Object nodeGetInitialValue(Node node) {
    if (node.isVar()) {
      return ((Var) node).getValue();
    }
    return null;
  }
}