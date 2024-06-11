package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  /** The compiler instance used for testing. */
  private AbstractCompiler compiler;

  /** The preprocessor symbol table instance used for testing. */
  private PreprocessorSymbolTable preprocessorSymbolTable;

  /** The alias transformation handler instance used for testing. */
  private AliasTransformationHandler transformationHandler;

  /** The {@link ScopedAliases} instance used for testing. */
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    // Verify the results.
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getString());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCall.getChildCount());
    assertEquals("function() { ...}", scopeCall.getLastChild().getString());
  }

  @Test
  public void testInvalidAlias() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() { ... });" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasRedefinition() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var DIV = 1;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(2, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasThrowsException() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('Test Error');" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasReturnsValue() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  return 1;" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasRefersToThis() {
    // Set up the test case.
    String sourceCode = "goog.scope(function() {" +
        "  var self = this;" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasParameter() {
    // Set up the test case.
    String sourceCode = "goog.scope(function(arg) {" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasWithJSDocInfo() {
    // Set up the test case.
    String sourceCode = "/** @type {goog.dom.TagName} */\n" +
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the error message.
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errors.get(0).getToken()
        .getDiagnosticCode());
  }

  @Test
  public void testAliasWithJSDocInfoTypeAnnotation() {
    // Set up the test case.
    String sourceCode = "/** @type {goog.dom.TagName} */\n" +
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the alias definition.
    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getString());

    // Verify the scope call.
    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCall.getChildCount());
    assertEquals("function() { ...}", scopeCall.getLastChild().getString());
  }

  @Test
  public void testAliasWithJSDocInfoTypeAnnotationAndTypeNode() {
    // Set up the test case.
    String sourceCode = "/** @type {goog.dom.TagName} */\n" +
        "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */ " +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // Test the expected behavior.
    Node rootNode = Node.parse(sourceCode);
    scopedAliases.process(null, rootNode);

    // Verify the results.
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Verify the alias definition.
    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getString());

    // Verify the scope call.
    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCall.getChildCount());
    assertEquals("function() { ...}", scopeCall.getLastChild().getString());

    // Verify the type node.
    Node typeNode = rootNode.getFirstChild();
    assertEquals("goog.dom.TagName", typeNode.getString());
  }
}