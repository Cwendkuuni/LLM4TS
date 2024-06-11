package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
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

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_ValidScopedFunctionBlock() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});");

    scopedAliases.process(null, root);

    // TODO: Add assertions for the changed root node and other testable state.
  }

  @Test(expected = AssertionError.class)
  public void testProcess_ImproperlyUsedGoogScopeCall() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "function test() {}" +
            "goog.scope(function() {});" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallWithBadParameters() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "goog.scope(123);" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallWithFunctionBody() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "function test() {}" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallUsesThis() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "this.test = 123;" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallUsesReturn() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "return 123;" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallUsesThrow() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "throw new Error('Test Error');" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallAliasesRedefined() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "goog.scope(function() {" +
                "var dom = goog.dom;" +
                "var DIV = goog.dom.TagName.DIV;" +
            "});" +
            "goog.scope(function() {" +
                "var dom = goog.dom;" +
                "var DIV = goog.dom.TagName.DIV;" +
            "});" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GoogScopeCallNonAliasLocal() {
    Node root = Node.outermostScript(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var test = 123;" +
            "});");

    scopedAliases.process(null, root);
  }

  @Test
  public void testHotSwapScript() {
    // TODO: Add test for hotSwapScript method.
  }

  @Test
  public void testApplyAlias() {
    Node aliasReference = Node.name("dom");
    Node aliasDefinition = Node.name("goog.dom");
    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);

    aliasedNode.applyAlias();

    assertEquals("goog.dom", aliasReference.getString());
  }

  @Test
  public void testApplyAliasType() {
    Node aliasReference = Node.name("DOM");
    String correctedType = "goog.dom.DOM";
    AliasedTypeNode aliasedNode = new AliasedTypeNode(aliasReference, correctedType);

    aliasedNode.applyAlias();

    assertEquals(correctedType, aliasReference.getString());
  }
}