package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.MockitoAnnotations;

/**
 * Tests for the ScopedAliases class.
 */
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
  public void testProcess_NoAliases() {
    Node root = createTestNode("function foo() {}");
    scopedAliases.process(null, root);

    // No errors were reported.
    MockitoAnnotations.verifyNoMoreInteractions(compiler);

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_SingleAlias() {
    Node root = createTestNode("goog.scope(function() {" +
        "  var DOM = goog.dom; " +
        "  DOM.createElement('div'); " +
        "});");
    scopedAliases.process(null, root);

    // No errors were reported.
    MockitoAnnotations.verifyNoMoreInteractions(compiler);

    // The alias was applied.
    Node domElementNode = root.getFirstChild().getLastChild().getLastChild();
    assertEquals("goog.dom.createElement", domElementNode.getQualifiedName());

    // The alias definition was removed.
    assertEquals(0, root.getFirstChild().getChildCount());

    // The goog.scope block was collapsed.
    assertEquals("goog.dom.createElement('div')", root.toSource());
  }

  @Test
  public void testProcess_MultipleAliases() {
    Node root = createTestNode("goog.scope(function() {" +
        "  var DOM = goog.dom; " +
        "  var DIV = DOM.TagName.DIV; " +
        "  DOM.createElement(DIV); " +
        "});");
    scopedAliases.process(null, root);

    // No errors were reported.
    MockitoAnnotations.verifyNoMoreInteractions(compiler);

    // The aliases were applied.
    assertEquals("goog.dom.TagName.DIV", root.getFirstChild().getLastChild().getLastChild()
        .getLastChild().getQualifiedName());
    assertEquals("goog.dom.createElement", root.getLastChild().getQualifiedName());

    // The alias definitions were removed.
    assertEquals(0, root.getFirstChild().getChildCount());

    // The goog.scope block was collapsed.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toSource());
  }

  @Test
  public void testProcess_InvalidGoogScopeCall() {
    Node root = createTestNode("goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var x = 1; " +
        "  }); " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for the nested goog.scope call.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The call to goog.scope must be alone in a single statement.")));

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_InvalidGoogScopeCallWithArguments() {
    Node root = createTestNode("goog.scope(function() {" +
        "  goog.scope(function(x) {" +
        "    var y = 1; " +
        "  }); " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for the nested goog.scope call with arguments.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The call to goog.scope must take only a single parameter. " +
            "It must be an anonymous function that itself takes no parameters.")));

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_InvalidGoogScopeCallWithThis() {
    Node root = createTestNode("goog.scope(function() {" +
        "  this.x = 1; " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for using 'this' in the goog.scope block.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The body of a goog.scope function cannot reference 'this'.")));

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_InvalidGoogScopeCallWithReturn() {
    Node root = createTestNode("goog.scope(function() {" +
        "  return 1; " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for using 'return' in the goog.scope block.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The body of a goog.scope function cannot use 'return'.")));

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_InvalidGoogScopeCallWithThrow() {
    Node root = createTestNode("goog.scope(function() {" +
        "  throw new Error(); " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for using 'throw' in the goog.scope block.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The body of a goog.scope function cannot use 'throw'.")));

    // No changes were made to the input.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_DuplicateAlias() {
    Node root = createTestNode("goog.scope(function() {" +
        "  var DOM = goog.dom; " +
        "  var DOM = goog.dom; " + // Duplicate alias definition.
        "  DOM.createElement('div'); " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for the duplicate alias definition.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The alias DOM is assigned a value more than once.")));

    // The input was not changed.
    assertEquals(root.toSource(), root.toSource());
  }

  @Test
  public void testProcess_NonAliasLocalVariable() {
    Node root = createTestNode("goog.scope(function() {" +
        "  var x = 1; " + // Non-alias local variable.
        "  var DOM = goog.dom; " +
        "  DOM.createElement('div'); " +
        "});");
    scopedAliases.process(null, root);

    // An error was reported for the non-alias local variable.
    MockitoAnnotations.verify(compiler).report(ArgumentMatcher.isA(Error.class)
        .and(error -> error.getMessage().equals("The local variable x is in a goog.scope and is not an alias.")));

    // The input was not changed.
    assertEquals(root.toSource(), root.toSource());
  }

  private Node createTestNode(String code) {
    Node root = new Node(Token.BLOCK, new SourcePosition(1, 1, 1, 1), null);
    Node node = new Node(Token.SCOPE, new SourcePosition(1, 1, 1, 1), root);
    node.setString("goog.scope");
    root.addChild(node);
    node = new Node(Token.FUNCTION, new SourcePosition(2, 1, 2, 1), node);
    node.setString("function");
    node.setJSDocInfo(new JSDocInfo());
    node.addChild(node.getFirstChild());
    root.addChild(node);
    node = new Node(Token.FUNCTION, new SourcePosition(3, 1, 3, 1), root);
    node.setString("function");
    node.setJSDocInfo(new JSDocInfo());
    node.addChild(node.getFirstChild());
    Node body = new Node(Token.BLOCK, new SourcePosition(4, 1, 4, 1), node);
    node.addChild(body);
    root.addChild(node);
    root.getLastChild().setString(code);
    return root;
  }
}