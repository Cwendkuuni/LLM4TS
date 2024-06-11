import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ParserConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    // Set up a test source file
    String sourceCode = "function test() {\n" +
            "  // Test code goes here\n" +
            "}";
    sourceFile = new SourceFile("TestFile.java", sourceCode);
  }

  @Test
  public void testJsAstGeneration() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compilerForTest = new AbstractCompiler();
    Node root = ast.getAstRoot(compilerForTest);
    assertNotNull(root);
    assertEquals("function test() {\n  // Test code goes here\n}", sourceFile.getCode());
  }

  @Test
  public void testParsingError() {
    String invalidSourceCode = "function test() {\n" +
            "  // Syntax error\n" +
            "} function test2() {\n" +
            "  // Another test function\n" +
            "}";
    sourceFile = new SourceFile("InvalidFile.java", invalidSourceCode);
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compilerForTest = new AbstractCompiler();
    Node root = ast.getAstRoot(compilerForTest);
    assertNotNull(root);
    assertEquals(1, compilerForTest.getErrors().size());
    JSError error = compilerForTest.getErrors().get(0);
    assertEquals(JSError.Type.PARSER_ERROR, error.getType());
  }

  @Test
  public void testIOException() {
    String emptyFileName = "";
    sourceFile = new SourceFile(emptyFileName, "");
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compilerForTest = new AbstractCompiler();
    compilerForTest.report(JSError.make(AbstractCompiler.READ_ERROR, emptyFileName));
    Node root = ast.getAstRoot(compilerForTest);
    assertNull(root);
  }

  @Test
  public void testParseWithConfig() {
    ParserConfig parserConfig = new ParserConfig();
    parserConfig.setAllowSourceInfo(true);
    sourceFile.setParserConfig(parserConfig);
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compilerForTest = new AbstractCompiler();
    Node root = ast.getAstRoot(compilerForTest);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compilerForTest = new AbstractCompiler();
    Node originalRoot = ast.getAstRoot(compilerForTest);
    ast.clearAst();
    Node newRoot = ast.getAstRoot(compilerForTest);
    assertNotNull(newRoot);
    assertNotNull(originalRoot);
    assertNotNull(originalRoot.getParent());
    assertEquals(originalRoot.getParent(), newRoot.getParent());
  }
}