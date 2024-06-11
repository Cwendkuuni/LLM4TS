import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for the {@link ScopedAliases} compiler pass.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  return goog.dom.createElement(DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testNestedScopedAlias() {
    String input = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = dom.TagName.DIV;" +
        "    return goog.dom.createElement(DIV);" +
        "  });" +
        "  return 'foo';" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    Node result = compiler.getAST();
    assertEquals("'foo'", result.toPrintableJavaScript());
  }

  @Test
  public void testAliasIsAssignedMoreThanOnce() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  DIV = DIV;" +
        "  return goog.dom.createElement(DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testNonAliasLocalVariableInScope() {
    String input = "goog.scope(function() {" +
        "  var x = 0;" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_NON_ALIAS_LOCAL error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testGoogScopeCallWithInvalidParameters() {
    String input = "goog.scope(123);";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_HAS_BAD_PARAMETERS error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testGoogScopeCallWithThisKeyword() {
    String input = "goog.scope(function() {" +
        "  this.x = 0;" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_REFERENCES_THIS error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testGoogScopeCallWithReturnStatement() {
    String input = "goog.scope(function() {" +
        "  return 123;" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_RETURN error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testGoogScopeCallWithThrowStatement() {
    String input = "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_THROW error.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @Test
  public void testAliasTransformation() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  return goog.dom.createElement(DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithFunctionCall() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  return foo();" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithFunctionExpression() {
    String input = "goog.scope(function() {" +
        "  var foo = function() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  return foo();" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithFunctionDeclaration() {
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "  }" +
        "  foo.prototype.createElement = function() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "  return foo.createElement();" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithNamedFunction() {
    String input = "goog.scope(function foo() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithAnonymousFunction() {
    String input = "goog.scope(function() {" +
        "  return (function() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  })();" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of("dom", "DIV"), aliases);

    // Verify that the alias transformations were applied in the correct order.
    Node result = compiler.getAST();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        result.toPrintableJavaScript());
  }

  @Test
  public void testAliasTransformationWithFunctionWithArguments() {
    String input = "goog.scope(function(x) {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parseString(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<String> aliases = transformationHandler.getAliases();
    assertEquals(ImmutableList.of(), aliases);

    // Verify that no alias transformations were applied.
    Node result = compiler.getAST();
    assertEquals("function(x) {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "}",
        result.toPrintableJavaScript());
  }
}