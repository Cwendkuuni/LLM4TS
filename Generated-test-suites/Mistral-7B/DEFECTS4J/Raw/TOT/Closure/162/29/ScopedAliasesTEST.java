package com.google.javascript.jscomp;

import static com.google.javascript.rhino.NodeProperty.JSDOC_INFO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private Compiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setCompilerOptions(
        new CompilerOptions()
            .setAliasTransformation(AliasTransformation.STRICT));
    sourceFile = new SourceFile("ScopedAliasesTest.java", "");
  }

  @Test
  public void testSimpleScopedAlias() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = sourceFile.parse(code);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.TagName", aliasDefinitionsInOrder.get(0).getQualifiedName());
    assertEquals(1, aliasUsages.size());
    assertEquals("dom", aliasUsages.get(0).getClass().getName());
    assertEquals("goog.dom.TagName", aliasUsages.get(0).toString());
    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    Node aliasNode = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.ASSIGN, aliasNode.getFirstChild().getType());
    assertEquals(Token.NAME, aliasNode.getLastChild().getType());

    Node aliasUsageNode = aliasUsages.get(0);
    assertEquals(Token.NAME, aliasUsageNode.getType());
    assertEquals("dom", aliasUsageNode.getString());

    Node scopeCallNode = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCallNode.getType());
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());

    Node functionNode = scopeCallNode.getLastChild();
    assertEquals(Token.FUNCTION, functionNode.getType());
    assertNull(functionNode.getJSDocInfo().getTypeNodes());
  }

  @Test
  public void testScopedAliasWithJSDoc() {
    String code =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "goog.scope(function MyScope() {" +
        "  /**\n" +
        "   * @type {number}\n" +
        "   */\n" +
        "  var myNumber = 42;" +
        "});\n" +
        "goog.scope(function() {" +
        "  var myNumber = goog.MyScope.myNumber;" +
        "});";

    Node root = sourceFile.parse(code);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("MyScope", aliasDefinitionsInOrder.get(0).getQualifiedName());
    assertEquals(1, aliasUsages.size());
    assertEquals("myNumber", aliasUsages.get(0).getClass().getName());
    assertEquals("MyScope.myNumber", aliasUsages.get(0).toString());
    assertEquals(2, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());
    assertEquals("MyScope", scopeCalls.get(1).getFirstChild().getQualifiedName());

    Node aliasNode = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.ASSIGN, aliasNode.getFirstChild().getType());
    assertEquals(Token.NAME, aliasNode.getLastChild().getType());

    Node aliasUsageNode = aliasUsages.get(0);
    assertEquals(Token.NAME, aliasUsageNode.getType());
    assertEquals("myNumber", aliasUsageNode.getString());

    Node firstScopeCallNode = scopeCalls.get(0);
    assertEquals(Token.CALL, firstScopeCallNode.getType());
    assertEquals("goog.scope", firstScopeCallNode.getFirstChild().getQualifiedName());

    Node secondScopeCallNode = scopeCalls.get(1);
    assertEquals(Token.CALL, secondScopeCallNode.getType());
    assertEquals("MyScope", secondScopeCallNode.getFirstChild().getQualifiedName());

    Node myNumberNode = aliasDefinitionsInOrder.get(0).getLastChild();
    assertEquals(Token.NAME, myNumberNode.getType());
    assertEquals("myNumber", myNumberNode.getString());

    Node myNumberUsageNode = aliasUsages.get(0);
    assertEquals(Token.NAME, myNumberUsageNode.getType());
    assertEquals("myNumber", myNumberUsageNode.getString());

    Node jsDocInfoNode = myNumberNode.getJSDocInfo();
    assertNull(jsDocDocInfoNode);
  }

  @Test
  public void testErrorScenarios() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  return;" +
        "});";

    Node root = sourceFile.parse(code);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCallNode.getType());
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());

    Node functionNode = scopeCallNode.getLastChild();
    assertEquals(Token.FUNCTION, functionNode.getType());
    Node returnNode = functionNode.getLastChild();
    assertEquals(Token.RETURN, returnNode.getType());

    compiler.reportDiagnostics();
    assertEquals(
        1, compiler.getDiagnostics().length());
    DiagnosticType diagnosticType =
        compiler.getDiagnostics()[0].getType();
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        diagnosticType.getName());
  }

  @Test
  public void testErrorScenariosWithJSDoc() {
    String code =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  return;" +
        "});";

    Node root = sourceFile.parse(code);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCallNode.getType());
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());

    Node functionNode = scopeCallNode.getLastChild();
    assertEquals(Token.FUNCTION, functionNode.getType());
    Node returnNode = functionNode.getLastChild();
    assertEquals(Token.RETURN, returnNode.getType());

    Node jsDocInfoNode = functionNode.getJSDocInfo();
    assertNotNull(jsDocInfoNode);
    assertEquals(jsDocInfoNode.getTypeNodes().size(), 0);

    compiler.reportDiagnostics();
    assertEquals(
        1, compiler.getDiagnostics().length());
    DiagnosticType diagnosticType =
        compiler.getDiagnostics()[0].getType();
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        diagnosticType.getName());
  }
}