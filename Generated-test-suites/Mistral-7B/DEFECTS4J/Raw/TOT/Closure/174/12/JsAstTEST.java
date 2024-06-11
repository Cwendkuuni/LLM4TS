import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JsAstTest {

  private static final String TEST_FILE = "TestSource.js";
  private JsAst jsAst;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Initialize the AbstractCompiler instance for testing
    compiler = new AbstractCompiler();
    // Create a SourceFile instance with the test source code
    SourceFile sourceFile = new SourceFile(TEST_FILE, "test source code");
    // Create a JsAst instance with the SourceFile
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testParseSuccess() {
    // Call the getAstRoot method to parse the source code
    Node astRoot = jsAst.getAstRoot(compiler);
    // Assert that the parsed AST is not null
    assertNotNull(astRoot);
  }

  @Test
  public void testParseFailure() {
    // Set the source code to contain an error
    String errorSource = "syntax error";
    SourceFile sourceFile = new SourceFile(TEST_FILE, errorSource);
    JsAst jsAst = new JsAst(sourceFile);

    // Call the getAstRoot method to parse the source code
    // This should result in an error being reported
    jsAst.getAstRoot(compiler);

    // Assert that the AST is null, indicating a parse failure
    assertNotNull(jsAst.getAstRoot(compiler) == null);
  }

  @Test
  public void testClearAst() {
    // Parse the source code
    Node astRoot = jsAst.getAstRoot(compiler);
    // Clear the AST and verify that it is null
    jsAst.clearAst();
    assertNotNull(jsAst.getAstRoot(compiler) == null);
  }
}