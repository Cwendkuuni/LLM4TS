import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
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

  @Test(expected = AssertionError.class)
  public void testSetSourceFileWithDifferentName() {
    jsAst.setSourceFile(new SourceFile("test2.js", "test content"));
  }

  @Test
  public void testParseWithValidCode() {
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testParseWithIOException() {
    when(compiler.getParserConfig().getFileReader().readString(anyString())).thenThrow(new IOException());
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseWithParsingError() {
    compiler.getErrorReporter().setThrowOnError(true);
    when(compiler.getParserConfig().getFileReader().readString(anyString()))
        .thenReturn("invalid code");
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseWithEmptyCode() {
    when(compiler.getParserConfig().getFileReader().readString(anyString()))
        .thenReturn("");
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot);
  }

  @Test
  public void testParseWithMalformedCode() {
    when(compiler.getParserConfig().getFileReader().readString(anyString()))
        .thenReturn("malformed code");
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  private void when(MethodType methodType, String expected) {
    // Mock the FileReader and replace it with a mock that returns the expected code
    MockFileReader mockFileReader = new MockFileReader();
    when(compiler.getParserConfig().getFileReader()).thenReturn(mockFileReader);
    mockFileReader.setReturnValue(expected);
  }

  @Test
  public void testSetSourceFileWithSameName() {
    SourceFile newSourceFile = new SourceFile("test.js", "test content");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  private static class MockFileReader {
    private String returnValue;

    public String readString(String path) {
      return returnValue;
    }

    public void setReturnValue(String value) {
      this.returnValue = value;
    }
  }
}