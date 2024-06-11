import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst ast;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
    ast = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node root = ast.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    Node root = ast.getAstRoot(compiler);
    ast.clearAst();
    Node newRoot = ast.getAstRoot(compiler);
    assertNull(newRoot);
  }

  @Test
  public void testGetInputId() {
    assertEquals("test.js", ast.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("test content", ast.getSourceFile().getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    ast.setSourceFile(newSourceFile);
    assertEquals("newTest.js", ast.getInputId().getName());
  }

  @Test(expected = AssertionError.class)
  public void testGetAstRootWithNullCompiler() {
    Node root = ast.getAstRoot(null);
  }

  @Test
  public void testParse() throws IOException {
    parseAndCheckResult(true, ParserRunner.LanguageMode.ECMASCRIPT5);
  }

  @Test
  public void testParseWithErrors() throws IOException {
    String code = "function test(){\n};test()";
    sourceFile.setCode(code);
    parseAndCheckResult(false, ParserRunner.LanguageMode.ECMASCRIPT5);
  }

  @Test
  public void testParseWithOldAST() throws IOException {
    String code = "function test(){\n};test()";
    sourceFile.setCode(code);
    ParserRunner.ParseResult oldParseResult = ParserRunner.parse(sourceFile, code, null, null, null);
    compiler.setOldParseTree(sourceFile.getName(), oldParseResult.ast);
    Node root = ast.getAstRoot(compiler);
    assertNotNull(root);
    // Check that old parse tree was replaced.
    assertNull(compiler.getOldParseTree(sourceFile.getName()));
  }

  @Test
  public void testParseWithIOException() {
    sourceFile.setCode("not a valid file");
    ast.parse(compiler);
    fail("Expected IOException but no exception was thrown.");
  }

  private void parseAndCheckResult(boolean expectedSuccess, ParserRunner.LanguageMode languageMode) throws IOException {
    ast.parse(compiler);
    if (expectedSuccess) {
      Node root = ast.getAstRoot(compiler);
      assertNotNull(root);
    } else {
      assertTrue(compiler.hasHaltingErrors());
    }
    // Check parse result language mode.
    assertEquals(languageMode, compiler.getParserConfig().getLanguageMode());
  }
}