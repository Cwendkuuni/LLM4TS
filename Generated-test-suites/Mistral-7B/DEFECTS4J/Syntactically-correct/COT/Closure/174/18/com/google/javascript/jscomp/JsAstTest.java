import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.InputId;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = Mockito.spy(new AbstractCompiler() {
      @Override
      public ParserRunner getParser() {
        return Mockito.mock(ParserRunner.class);
      }
    });
    sourceFile = Mockito.mock(SourceFile.class);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    ParserRunner parserRunner = Mockito.mock(ParserRunner.class);
    when(compiler.getParser()).thenReturn(parserRunner);

    Node root = Mockito.mock(Node.class);
    ParserRunner.ParseResult parseResult = new ParserRunner.ParseResult();
    parseResult.ast = root;

    when(parserRunner.parse(ArgumentMatchers.any(SourceFile.class), ArgumentMatchers.anyString(),
        ArgumentMatchers.any(ParserConfig.class), ArgumentMatchers.any(ErrorReporter.class),
        ArgumentMatchers.any(Logger.class))).thenReturn(parseResult);

    Node expectedAstRoot = jsAst.getAstRoot(compiler);
    assertEquals(root, expectedAstRoot);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    String expectedInputId = "test.js";
    when(sourceFile.getName()).thenReturn(expectedInputId);
    InputId actualInputId = jsAst.getInputId();
    assertEquals(expectedInputId, actualInputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile expectedSourceFile = Mockito.mock(SourceFile.class);
    when(sourceFile).thenReturn(expectedSourceFile);
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertEquals(expectedSourceFile, actualSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    String newFileName = "newTest.js";
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn(newFileName);
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newFileName, jsAst.getInputId().getName());
  }

  @Test(expected = AssertionError.class)
  public void testParseWithIOException() {
    when(compiler.getParser()).thenThrow(new IOException());
    jsAst.parse(compiler);
  }

  @Test
  public void testParseWithNullAst() {
    when(compiler.getParser()).thenReturn(null);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }
}