import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.List;

public class ScopedAliasesTest {

  @org.junit.Test
  public void testScopedAliasesPass() throws IOException {
    String input = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    assertEquals(0, errors.size());

    Node rootNode = sourceFile.getAST();
    Node result = pass.process(null, rootNode);

    // The resulting code should be: goog.dom.createElement(goog.dom.TagName.DIV);
    String expected = "" +
        "goog.dom.createElement(goog.dom.TagName.DIV);";

    String output = NodeUtil.getSource(result);
    assertEquals(expected, output);
  }

  @org.junit.Test
  public void testScopedAliasesPass_ImproperUsage() throws IOException {
    String input = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  return dom.createElement(DIV); " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    // There should be one error.
    DiagnosticType errorType = GOOG_SCOPE_USES_RETURN;
    String errorMessage = "The body of a goog.scope function cannot use 'return'.";

    assertEquals(1, errors.size());
    assertEquals(errorType.getMessage(), errors.get(0).getMessage());
    assertEquals(errorMessage, errors.get(0).getMessage());
  }

  @org.junit.Test
  public void testScopedAliasesPass_BadParameters() throws IOException {
    String input = "" +
        "goog.scope(function(foo) {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    // There should be one error.
    DiagnosticType errorType = GOOG_SCOPE_HAS_BAD_PARAMETERS;
    String errorMessage = "The call to goog.scope must take only a single parameter.  It must be an anonymous function that itself takes no parameters.";

    assertEquals(1, errors.size());
    assertEquals(
        errorType.getMessage(),
        errors.get(0).getMessage());
    assertEquals(errorMessage, errors.get(0).getMessage());
  }

  @org.junit.Test
  public void testScopedAliasesPass_ThisKeyword() throws IOException {
    String input = "" +
        "goog.scope(function() {" +
        "  this.dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    // There should be one error.
    DiagnosticType errorType = GOOG_SCOPE_REFERENCES_THIS;
    String errorMessage = "The body of a goog.scope function cannot reference 'this'.";

    assertEquals(1, errors.size());
    assertEquals(errorType.getMessage(), errors.get(0).getMessage());
    assertEquals(errorMessage, errors.get(0).getMessage());
  }

  @org.junit.Test
  public void testScopedAliasesPass_RedefinedAlias() throws IOException {
    String input = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  var DIV = 'div'; " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    // There should be one error.
    DiagnosticType errorType = GOOG_SCOPE_ALIAS_REDEFINED;
    String errorMessage = "The alias {0} is assigned a value more than once.";

    assertEquals(1, errors.size());
    assertEquals(errorType.getMessage(), errors.get(0).getMessage());
    assertEquals(errorMessage, errors.get(0).getMessage());
  }

  @org.junit.Test
  public void testScopedAliasesPass_NonAliasLocal() throws IOException {
    String input = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var div = goog.dom.createElement(dom.TagName.DIV); " +
        "});";

    SourceFile sourceFile = new SourceFile("test.js", input);

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation, ALWAYS_ON);

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    List<Diagnostics> errors = compiler.compile(Lists.newArrayList(sourceFile), pass);

    // There should be one error.
    DiagnosticType errorType = GOOG_SCOPE_NON_ALIAS_LOCAL;
    String errorMessage = "The local variable {0} is in a goog.scope and is not an alias.";

    assertEquals(1, errors.size());
    assertEquals(errorType.getMessage(), errors.get(0).getMessage());
    assertEquals(errorMessage, errors.get(0).getMessage());
  }

}