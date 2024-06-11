package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.TransformationReport;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourcePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test case for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  private static final DiagnosticType[] GOOG_SCOPE_ERRORS = {
    ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY,
    ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS,
    ScopedAliases.GOOG_SCOPE_REFERENCES_THIS,
    ScopedAliases.GOOG_SCOPE_USES_RETURN,
    ScopedAliases.GOOG_SCOPE_USES_THROW,
    ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED,
    ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL
  };

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setAliasTransformationHandler(handler);
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, handler);

    Node root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});");

    // No errors, since root is valid.
    List<Node> errors = new ArrayList<>();
    scopedAliases.process(compiler.getExterns(), root);
    assertFalse(errors.isEmpty());

    // Test errors when goog.scope is used improperly.
    Node invalidScope = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScope);
    for (DiagnosticType error : GOOG_SCOPE_ERRORS) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test errors when goog.scope has bad parameters.
    Node invalidScopeWithParams = Node.parse("goog.scope(function(a, b) {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithParams);
    for (DiagnosticType error : GOOG_SCOPE_ERRORS) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test errors when goog.scope references 'this'.
    Node invalidScopeWithThis = Node.parse("goog.scope(function() {" +
        "  var self = this;" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithThis);
    for (DiagnosticType error : new DiagnosticType[] {
        ScopedAliases.GOOG_SCOPE_REFERENCES_THIS
    }) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test errors when goog.scope uses 'return'.
    Node invalidScopeWithReturn = Node.parse("goog.scope(function() {" +
        "  return 42;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithReturn);
    for (DiagnosticType error : GOOG_SCOPE_ERRORS) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test errors when goog.scope uses 'throw'.
    Node invalidScopeWithThrow = Node.parse("goog.scope(function() {" +
        "  throw new Error('Something went wrong');" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithThrow);
    for (DiagnosticType error : GOOG_SCOPE_ERRORS) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test alias redefinition error.
    Node invalidScopeWithAliasRedefinition = Node.parse("goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  var foo = 42;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithAliasRedefinition);
    for (DiagnosticType error : new DiagnosticType[] {
        ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED
    }) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test non-alias local error.
    Node invalidScopeWithNonAliasLocal = Node.parse("goog.scope(function() {" +
        "  var foo = 42;" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), invalidScopeWithNonAliasLocal);
    for (DiagnosticType error : new DiagnosticType[] {
        ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL
    }) {
      assertTrue(errors.stream().anyMatch(n -> error.equals(n.getType())));
    }

    // Test valid usage of goog.scope.
    Node validScope = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");
    errors.clear();
    scopedAliases.process(compiler.getExterns(), validScope);
    assertTrue(errors.isEmpty());

    // Test transformation of valid usage of goog.scope.
    Traversal traversal = new Traversal();
    scopedAliases.hotSwapScript(validatorRoot(root), null);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    Node googDom = NodeUtil.findNodeByQualifiedName(aliasDefinitionsInOrder.get(0), "goog.dom");
    Node domElement = NodeUtil.findNodeByQualifiedName(aliasDefinitionsInOrder.get(1), "goog.dom.Element");
    Node domTagName = NodeUtil.findNodeByQualifiedName(aliasDefinitionsInOrder.get(2), "goog.dom.TagName");
    Node newDIV = NodeUtil.findNodeByQualifiedName(scopeCalls.get(0), "goog.dom.createElement");
    TransformationReport report = handler.getTransformationReport();
    String expectedAliasName = "goog.dom";
    String expectedNewAliasName = "goog_dom_";
    assertEquals(1, report.getAliasTransformations().size());
    assertTrue(report.getAliasTransformations().get(0).getAliasedName().equals(expectedAliasName));
    assertEquals(expectedNewAliasName, report.getAliasTransformations().get(0).getNewAliasName());
    assertEquals(newDIV.getQualifiedName(), "goog_dom_Element.createElement(goog_dom_TagName.DIV)");
  }

  private Node validatorRoot(Node root) {
    Node validatorRoot = Node.parse("function() {" +
        "  var result = {};" +
        "  for (var name in arguments[0]) {" +
        "    result[name] = arguments[0][name];" +
        "  }" +
        "  return result;" +
        "}");
    return validatorRoot.call(null, root);
  }
}