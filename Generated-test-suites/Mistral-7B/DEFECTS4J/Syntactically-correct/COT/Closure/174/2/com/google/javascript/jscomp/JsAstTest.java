import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.AbstractCompiler;
import com.google.javascript.rhino.ParserConfig;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(new AbstractCompiler());
    jsAst.clearAst();
    Node root = jsAst.getAstRoot(new AbstractCompiler());
    assertEquals(IR.script(), root);
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals(new SourceName("test.js"), inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFileResult = jsAst.getSourceFile();
    assertEquals(sourceFile, sourceFileResult);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFileOriginal = new SourceFile("test.js", "test content");
    SourceFile sourceFileNew = new SourceFile("test2.js", "test content2");
    JsAst jsAst = new JsAst(sourceFileOriginal);
    jsAst.setSourceFile(sourceFileNew);
    SourceFile sourceFileResult = jsAst.getSourceFile();
    assertEquals(sourceFileNew, sourceFileResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentNames() {
    SourceFile sourceFileOriginal = new SourceFile("test.js", "test content");
    SourceFile sourceFileNew = new SourceFile("test2.js", "test content2");
    JsAst jsAst = new JsAst(sourceFileOriginal);
    jsAst.setSourceFile(sourceFileNew);
  }

  @Test
  public void testParseNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullSourceFile() {
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(null);
    jsAst.parse(compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseWithDifferentSourceFile() {
    SourceFile sourceFileOriginal = new SourceFile("test.js", "test content");
    SourceFile sourceFileNew = new SourceFile("test2.js", "test content2");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFileOriginal);
    jsAst.parse(compiler);
    jsAst.parse(compiler); // second parse should fail
  }

  @Test
  public void testParseWithHaltingErrors() {
    SourceFile sourceFile = new SourceFile("test.js", "test content with error");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertEquals(IR.script(), root);
  }

  @Test
  public void testParseWithIOError() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    try {
      jsAst.parse(compiler);
      fail("Expected IOException");
    } catch (IOException e) {
      // expected
    }
  }
}