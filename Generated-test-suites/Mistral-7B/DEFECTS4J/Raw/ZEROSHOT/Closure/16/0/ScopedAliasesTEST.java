import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = "package test;\n" +
        "goog.scope(function() {\n" +
        "  var goog = goog;\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var x = goog.x;\n" +
        "  goog.x = 42;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REMOVE);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    List<Node> ast = compiler.parse(sourceCode, null, null, null);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertEquals(2, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    Node googScopeCall = scopeCall.getFirstChild().getLastChild();
    assertNotNull(googScopeCall);
    assertEquals("goog.scope", googScopeCall.getQualifiedName());

    Node googScopeBlock = scopeCall.getLastChild();
    assertEquals(11, googScopeBlock.getChildCount());

    Node nodeGoog = googScopeBlock.getChildAtIndex(0);
    assertEquals("goog", nodeGoog.getString());
    assertEquals(Node.ASSIGN, nodeGoog.getType());

    Node nodeDom = googScopeBlock.getChildAtIndex(2);
    assertEquals("dom", nodeDom.getString());
    assertEquals(Node.ASSIGN, nodeDom.getType());

    Node nodeDiv = googScopeBlock.getChildAtIndex(4);
    assertEquals("DIV", nodeDiv.getString());
    assertEquals(Node.IDENT, nodeDiv.getType());

    Node nodeX = googScopeBlock.getChildAtIndex(6);
    assertEquals("x", nodeX.getString());
    assertEquals(Node.ASSIGN, nodeX.getType());

    Node nodeNumber = googScopeBlock.getChildAtIndex(8);
    assertEquals("number", nodeNumber.getString());
    assertEquals(Node.IDENT, nodeNumber.getType());

    Node nodeXAssignment = googScopeBlock.getChildAtIndex(10);
    assertEquals("x", nodeXAssignment.getFirstChild().getString());
    assertEquals(Node.ASSIGN, nodeXAssignment.getType());
    assertEquals(Node.INT_LITERAL, nodeXAssignment.getLastChild().getType());

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node nodeGoogAlias = aliasDefinition.getLastChild();
    assertEquals("goog", nodeGoogAlias.getString());
    assertEquals(Node.ASSIGN, nodeGoogAlias.getType());
    assertEquals(42, ((Node.IntLiteral) nodeGoogAlias.getLastChild()).getValue());

    Node aliasDefinitionForDom = scopedAliases.getAliasDefinitionsInOrder().get(1);
    Node nodeDomAlias = aliasDefinitionForDom.getLastChild();
    assertEquals("dom", nodeDomAlias.getString());
    assertEquals(Node.ASSIGN, nodeDomAlias.getType());
    assertEquals(Node.NAME, nodeDomAlias.getFirstChild().getType());
    assertEquals("dom", nodeDomAlias.getFirstChild().getString());

    Node aliasUsage = scopedAliases.getAliasUsages().get(0);
    Node nodeGoogUsage = aliasUsage.applyAlias();
    assertEquals(nodeGoog, nodeGoogUsage);

    SourcePosition sourcePosition = new SourcePosition<>();
    Node nodeElement = googScopeBlock.getChildAtIndex(3);
    assertNotNull(nodeElement);
    assertEquals(sourceCode, nodeElement.getSourceFileName());
    sourcePosition.setPositionInformation(
        nodeElement, 5, 24, 6, 27);
    assertEquals(sourcePosition, nodeGoogUsage.getSourcePosition());
  }

  @Test
  public void testScopedAliasesWithError() {
    String sourceCode = "package test;\n" +
        "goog.scope(function() {\n" +
        "  var goog = goog;\n" +
        "  var x = goog.x;\n" +
        "  goog.x = 42;\n" +
        "  x = 0;\n" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REMOVE);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    List<Node> ast = compiler.parse(sourceCode, null, null, null);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    Node googScopeCall = scopeCall.getFirstChild().getLastChild();
    assertNotNull(googScopeCall);
    assertEquals("goog.scope", googScopeCall.getQualifiedName());

    Node googScopeBlock = scopeCall.getLastChild();
    assertEquals(10, googScopeBlock.getChildCount());

    Node nodeGoog = googScopeBlock.getChildAtIndex(0);
    assertEquals("goog", nodeGoog.getString());
    assertEquals(Node.ASSIGN, nodeGoog.getType());

    Node nodeX = googScopeBlock.getChildAtIndex(2);
    assertEquals("x", nodeX.getString());
    assertEquals(Node.ASSIGN, nodeX.getType());

    Node nodeNumber = googScopeBlock.getChildAtIndex(4);
    assertEquals("number", nodeNumber.getString());
    assertEquals(Node.IDENT, nodeNumber.getType());

    Node nodeXAssignment = googScopeBlock.getChildAtIndex(6);
    assertEquals("x", nodeXAssignment.getFirstChild().getString());
    assertEquals(Node.ASSIGN, nodeXAssignment.getType());
    assertEquals(Node.INT_LITERAL, nodeXAssignment.getLastChild().getType());

    Node nodeNumberAssignment = googScopeBlock.getChildAtIndex(8);
    assertEquals("x", nodeNumberAssignment.getFirstChild().getString());
    assertEquals(Node.ASSIGN, nodeNumberAssignment.getType());
    assertEquals(Node.INT_LITERAL, nodeNumberAssignment.getLastChild().getType());

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node nodeGoogAlias = aliasDefinition.getLastChild();
    assertEquals("goog", nodeGoogAlias.getString());
    assertEquals(Node.ASSIGN, nodeGoogAlias.getType());
    assertEquals(42, ((Node.IntLiteral) nodeGoogAlias.getLastChild()).getValue());

    Node aliasUsage = scopedAliases.getAliasUsages().get(0);
    Node nodeGoogUsage = aliasUsage.applyAlias();
    assertEquals(nodeGoog, nodeGoogUsage);

    Compiler.Error error = null;
    try {
      aliasUsage.applyAlias();
    } catch (Exception e) {
      error = e.getCause();
    }

    assertNotNull(error);
    assertEquals(GOOG_SCOPE_USES_RETURN, error.getType());
  }

  @Test
  public void testScopedAliasesInvalidCall() {
    String sourceCode = "package test;\n" +
        "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REMOVE);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    List<Node> ast = compiler.parse(sourceCode, null, null, null);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    Node googScopeCall = scopeCall.getFirstChild().getLastChild();
    assertNotNull(googScopeCall);
    assertEquals("goog.scope", googScopeCall.getQualifiedName());

    Node googScopeBlock = scopeCall.getLastChild();
    assertEquals(1, googScopeBlock.getChildCount());

    Node innerScopeCall = googScopeBlock.getChildAtIndex(0);
    assertNotNull(innerScopeCall);
    assertEquals("goog.scope", innerScopeCall.getQualifiedName());

    Compiler.Error error = null;
    try {
      scopedAliases.process(null, ast.get(0));
    } catch (Exception e) {
      error = e.getCause();
    }

    assertNotNull(error);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, error.getType());
  }

  @Test
  public void testScopedAliasesWithThis() {
    String sourceCode = "package test;\n" +
        "goog.scope(function() {\n" +
        "  this.x = 0;\n" +
        "  function foo() {\n" +
        "    this.x = 1;\n" +
        "  }\n" +
        "  goog.scope(function() {\n" +
        "    foo();\n" +
        "  });\n" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REMOVE);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    List<Node> ast = compiler.parse(sourceCode, null, null, null);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    Node googScopeCall = scopeCall.getFirstChild().getLastChild();
    assertNotNull(googScopeCall);
    assertEquals("goog.scope", googScopeCall.getQualifiedName());

    Node googScopeBlock = scopeCall.getLastChild();
    assertEquals(2, googScopeBlock.getChildCount());

    Node innerScopeCall = googScopeBlock.getChildAtIndex(0);
    assertNotNull(innerScopeCall);
    assertEquals("goog.scope", innerScopeCall.getQualifiedName());

    Compiler.Error error = null;
    try {
      scopedAliases.process(null, ast.get(0));
    } catch (Exception e) {
      error = e.getCause();
    }

    assertNotNull(error);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, error.getType());
  }
}