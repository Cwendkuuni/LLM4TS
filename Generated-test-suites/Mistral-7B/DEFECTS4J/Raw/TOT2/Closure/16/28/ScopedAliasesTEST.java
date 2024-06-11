import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp. HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions compilerOptions;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    compilerOptions = new CompilerOptions();
    compilerOptions.addTransformation(new AliasTransformationHandler());
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, compilerOptions.getAliasTransformationHandler());
  }

  @Test
  public void testBasicFunctionBlock() {
    String code =
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "x = 10;" +
            "goog.log(x);" +
            "});";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);
    Node resultNode = compiler.getCompiledSource();

    assertEquals("goog.log(10)", resultNode.toString());
  }

  @Test
  public void testMultipleFunctionBlocks() {
    String code =
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "x = 10;" +
            "})" +
            "goog.scope(function() {" +
            "var y = goog.x;" +
            "y = 20;" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);
    Node resultNode = compiler.getCompiledSource();

    assertEquals("(function() {" +
        "  var x = 10;" +
        "})()", resultNode.getFirstChild().toString());
    assertEquals("(function() {" +
        "  var y = 20;" +
        "})()", resultNode.getLastChild().toString());
  }

  @Test(expected = AssertionError.class)
  public void testScopeFunctionWithParameters() {
    String code =
        "goog.scope(function(x) {" +
            "x = 10;" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);

    // The test should throw an AssertionError due to GOOG_SCOPE_BAD_PARAMETERS error.
  }

  @Test(expected = AssertionError.class)
  public void testScopeFunctionWithReturn() {
    String code =
        "goog.scope(function() {" +
            "return 10;" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);

    // The test should throw an AssertionError due to GOOG_SCOPE_USES_RETURN error.
  }

  @Test(expected = AssertionError.class)
  public void testScopeFunctionWithThis() {
    String code =
        "goog.scope(function() {" +
            "this.x = 10;" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);

    // The test should throw an AssertionError due to GOOG_SCOPE_REFERENCES_THIS error.
  }

  @Test(expected = AssertionError.class)
  public void testScopeFunctionWithThrow() {
    String code =
        "goog.scope(function() {" +
            "throw new Error('Test')"; +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);

    // The test should throw an AssertionError due to GOOG_SCOPE_USES_THROW error.
  }

  @Test
  public void testScopeFunctionWithAlias() {
    String code =
        "goog.scope(function() {" +
            "var x = goog.dom;" +
            "x = {createElement: function(tag) {" +
            "  return document.createElement(tag);" +
            "}};" +
            "goog.log(x.createElement('div'));" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);
    Node resultNode = compiler.getCompiledSource();

    assertEquals("document.createElement('div')", resultNode.toString());
  }

  @Test(expected = AssertionError.class)
  public void testScopeFunctionWithAliasRedefinition() {
    String code =
        "goog.scope(function() {" +
            "var x = goog.dom;" +
            "x = {createElement: function(tag) {" +
            "  return document.createElement(tag);" +
            "}};" +
            "var x = 10;" +
            "})";

    Node rootNode = AbstractCompiler.parseString(code, null, null);
    scopedAliases.process(null, rootNode);

    // The test should throw an AssertionError due to GOOG_SCOPE_ALIAS_REDEFINED error.
  }
}