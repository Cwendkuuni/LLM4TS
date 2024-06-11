import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.List;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    dom.createElement(DIV);\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation(
        AliasTransformation.GOOG_DOM, "goog.dom"));
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());
  }

  @Test
  public void testScopedAliasesWithMissingAlias() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var missing = goog.someMissingLibrary;\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The call to goog.scope must be alone in a single statement.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithMultipleStatements() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    dom.createElement(DIV);\n" +
        "    goog.typeOf(1);\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The call to goog.scope must be alone in a single statement.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithFunctionAsParameter() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function(dom) {\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    dom.createElement(DIV);\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The call to goog.scope must take only a single parameter.  It must " +
        "be an anonymous function that itself takes no parameters.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    this.value = 42;\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The body of a goog.scope function cannot reference 'this'.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    return 42;\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The body of a goog.scope function cannot use 'return'.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error('Test error');\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The body of a goog.scope function cannot use 'throw'.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  @Test
  public void testScopedAliasesWithDuplicateAlias() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "  });\n" +
        "}");
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);
    scopedAliases.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();
    String errorMessage = "Error: The alias DIV is assigned a value more than once.";
    assertErrorMessage(scopedAliases.getErrors().toString(), errorMessage);
  }

  private void assertErrorMessage(String errorMessage, String expectedErrorMessage) {
    List<String> actualErrors = NodeUtil.getErrorsFromDiagnostics(errorMessage);
    for (String error : actualErrors) {
      assertTrue(error.contains(expectedErrorMessage));
    }
  }
}