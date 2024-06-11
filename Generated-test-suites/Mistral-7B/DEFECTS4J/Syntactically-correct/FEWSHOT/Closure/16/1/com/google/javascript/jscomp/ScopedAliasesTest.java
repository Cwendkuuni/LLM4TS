package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.cli.ScopedAliases;
import org.apache.commons.cli.AbstractCompiler;
import org.apache.commons.cli.CompilerOptions;
import org.apache.commons.cli.PreprocessorSymbolTable;
import org.apache.commons.cli.AliasTransformationHandler;
import org.apache.commons.cli.Node;
import org.apache.commons.cli.JSDocInfo;
import org.apache.commons.cli.SourcePosition;
import org.apache.commons.cli.Traversal;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.Var;
import org.apache.commons.cli.DiagnosticType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testProcessing() {
    // Set up the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {" +
        "var x = goog.x;" +
        "var y = goog.y;" +
        "goog.scope(function() {" +
        "  var x = 1;" +
        "  var z = goog.z;" +
        "});" +
        "goog.scope(function() {" +
        "  var w = goog.w;" +
        "  var z = 2;" +
        "});" +
        "})";

    Node root = new Parser(null).parse(script, null, null);

    // Run the test
    scopedAliases.process(null, root);

    // Validate the results
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertTrue(aliasDefinitionsInOrder.size() == 3);
    assertEquals("goog.x", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.y", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("goog.w", aliasDefinitionsInOrder.get(2).getString());

    assertEquals(2, scopeCalls.size());
    assertEquals(3, aliasUsages.size());

    // Check that the alias definitions are in the correct order
    for (int i = 0; i < aliasDefinitionsInOrder.size() - 1; i++) {
      Node current = aliasDefinitionsInOrder.get(i);
      Node next = aliasDefinitionsInOrder.get(i + 1);
      assertTrue(NodeUtil.compareNodes(current, next) < 0);
    }

    // Check that the alias usages are applied correctly
    for (AliasUsage usage : aliasUsages) {
      if (usage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) usage;
        Node aliasReference = aliasedNode.aliasReference;
        Node aliasDefinition = aliasedNode.aliasDefinition;
        assertEquals(aliasDefinition.getString(), aliasReference.getString());
      } else if (usage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) usage;
        Node typeReference = aliasedTypeNode.typeReference;
        String aliasName = aliasedTypeNode.aliasName;
        String qualifiedName = aliasDefinition.getString();
        int dotIndex = qualifiedName.indexOf(".");
        String baseName = qualifiedName.substring(0, dotIndex);
        Var aliasVar = scopedAliases.getScope().getVar(baseName);
        Node aliasedNode = aliasVar.getInitialValue();
        assertEquals(aliasedNode.getQualifiedName() + aliasName, typeReference.getString());
      }
    }

    // Check that the scopes are collapsed correctly
    int numStatements = 0;
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isStatement()) {
        numStatements++;
      }
    }
    assertEquals(10, numStatements);

    // Check that there are no errors
    assertFalse(scopedAliases.getTraversal().hasErrors());
  }

  @Test
  public void testErrors() {
    // Set up the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test bad usage of goog.scope
    String script = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "})";

    Node root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    List<Node> errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    DiagnosticType errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errorType.getName());

    // Test goog.scope with multiple parameters
    script = "goog.scope(function() {" +
        "goog.scope(function(x, y) {" +
        "})";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errorType.getName());

    // Test goog.scope with a non-function body
    script = "goog.scope(1)";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errorType.getName());

    // Test goog.scope with a return statement
    script = "goog.scope(function() {" +
        "return 1";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errorType.getName());

    // Test goog.scope with a throw statement
    script = "goog.scope(function() {" +
        "throw new Error()";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errorType.getName());

    // Test alias redefinition
    script = "goog.scope(function() {" +
        "var x = goog.x;" +
        "var x = \"foo\";" +
        "})";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errorType.getName());

    // Test non-alias local variable in goog.scope
    script = "goog.scope(function() {" +
        "var x = 1;" +
        "})";

    root = new Parser(null).parse(script, null, null);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.getTraversal().hasErrors());
    errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    errorType = errors.get(0).getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", errorType.getName());
  }
}