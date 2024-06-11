import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testSimpleAlias() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    options.setAliasTransformations(ImmutableMap.of(
        "dom", "goog.dom"
    ));

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, new IdentityAliasTransformationHandler());

    pass.process(null, rootNode);

    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.getScriptNode(pass.getOutput()).getTextContent()
    );
  }

  @Test
  public void testAliasRefersToThis() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var self = this;" +
        "  function foo() { return self; }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertError(GOOG_SCOPE_REFERENCES_THIS, rootNode, null);
  }

  @Test
  public void testAliasUsesReturn() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertError(GOOG_SCOPE_USES_RETURN, rootNode, null);
  }

  @Test
  public void testAliasUsesThrow() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error('Boo')" +
        "  }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertError(GOOG_SCOPE_USES_THROW, rootNode, null);
  }

  @Test
  public void testAliasRedefinition() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  var DIV = function() {};" +
        "  goog.dom.createElement(DIV);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertError(GOOG_SCOPE_ALIAS_REDEFINED, rootNode, "DIV");
  }

  @Test
  public void testAliasNonLocal() {
    String input = "goog.scope(function() {" +
        "  var x = 42;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertError(GOOG_SCOPE_NON_ALIAS_LOCAL, rootNode, "x");
  }

  @Test
  public void testScopedFunctionWithParameters() {
    String input = "goog.scope(function() {" +
        "  function foo(x, y) {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo, 1, 2);" +
        "})";

    assertError(GOOG_SCOPE_HAS_BAD_PARAMETERS, rootNode, null);
  }

  @Test
  public void testScopedFunctionWithReturn() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo);" +
        "})";

    assertError(GOOG_SCOPE_USES_RETURN, rootNode, null);
  }

  @Test
  public void testScopedFunctionWithThrow() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error('Boo')" +
        "  }" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, null, null, foo);" +
        "})";

    assertError(GOOG_SCOPE_USES_THROW, rootNode, null);
  }

  @Test
  public void testNestedScopes() {
    String input = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "})";

    Node rootNode = new SourceFile("Test.java", input).parseTree();

    assertNoErrors(rootNode);
  }

  private void assertError(DiagnosticType errorType, Node rootNode, String... errorArguments) {
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, new IdentityAliasTransformationHandler());

    pass.process(null, rootNode);

    assertEquals(1, pass.getDiagnostics().size());
    Diagnostic diagnostic = pass.getDiagnostics().get(0);

    assertEquals(errorType, diagnostic.getType());
    assertTrue(diagnostic.getMessage().containsAllOf(errorArguments));
  }

  private void assertNoErrors(Node rootNode) {
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, new IdentityAliasTransformationHandler());

    pass.process(null, rootNode);

    assertEquals(0, pass.getDiagnostics().size());
  }
}