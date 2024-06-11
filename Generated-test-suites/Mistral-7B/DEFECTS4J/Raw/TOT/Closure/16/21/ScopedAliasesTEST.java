import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

  @Test
  public void testValidScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    Node result = root.getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", result.toString());
  }

  @Test
  public void testImproperUsageOfGoogScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var element = dom.createElement(DIV);\n" +
        "  goog.scope(function() {\n" +
        "    // This should cause an error.\n" +
        "  });\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.scope(function() {\n" +
        "    // This should cause an error.\n" +
        "  }, 123);\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testGoogScopeRefersToThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  this.counter = 0;\n" +
        "  goog.scope(function() {\n" +
        "    this.counter++;\n" +
        "  });\n" +
        "  return this.counter;\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testGoogScopeUsesReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testGoogScopeUsesThrow() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  try {\n" +
        "    throw new Error('Test error');\n" +
        "  } finally {\n" +
        "    // This should cause an error.\n" +
        "  };\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testAliasRedefined() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  var x = goog.x;\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        errorNode.getFirstChild().getQualifiedName().getName());
  }

  @Test
  public void testAliasUsesNonAliasLocal() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String script = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 2;\n" +
        "  });\n" +
        "});";

    Node root = RhinoLoader.loadJS(script);
    scopedAliases.process(null, root);

    // Check if an error was reported.
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        errorNode.getFirstChild().getQualifiedName().getName());
  }
}