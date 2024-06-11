import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testValidScopedAlias() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    Node domNode = root.getFirstChild().getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        domNode.toSource());
  }

  @Test(expected = AssertionError.class)
  public void testMissingScopeCall() {
    SourceFile sourceFile = new SourceFile("Test.js", "var dom = goog.dom; " +
        "dom.createElement('DIV');");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_USED_IMPROPERLY error");
  }

  @Test(expected = AssertionError.class)
  public void testMultipleScopeCalls() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement('DIV'); " +
        "  }); " +
        "  goog.dom.createElement('DIV'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_USED_IMPROPERLY error");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithArguments() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function(x) {" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement('DIV'); " +
        "  }); " +
        "  goog.dom.createElement('DIV'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_HAS_BAD_PARAMETERS error");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithReturnStatement() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  return; " +
        "  goog.dom.createElement('DIV'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_USES_RETURN error");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithThrowStatement() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  throw new Error(); " +
        "  goog.dom.createElement('DIV'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_USES_THROW error");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithThisKeyword() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  this.count = 0;" +
        "  goog.dom.createElement('DIV'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    fail("Expected GOOG_SCOPE_REFERENCES_THIS error");
  }

  @Test
  public void testNestedScopedAliases() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  var outer = goog.dom; " +
        "  goog.scope(function() {" +
        "    var inner = goog.dom; " +
        "    goog.dom.createElement(outer.TagName.DIV); " +
        "  }); " +
        "  goog.dom.createElement(inner.TagName.DIV); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    Node outerDiv = root.getFirstChild().getLastChild();
    Node innerDiv = outerDiv.getLastChild().getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", outerDiv.toSource());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", innerDiv.toSource());
  }

  @Test
  public void testAliasRedefinition() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  var x = 1;" +
        "  var y = goog.x;" +
        "  goog.x = 2;" +
        "  var z = goog.x;" +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    assertEquals("2", root.getLastChild().getLastChild().getInt());
  }

  @Test
  public void testAliasWithQualifiedName() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  var google = {" +
        "    dom: {" +
        "      TagName: {" +
        "        DIV: null" +
        "      }" +
        "    }" +
        "  }; " +
        "  goog.dom = {TagName: {DIV: null}}; " +
        "  goog.dom.TagName.DIV = google.dom.TagName.DIV;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    Node googDomTagNameDiv = root.getFirstChild().getLastChild().getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        googDomTagNameDiv.toSource());
  }

  @Test
  public void testAliasWithShadowedNamespace() {
    SourceFile sourceFile = new SourceFile("Test.js", "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      getElementById: function(id) {" +
        "        return document.getElementById(id);" +
        "      }" +
        "    }" +
        "  }; " +
        "  goog.dom.getElementById = function(id) {" +
        "    return document.getElementById(id + '_extra');" +
        "  }; " +
        "  var element = goog.dom.getElementById('test'); " +
        "});");

    Node root = compiler.parse(sourceFile, null, null);
    scopedAliases.process(null, root);

    assertTrue(root.getLastChild().getLastChild().isMissing());
  }
}