package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.util.NodeTraversal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testProcess() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    Node expected = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    assertSame(expected, root);
  }

  @Test
  public void testProcessWithErrors() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; this.test = 42; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    Diagnostics diagnostics = compiler.getDiagnostics();
    List<String> errors = Lists.newArrayList();
    for (Node n : diagnostics.getDiagnostics()) {
      errors.add(n.getErrorMessage());
    }
    List<String> expectedErrors = Lists.newArrayList(
        "The body of a goog.scope function cannot reference 'this'",
        "JSC_GOOG_SCOPE_USES_RETURN");
    assertSame(expectedErrors, errors);
  }

  @Test
  public void testHotSwapScript() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "function createElement(tagName) { return goog.dom.createElement(tagName); } " +
        "createElement('DIV'); });");

    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());

    Node expected = Node.parse("goog.dom.createElement('DIV');");
    assertSame(expected, root);
  }

  @Test
  public void testHotSwapScriptWithErrors() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "function createElement(tagName) { return goog.dom.createElement(tagName); } " +
        "function test() { return 42; } createElement('DIV'); });");

    scopedAliases.hotSwapScript(root, null);
    assertTrue(scopedAliases.hasErrors());

    Diagnostics diagnostics = compiler.getDiagnostics();
    List<String> errors = Lists.newArrayList();
    for (Node n : diagnostics.getDiagnostics()) {
      errors.add(n.getErrorMessage());
    }
    List<String> expectedErrors = Lists.newArrayList(
        "The body of a goog.scope function cannot use 'return'",
        "JSC_GOOG_SCOPE_USES_THROW");
    assertSame(expectedErrors, errors);
  }

  @Test
  public void testApplyAlias() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "var DOMElement = dom.Element; DOMElement.DIV = DomElement.DIV || TagName.DIV; });");
    Node aliasDefinition = root.getLastChild().getLastChild();
    ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(
        root.getLastChild().getLastChild().getLastChild(), aliasDefinition);

    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());

    Node expected = Node.parse("goog.dom.Element.DIV = goog.dom.Element.DIV || com.google.javascript.jscomp.com.google.javascript.rhino.TagName.DIV;");
    assertSame(expected, aliasDefinition.getParent().getLastChild());
  }

  @Test
  public void testApplyAliasWithErrors() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "var DOMElement = goog.dom.Element; DOMElement.DIV = DomElement.DIV || TagName.DIV; " +
        "DOMElement.DIV = 42; });");
    Node aliasDefinition = root.getLastChild().getLastChild();
    ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(
        root.getLastChild().getLastChild().getLastChild(), aliasDefinition);

    scopedAliases.hotSwapScript(root, null);
    assertTrue(scopedAliases.hasErrors());

    Diagnostics diagnostics = compiler.getDiagnostics();
    List<String> errors = Lists.newArrayList();
    for (Node n : diagnostics.getDiagnostics()) {
      errors.add(n.getErrorMessage());
    }
    List<String> expectedErrors = Lists.newArrayList(
        "The alias DOMElement.DIV is assigned a value more than once");
    assertSame(expectedErrors, errors);
  }

  @Test
  public void testApplyAliasToType() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "function getElement(tagName) { return goog.dom.createElement(tagName); } " +
        "getElement.prototype.DIV = Element.prototype.DIV || TagName.DIV; });");
    Node aliasDefinition = root.getLastChild().getLastChild();
    ScopedAliases.AliasedTypeNode aliasedNode = new ScopedAliases.AliasedTypeNode(
        aliasDefinition, "Element.prototype.DIV");

    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());

    Node expected = Node.parse("getElement.prototype.DIV = Element.prototype.DIV || " +
        "com.google.javascript.jscomp.com.google.javascript.rhino.TagName.DIV;");
    assertSame(expected, aliasDefinition);
  }

  @Test
  public void testApplyAliasToTypeWithErrors() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "function getElement(tagName) { return goog.dom.createElement(tagName); } " +
        "getElement.prototype.DIV = Element.prototype.DIV || TagName.DIV; " +
        "getElement.prototype.DIV = new Type('Object'); });");
    Node aliasDefinition = root.getLastChild().getLastChild();
    ScopedAliases.AliasedTypeNode aliasedNode = new ScopedAliases.AliasedTypeNode(
        aliasDefinition, "new Type('Object')");

    scopedAliases.hotSwapScript(root, null);
    assertTrue(scopedAliases.hasErrors());

    Diagnostics diagnostics = compiler.getDiagnostics();
    List<String> errors = Lists.newArrayList();
    for (Node n : diagnostics.getDiagnostics()) {
      errors.add(n.getErrorMessage());
    }
    List<String> expectedErrors = Lists.newArrayList(
        "The alias getElement.prototype.DIV is assigned a value other than a " +
          "const or var, but no value is given in the current scope.");
    assertSame(expectedErrors, errors);
  }

  @Test
  public void testCollapseScope() {
    Node root = Node.parse("goog.scope(function() { var dom = goog.dom; " +
        "function createElement(tagName) { return goog.dom.createElement(tagName); } " +
        "createElement('DIV'); goog.scope(function() { var elem = createElement('DIV'); }); });");

    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());

    Node expected = Node.parse("goog.dom.createElement('DIV');");
    assertSame(expected, root.getLastChild());

    Node elemScope = root.getLastChild().getLastChild();
    assertNull(elemScope.getParent());
    assertSame(elemScope, NodeUtil.tryMergeBlock(elemScope));
  }
}