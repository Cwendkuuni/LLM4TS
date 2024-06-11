import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    Assert.assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootWhenCompilerIsNull() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
    Assert.assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    Assert.assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    Assert.assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    JsAst jsAst = new JsAst(new SourceFile("test.js", "test content"));
    jsAst.setSourceFile(newSourceFile);
    Assert.assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileWithDifferentFileName() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    JsAst jsAst = new JsAst(new SourceFile("test.js", "test content"));
    jsAst.setSourceFile(newSourceFile);
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ParserConfig getParserConfig() {
        ParserConfig config = new ParserConfig();
        config.setAllowAllIdentifiers(true);
        return config;
      }
    };
    jsAst.parse(compiler);
  }

  @Test
  public void testParseWithNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
  }

  @Test
  public void testParseWithEmptyCode() {
    SourceFile sourceFile = new SourceFile("test.js", "");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    Assert.assertNotNull(astRoot);
    Assert.assertTrue(astRoot.isNull());
  }

  @Test
  public void testParse() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    Assert.assertNotNull(astRoot);
    Assert.assertFalse(astRoot.isNull());
  }

  @Test
  public void testClearCachedSource() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearCachedSource();
    String cachedSource = sourceFile.getCachedSource();
    Assert.assertNull(cachedSource);
  }
}