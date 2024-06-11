package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.robotframework.javalin.JUnitUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node rootNode = Node.objectLiteral();
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testValidScopeCall() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopeCall() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  return;\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallWithBadParameters() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  goog.dom\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallWithThisKeyword() {
    String sourceCode = "goog.scope(function() {\n" +
            "  this.dom = goog.dom;\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallWithThrowKeyword() {
    String sourceCode = "goog.scope(function() {\n" +
            "  throw new Error('Test error');\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallWithAliasRedefined() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  goog.scope(function() {\n" +
            "    var dom = 'new dom'; // should be an error\n" +
            "  });\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallWithAliasUsage() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  goog.scope(function() {\n" +
            "    dom.createElement('div');\n" +
            "  });\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAliases() {
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  goog.scope(function() {\n" +
            "    var DIV = goog.dom.TagName.DIV;\n" +
            "    goog.dom.createElement(DIV);\n" +
            "  });\n" +
            "});";
    Node rootNode = JUnitUtils.compileSource(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.nullNode(), rootNode);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinitionNode = aliasDefinitionsInOrder.get(0);
    Node aliasUsageNode = aliasUsages.get(0);
    Node scopeCallNode = scopeCalls.get(0);

    assertEquals("goog.dom", aliasDefinitionNode.getString());
    assertEquals("DIV", aliasUsageNode.getString());
    assertEquals("goog.dom.TagName.DIV", scopeCallNode.getLastChild().getLastChild().getString());
  }
}