import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.type;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    String code =
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "});";
    rootNode = TestUtils.parseAndCompileToString(compiler, code);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    assertNotNull(scopedAliases.transformation);

    Node googScopeFunction = TestUtils.findNodeByUniqueName(rootNode, "goog.scope");
    assertTrue(googScopeFunction.getChildCount() == 3);

    Node anonymousFunction = googScopeFunction.getChildAtIndex(1);
    assertNotNull(anonymousFunction);
    assertEquals(1, anonymousFunction.getChildCount());

    Node aliasDefinition = scopedAliases.aliasDefinitionsInOrder.get(0);
    assertEquals(aliasDefinition, anonymousFunction.getFirstChild());

    assertEquals("dom", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.dom", aliasDefinition.getLastChild().getQualifiedName());

    Node domElementCreation = rootNode.getChildAtIndex(1);
    assertEquals("createElement", domElementCreation.getOperator());

    Node dom = rootNode.getChildAtIndex(0);
    assertEquals(domElementCreation.getParent(), dom);

    Node aliasUsage = scopedAliases.aliasUsages.get(0);
    assertEquals(aliasUsage, domElementCreation.getParent().getChildAtIndex(1));
    assertEquals("dom", aliasUsage.getAliasReference().getString());
    assertEquals("goog.dom", aliasUsage.getAliasDefinition().getQualifiedName());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsedImproperly() {
    String code =
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "  goog.scope(function() {\n" +
            "    DIV.createElement('DIV');\n" +
            "  });\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_USED_IMPROPERLY) + " but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeHasBadParameters() {
    String code =
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  function test() {\n" +
            "    dom.createElement(DIV);\n" +
            "  }\n" +
            "  goog.scope(test);\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_HAS_BAD_PARAMETERS) + " but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeReferencesThis() {
    String code =
        "goog.scope(function() {\n" +
            "  this.dom = goog.dom;\n" +
            "  function test() {\n" +
            "    goog.scope(function() {\n" +
            "      this.createElement(this.dom.TagName.DIV);\n" +
            "    });\n" +
            "  }\n" +
            "  test();\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_REFERENCES_THIS) + " but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesReturn() {
    String code =
        "goog.scope(function() {\n" +
            "  function test() {\n" +
            "    return goog.dom.createElement(goog.dom.TagName.DIV);\n" +
            "  }\n" +
            "  goog.scope(test);\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_USES_RETURN) + " but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesThrow() {
    String code =
        "goog.scope(function() {\n" +
            "  function test() {\n" +
            "    try {\n" +
            "      goog.scope(function() {\n" +
            "        throw new Error();\n" +
            "      });\n" +
            "    } catch (e) {\n" +
            "      // do nothing\n" +
            "    }\n" +
            "  }\n" +
            "  goog.scope(test);\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_USES_THROW) + " but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeAliasRedefined() {
    String code =
        "goog.scope(function() {\n" +
            "  function test() {\n" +
            "    goog.scope(function() {\n" +
            "      var dom = goog.dom;\n" +
            "      var DIV = dom.TagName.DIV;\n" +
            "      goog.scope(function() {\n" +
            "        var DIV = goog.dom.TagName.DIV;\n" +
            "      });\n" +
            "      dom.createElement(DIV);\n" +
            "    });\n" +
            "  }\n" +
            "  test();\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_ALIAS_REDEFINED) +
        " for alias 'DIV' but not thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeNonAliasLocal() {
    String code =
        "goog.scope(function() {\n" +
            "  function test() {\n" +
            "    var dom = goog.dom;\n" +
            "    var DIV = dom.TagName.DIV;\n" +
            "    goog.scope(function() {\n" +
            "      var dom = goog.dom;\n" +
            "      var DIV = dom.TagName.DIV;\n" +
            "    });\n" +
            "    dom.createElement(DIV);\n" +
            "  }\n" +
            "  test();\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    fail("Expected " + type(GOOG_SCOPE_NON_ALIAS_LOCAL) + " for variable 'dom' but not thrown.");
  }

  @Test
  public void testNoErrors() {
    String code =
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  goog.scope(function() {\n" +
            "    dom.createElement(DIV);\n" +
            "  });\n" +
            "  goog.scope(function() {\n" +
            "    dom.createElement(DIV);\n" +
            "  });\n" +
            "});";

    rootNode = TestUtils.parseAndCompileToString(compiler, code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());

    // The code above will generate a single error message that includes all
    // the errors that would be reported separately.  We cannot test this error
    // message directly, but we can check that no other errors were reported.
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }
}