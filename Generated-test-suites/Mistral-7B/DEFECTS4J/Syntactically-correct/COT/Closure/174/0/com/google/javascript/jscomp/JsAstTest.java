import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parser.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    assertEquals("test.js", jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newTest.js", jsAst.getInputId().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentFileName() {
    SourceFile newSourceFile = new SourceFile("test.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParse() throws IOException {
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    jsAst.parse(compiler);
    throw new IOException();
  }

  @Test
  public void testParseEmptyFile() {
    sourceFile.setCode("");
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot);
  }
}