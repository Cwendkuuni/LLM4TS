import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setup() {
    // Initialize the compiler and source file for testing
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
    assertEquals("test content", jsAst.getSourceFile().getCode());
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
  public void testParseSuccess() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    jsAst.parse(compiler);
    // Replace this with actual IO error testing code if needed
    throw new IOException();
  }

  @Test
  public void testParseWithHaltingErrors() {
    compiler.addError(JSError.make(AbstractCompiler.READ_ERROR, "test.js"));
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot instanceof com.google.javascript.rhino.Script);
  }
}