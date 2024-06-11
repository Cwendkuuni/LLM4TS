package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.util.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String name, String alias) {
        System.out.println("Alias " + name + " is mapped to " + alias);
      }
    };

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.parse(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("DIV", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(anonymousFnNode.isFunction());
    assertFalse(NodeUtil.getFunctionName(anonymousFnNode) != null);
    assertFalse(NodeUtil.getFunctionParameters(anonymousFnNode).hasChildren());
  }

  @Test
  public void testScopedAliasesWithJSDocs() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String name, String alias) {
        System.out.println("Alias " + name + " is mapped to " + alias);
      }
    };

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.parse(
        "goog.scope(function() {" +
            "/** @type {goog.dom.TagName} */\n" +
            "var DIV = goog.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("DIV", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(anonymousFnNode.isFunction());
    assertFalse(NodeUtil.getFunctionName(anonymousFnNode) != null);
    assertFalse(NodeUtil.getFunctionParameters(anonymousFnNode).hasChildren());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String name, String alias) {
      }
    };

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.parse(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.scope(" +
            ")" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "this;" +
            "});");

    scopedAliases.process(null, root);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumberOfErrors() > 0);
    assertTrue(diagnostics.getNumberOfWarnings() == 0);
    List<Node> errorNodes = Lists.newArrayList();

    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
      if (diagnostic.getSeverity() == Diagnostics.Level.ERROR) {
        errorNodes.add(diagnostic.getSourceNode());
      }
    }

    assertEquals(3, errorNodes.size());

    Node thisNode = errorNodes.get(0);
    assertEquals("this", thisNode.getString());
    assertEquals(1, thisNode.getLineNumber());
    assertEquals(18, thisNode.getCharPositionInLine());

    Node googScopeCallNode = errorNodes.get(1);
    assertEquals("goog.scope", googScopeCallNode.getString());
    assertEquals(1, googScopeCallNode.getLineNumber());
    assertEquals(7, googScopeCallNode.getCharPositionInLine());

    Node googScopeCallWithParamNode = errorNodes.get(2);
    assertEquals("goog.scope", googScopeCallWithParamNode.getString());
    assertEquals(1, googScopeCallWithParamNode.getLineNumber());
    assertEquals(10, googScopeCallWithParamNode.getCharPositionInLine());
  }
}