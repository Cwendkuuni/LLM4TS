import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(new AbstractCompiler());
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test content 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentNames() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test content 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
  }

  @Test
  public void testParseSuccess() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    compiler.report(JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));
  }

  @Test
  public void testParseNullAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseOldParseTree() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node oldAst = new Node();
    jsAst.setSourceFile(sourceFile);
    jsAst.getAstRoot(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assert NotNull (astRoot);
    assertEquals(oldAst, jsAst.getOldParseTree());
  }
}