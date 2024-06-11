import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.testtype.ErrorTestType.error;
import static com.google.javascript.jscomp.parser.testtype.ErrorTestType.warning;
import static com.google.javascript.jscomp.parser.testtype.SourceFileTestType.sourceFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals("Expected script node", Node.SCRIPT_NODE_TYPE, astRoot.getType());
  }

  @Test
  public void testClearAst() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);
    assertNull("Expected new ast to be null after clearAst call", newAstRoot);
  }

  @Test
  public void testGetInputId() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();
    assertEquals("Expected inputId to match sourceFile name", "test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertEquals("Expected sourceFiles to be equal", sourceFile, actualSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile1 = sourceFile("test1.js", "function test1() {}");
    SourceFile sourceFile2 = sourceFile("test2.js", "function test2() {}");
    JsAst jsAst = new JsAst(sourceFile1);

    jsAst.setSourceFile(sourceFile2);
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertEquals("Expected sourceFiles to be equal after setSourceFile call", sourceFile2, actualSourceFile);
  }

  @Test
  public void testParseWithValidInput() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(options);

    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals("Expected script node", Node.SCRIPT_NODE_TYPE, astRoot.getType());
    assertTrue("Expected no errors", compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testParseWithErrors() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {\n^");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(options);

    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals("Expected script node", Node.SCRIPT_NODE_TYPE, astRoot.getType());
    assertFalse("Expected errors", compiler.getDiagnostics().isEmpty());

    JSError firstError = compiler.getDiagnostics().get(0);
    assertEquals("Expected error type", JSError.SYNTAX_ERROR, firstError.getType());
    assertEquals("Expected error message", "Parsing error at line: 1 column: 0. Expected ')' but found end of input.", firstError.getMessage());
  }

  @Test
  public void testParseWithWarnings() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CompilerOptions.WarningLevel.VERBOSE);
    SourceFile sourceFile = sourceFile("test.js", "function test() { \n^");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(options);

    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals("Expected script node", Node.SCRIPT_NODE_TYPE, astRoot.getType());
    assertFalse("Expected warnings", compiler.getDiagnostics().isEmpty());

    int warningCount = 0;
    for (JSError error : compiler.getDiagnostics()) {
      if (error.getType() == JSError.TYPE_WARNING) {
        warningCount++;
      }
    }

    assertEquals("Expected one warning", 1, warningCount);
    assertEquals("Expected warning message", "Parsing error at line: 1 column: 0. Expected ')' but found end of input.", compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testClearCachedSource() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    SourceFile sourceFile = sourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);

    jsAst.clearCachedSource();
    String actualSource = sourceFile.getCode();
    assertEquals("Expected source to be empty after clearCachedSource call", "", actualSource);
  }
}