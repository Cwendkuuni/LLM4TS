import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSCodingConventions.SCOPED_ALIASES_CLASS_NAME;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases_BasicFunctionality() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";
    
    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    Node aliasUsage = scopedAliases.getTraversal().getAliasUsages().get(0);
    assertEquals("dom", aliasUsage.getString());

    assertNull(compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR));

    assertEquals(1, compiler.getDiagnostics().getDiagnostics().size());
    DiagnosticType errorType = compiler.getDiagnostics().getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, errorType);

    String errorMessage = compiler.getDiagnostics().getDiagnostics().get(0).getMessage(errorType);
    assertTrue(errorMessage.contains("Renamed"));
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperUsage() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "function googScope() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "}" +
        "googScope();" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // This test should throw an exception because of the improper usage of goog.scope.
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_ImproperParameters() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "function googScope(param) {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "}" +
        "googScope('test');" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // This test should throw an exception because of the improper parameters passed to goog.scope.
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_RefersToThis() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "this.var = 'test';" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // This test should throw an exception because the body of a goog.scope function cannot reference 'this'.
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_UsesReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "return dom.createElement(DIV);" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // This test should throw an exception because the body of a goog.scope function cannot use 'return'.
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_UsesThrow() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "try {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  throw new Error('Test');" +
        "} catch (e) {" +
        "  // Do nothing" +
        "}" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // This test should throw an exception because the body of a goog.scope function cannot use 'throw'.
  }

  @Test
  public void testScopedAliases_AliasRedefined() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "var dom = goog.dom;" + // Redefining 'dom'
        "dom.createElement(DIV);" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());

    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertEquals("dom", aliasUsages.get(0).getString());
    assertEquals("dom", aliasUsages.get(1).getString());

    assertNotNull(compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR));
    assertEquals(1, compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR));
    DiagnosticType errorType = compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR).getType();
    assertEquals(DiagnosticType.ERROR, errorType);
    String errorMessage = compiler.getDiagnostics().getErrorMessage(DiagnosticType.ERROR, 0);
    assertTrue(errorMessage.contains("Aliased name"));
    assertTrue(errorMessage.contains("redefined"));
  }

  @Test
  public void testScopedAliases_AliasNonLocal() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.RENAME);
    ErrorReporter errorReporter = new ErrorReporter() {};

    String sourceCode = "goog.scope(function() {" +
        "var test = 0;" +
        "goog.scope(function() {" +
        "  var test = 1;" + // Redefining 'test' which is not an alias
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});" +
        "});";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.parse();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals("dom", aliasUsages.get(0).getString());

    assertNotNull(compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR));
    assertEquals(1, compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR));
    DiagnosticType errorType = compiler.getDiagnostics().getErrorCount(DiagnosticType.ERROR).getType();
    assertEquals(DiagnosticType.ERROR, errorType);
    String errorMessage = compiler.getDiagnostics().getErrorMessage(DiagnosticType.ERROR, 0);
    assertTrue(errorMessage.contains("Local variable"));
    assertTrue(errorMessage.contains("is in a goog.scope and is not an alias"));
  }
}