package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.google.javascript.jscomp.DiagnosticType.error;
import static com.google.javascript.jscomp.DiagnosticType.warning;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private Compiler compiler;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    compiler.setPreprocessorSymbolTable(preprocessorSymbolTable);
    compiler.setAliasTransformationHandler(transformationHandler);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("DIV", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals("DIV", aliasUsages.get(0).toString());
  }

  @Test
  public void testMultipleAliases() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV2 = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.dom.createElement(DIV2);" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("DIV", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("DIV2", aliasDefinitionsInOrder.get(1).getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertEquals("DIV", aliasUsages.get(0).toString());
    assertEquals("DIV2", aliasUsages.get(1).toString());
  }

  @Test
  public void testAliasAssignmentError() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = 'div';" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_ALIAS_REDEFINED, "DIV"), diagnostics.get(0));
  }

  @Test
  public void testGoogScopeParamError() {
    String sourceCode =
        "goog.scope(function(x) {" +
        "  goog.dom.createElement(goog.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_HAS_BAD_PARAMETERS), diagnostics.get(0));
  }

  @Test
  public void testGoogScopeReturnError() {
    String sourceCode =
        "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_USES_RETURN), diagnostics.get(0));
  }

  @Test
  public void testGoogScopeThrowError() {
    String sourceCode =
        "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_USES_THROW), diagnostics.get(0));
  }

  @Test
  public void testGoogScopeInvalidAliasError() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var invalidAlias = goog.TagName.DIV;" +
        "});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_NON_ALIAS_LOCAL, "invalidAlias"), diagnostics.get(0));
  }

  @Test
  public void testEmptyGoogScope() {
    String sourceCode = "goog.scope(function() {});";

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals(1, scopeCall.getChildCount());

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testGoogScopeWithWarning() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var x = goog.TagName.DIV;" +
        "  goog.TagName.DIV = 'DIV2';" +
        "});";

    when(preprocessorSymbolTable.get(NodeUtil.createQualifiedName("goog", "TagName")))
        .thenReturn(new Var(NodeUtil.createString("DIV"), null, null, null, null));

    Node root = NodeUtil.parseJavaScript(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("DIV2", aliasDefinitionsInOrder.get(0).getString());

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals("DIV", aliasUsages.get(0).toString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(warning(GOOG_SCOPE_ALIAS_REDEFINED, "DIV"), diagnostics.get(0));
  }
}