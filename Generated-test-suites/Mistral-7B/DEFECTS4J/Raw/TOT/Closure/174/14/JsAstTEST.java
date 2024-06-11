import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.testutil.CodeSnippetTestUtil.assertCompilesTo;
import static com.google.javascript.jscomp.testutil.CodeSnippetTestUtil.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testValidJsFileParsing() {
    String jsCode = "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}";

    TestSourceFile sourceFile = new TestSourceFile("helloWorld.js", jsCode);
    JsAst ast = new JsAst(sourceFile);

    Node root = ast.getAstRoot(compiler);

    assertCompilesTo(compiler, root, "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}");

    assertNotNull(root);
  }

  @Test
  public void testInvalidJsFileParsing() {
    String jsCode = "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}";

    TestSourceFile sourceFile = new TestSourceFile("helloWorld.js", jsCode);
    JsAst ast = new JsAst(sourceFile);

    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);

    try {
      ParserRunner.parse(sourceFile, jsCode, options, compiler.getDefaultErrorReporter(), logger_);
      assertThrows(IllegalStateException.class, () -> ast.getAstRoot(compiler));
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testClearAst() {
    String jsCode = "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}";

    TestSourceFile sourceFile = new TestSourceFile("helloWorld.js", jsCode);
    JsAst ast = new JsAst(sourceFile);

    Node root = ast.getAstRoot(compiler);

    assertCompilesTo(compiler, root, "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}");

    ast.clearAst();

    assertThrows(NullPointerException.class, () -> ast.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    String jsCode = "function helloWorld() {\n" +
        "  print('Hello World');\n" +
        "}";

    TestSourceFile sourceFile = new TestSourceFile("helloWorld.js", jsCode);
    JsAst ast = new JsAst(sourceFile);

    InputId inputId = ast.getInputId();

    assertTrue(inputId.getName().equals("helloWorld.js"));
  }
}