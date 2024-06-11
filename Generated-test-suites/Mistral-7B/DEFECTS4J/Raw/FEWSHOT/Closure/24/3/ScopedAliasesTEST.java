package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

/**
 * Tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    compiler.setOptCompile(1);
    compiler.setOptStrict(1);
    compiler.setOptWarnings(1);
    preprocessorSymbolTable = new PreprocessorSymbolTable(compiler);
    transformationHandler = new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    Node root = Node.parse("goog.scope(function() { var a = 'foo'; var b = goog.dom; })");
    scopedAliases.process(null, root);
    assertEquals("goog.dom", root.getChildAtIndex(0).toString());
    assertSame("foo", root.getChildAtIndex(1).getFirstChild().getString());
  }

  @Test
  public void testHotSwapScript() {
    String input =
        "goog.scope(function() {" +
        "  var a = 'foo'; " +
        "  goog.scope(function() { var b = 'bar'; }); " +
        "  goog.scope(function() { var c = 'baz'; }); " +
        "})";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.dom.goog.dom", root.getChildAtIndex(0).toString());
    assertEquals("'foo'", root.getChildAtIndex(1).toString());
    assertEquals("goog.dom.goog.dom.goog.dom", root.getChildAtIndex(2).toString());
    assertSame("'bar'", root.getChildAtIndex(3).getFirstChild().getString());
    assertEquals("goog.dom.goog.dom.goog.dom.goog.dom", root.getChildAtIndex(4).toString());
    assertSame("'baz'", root.getChildAtIndex(5).getFirstChild().getString());
  }

  @Test
  public void testInvalidScopeCall() {
    String input = "goog.scope(function() { goog.scope() })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope()", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopeCallWithParameters() {
    String input = "goog.scope(function(x) { goog.scope() })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope()", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopeCallWithReturnStatement() {
    String input = "goog.scope(function() { return goog.scope() })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope()", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopeCallWithThisKeyword() {
    String input = "goog.scope(function() { this.a = 'foo' })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope(function() { this.a = 'foo' })", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopeCallWithThrowStatement() {
    String input = "goog.scope(function() { throw new Error() })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope(function() { throw new Error() })", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testAliasRedefined() {
    String input =
        "goog.scope(function() {" +
        "  var a = 'foo'; " +
        "  goog.scope(function() { var a = 'bar'; })";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope(function() {" +
                "  var a = 'foo'; " +
                "  goog.scope(function() { var a = 'bar'; })" +
                "})", root.toString());
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testAliasUsage() {
    String input =
        "goog.scope(function() {" +
        "  var a = 'foo'; " +
        "  goog.scope(function() { var b = a; })" +
        "})";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope(function() {" +
                "  var a = 'foo'; " +
                "  goog.scope(function() { var b = 'foo'; })" +
                "})", root.toString());
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testAliasUsageWithTypeNodes() {
    String input =
        "goog.scope(function() {" +
        "  var a = 'foo'; " +
        "  goog.scope(function() { var b = goog.dom; })" +
        "})";
    Node root = Node.parse(input);

    scopedAliases.hotSwapScript(root, null);
    assertEquals("goog.scope(function() {" +
                "  var a = 'foo'; " +
                "  goog.scope(function() { var b = goog.dom.goog.dom; })" +
                "})", root.toString());
    assertFalse(scopedAliases.hasErrors());
  }
}