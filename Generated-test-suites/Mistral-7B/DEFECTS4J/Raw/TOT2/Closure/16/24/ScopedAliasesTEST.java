import static org.junit.Assert.*;
import static com.google.javascript.jscomp.AbstractCompiler.WarningLevel;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.LogLevel;
import static com.google.javascript.rhino.SourcePosition.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourceFile;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  private Compiler compiler;
  private SourceFile sourceFile;
  private Node rootNode;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.WARNING, DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY);
    options.setWarningLevel(WarningLevel.ERROR, DiagnosticType.JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS);
    compiler = new Compiler(options);
    sourceFile = new SourceFile("TestScopedAliases.js");
    rootNode = sourceFile.getAST();
  }

  /**
   * Test case for valid usage of goog.scope with a single statement.
   */
  @Test
  public void testValidUsage() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getScopeCalls().size());
  }

  /**
   * Test case for invalid usage of goog.scope with multiple statements.
   */
  @Test(expected = AssertionError.class)
  public void testInvalidUsage() {
    String jsCode = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  goog.scope(function() {\n" +
        "    var y = 2;\n" +
        "  });\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with a single parameter.
   */
  @Test
  public void testSingleParameter() {
    String jsCode = "goog.scope(function() {\n" +
        "  var x = goog.dom;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.NAME, aliasDefinition.getType());
    assertEquals("goog.dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(Token.NAME, aliasUsage.getType());
    assertEquals("goog.dom", aliasUsage.getString());
  }

  /**
   * Test case for goog.scope with non-anonymous function.
   */
  @Test(expected = AssertionError.class)
  public void testNonAnonymousFunction() {
    String jsCode = "goog.scope(function foo() {\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with a throw statement.
   */
  @Test(expected = AssertionError.class)
  public void testThrowStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  throw new Error('test error');\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with a return statement.
   */
  @Test(expected = AssertionError.class)
  public void testReturnStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  return 1;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with "this" keyword.
   */
  @Test(expected = AssertionError.class)
  public void testThisKeyword() {
    String jsCode = "goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with alias redefinition.
   */
  @Test(expected = AssertionError.class)
  public void testAliasRedefinition() {
    String jsCode = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  var x = 2;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with an alias used outside of the scope block.
   */
  @Test(expected = AssertionError.class)
  public void testAliasUsedOutside() {
    String jsCode = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "})\n" +
        "console.log(x);";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
  }

  /**
   * Test case for goog.scope with a log level of LOG_SILENT.
   */
  @Test
  public void testLogSilent() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.WARNING, DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY);
    options.setAliasTransformation(AliasTransformation.SIMPLE);
    options.setAliasTransformationHandler(new LogSilentAliasTransformationHandler());
    compiler = new Compiler(options);
    String jsCode = "goog.scope(function() {\n" +
        "  var x = goog.dom;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.NAME, aliasDefinition.getType());
    assertEquals("goog.dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(Token.NAME, aliasUsage.getType());
    assertEquals("goog.dom", aliasUsage.getString());
  }

  /**
   * Test case for goog.scope with a log level of LOG_ERROR.
   */
  @Test
  public void testLogError() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.WARNING, DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY);
    options.setAliasTransformation(AliasTransformation.SIMPLE);
    options.setAliasTransformationHandler(new LogErrorAliasTransformationHandler());
    compiler = new Compiler(options);
    String jsCode = "goog.scope(function() {\n" +
        "  var x = goog.dom;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.NAME, aliasDefinition.getType());
    assertEquals("goog.dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(Token.NAME, aliasUsage.getType());
    assertEquals("goog.dom", aliasUsage.getString());
  }

  /**
   * Test case for goog.scope with a log level of LOG_WARNING.
   */
  @Test
  public void testLogWarning() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.WARNING, DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY);
    options.setAliasTransformation(AliasTransformation.SIMPLE);
    options.setAliasTransformationHandler(new LogWarningAliasTransformationHandler());
    compiler = new Compiler(options);
    String jsCode = "goog.scope(function() {\n" +
        "  var x = goog.dom;\n" +
        "});";
    sourceFile.setCode(jsCode);
    Node rootNode = sourceFile.getAST();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.NAME, aliasDefinition.getType());
    assertEquals("goog.dom", aliasDefinition.getString());
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(Token.NAME, aliasUsage.getType());
    assertEquals("goog.dom", aliasUsage.getString());

    // Check the warning message.
    List<Compiler.Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Compiler.Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY, diagnostic.getCode());
    assertEquals("The call to goog.scope must be alone in a single statement.", diagnostic.getMessage());
    assertEquals(SourcePosition.create(1, 1), diagnostic.getSourcePosition());
  }
}

class LogSilentAliasTransformationHandler implements AliasTransformationHandler {

  @Override
  public LogLevel getLogLevel() {
    return LogLevel.SILENT;
  }
}

class LogErrorAliasTransformationHandler implements AliasTransformationHandler {

  @Override
  public LogLevel getLogLevel() {
    return LogLevel.ERROR;
  }
}

class LogWarningAliasTransformationHandler implements AliasTransformationHandler {

  @Override
  public LogLevel getLogLevel() {
    return LogLevel.WARNING;
  }
}