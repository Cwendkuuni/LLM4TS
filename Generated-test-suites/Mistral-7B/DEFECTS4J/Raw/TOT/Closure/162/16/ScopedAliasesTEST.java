import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testNoAlias() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var x = 1; " +
            "}); " +
            "return x; " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Node result = compiler.compileExternsAndReturnRootNode(null, null);
    assertEquals(1, result.getNumber());
  }

  @Test
  public void testSimpleAlias() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var DIV = goog.dom.TagName.DIV; " +
            "}); " +
            "goog.dom.createElement(DIV); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Node result = compiler.compileExternsAndReturnRootNode(null, null);
    assertTrue(result.getLastChild().isInstanceOf(Node.NAME, "DIV"));
  }

  @Test
  public void testNestedAliases() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var goog = {}; " +
            "  goog.dom = {}; " +
            "  goog.dom.Div = goog.dom.TagName.DIV; " +
            "}); " +
            "goog.dom.Div div = new goog.dom.Div('test'); " +
            "return div; " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Node result = compiler.compileExternsAndReturnRootNode(null, null);
    assertTrue(result.getLastChild().isInstanceOf(Node.NAME, "Div"));
  }

  @Test
  public void testMultipleAliases() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var goog = {}; " +
            "  goog.dom = {}; " +
            "  goog.dom.Div = goog.dom.TagName.DIV; " +
            "  goog.dom.Button = goog.dom.TagName.BUTTON; " +
            "}); " +
            "goog.dom.Div div = new goog.dom.Div('test'); " +
            "goog.dom.Button button = new goog.dom.Button(); " +
            "return div; " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Node result = compiler.compileExternsAndReturnRootNode(null, null);
    assertTrue(result.getLastChild().isInstanceOf(Node.NAME, "Div"));
    assertTrue(result.getLastChildAfter(1).isInstanceOf(Node.NAME, "Button"));
  }

  @Test
  public void testAliasDuplication() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var goog = {}; " +
            "  goog.dom = {}; " +
            "  goog.dom.Div = goog.dom.TagName.DIV; " +
            "  goog.dom.Div = goog.dom.TagName.BUTTON; " +
            "}); " +
            "goog.dom.Div div = new goog.dom.Div('test'); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, compiler.compileExternsAndReturnRootNode(null, null), traversal);
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(1, aliasUsages.size());
    AliasUsage usage = aliasUsages.get(0);
    assertTrue(usage instanceof AliasedTypeNode);
    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) usage;
    assertEquals("Div", aliasedTypeNode.aliasReference.getString());
    assertEquals("Button", aliasedTypeNode.correctedType);
  }

  @Test
  public void testAliasNoInitializer() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  var goog = {}; " +
            "  goog.dom = {}; " +
            "  goog.dom.Div; " + // no initializer
            "}); " +
            "goog.dom.Div div = new goog.dom.Div('test'); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, compiler.compileExternsAndReturnRootNode(null, null), traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("Div", aliasDefinition.getLastChild().getString());
    assertEquals(0, aliasDefinition.getChildCount());
  }

  @Test
  public void testInvalidScopeCall() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(); " + // no function parameter
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0));
  }

  @Test
  public void testScopeCallWithParameters() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function(x) { " +
            "}); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testScopeCallWithFunctionBody() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  return 1; " +
            "}); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    Node result = compiler.compileExternsAndReturnRootNode(null, null);
    assertEquals(1, result.getNumber());
  }

  @Test
  public void testScopeCallWithReturn() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  return 1; " +
            "}); " +
            "return 2; " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @Test
  public void testScopeCallWithThis() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  return this; " +
            "}); " +
            "return 2; " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
  }

  @Test
  public void testScopeCallWithThrow() {
    Node root = NodeUtil.parseString(
        "function foo() { " +
            "goog.scope(function() { " +
            "  throw new Error(); " +
            "}); " +
            "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0));
  }

  @After
  public void tearDown() {
    compiler = null;
  }
}