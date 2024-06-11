import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class JsAstTest {
  
  @Mock
  private SourceFile sourceFile;
  
  @Mock
  private AbstractCompiler compiler;
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot() {
    // Typical use case
    jsAst.getAstRoot(compiler);
    verify(sourceFile, times(1)).getName();
    verify(sourceFile, times(1)).getCode();
    verify(compiler, times(1)).getParserConfig();
    verify(compiler, times(1)).getDefaultErrorReporter();
    verify(compiler, times(1)).setOldParseTree(eq(sourceFile.getName()), any());
    verify(compiler, times(1)).prepareAst(any());
    verify(sourceFile, times(1)).clearCachedSource();
    verify(sourceFile, never()).setName(anyString());
    verify(sourceFile, never()).getCodeForCompilation();
  }
  
  @Test
  public void testClearAst() {
    // Typical use case
    jsAst.clearAst();
    verify(sourceFile, times(1)).clearCachedSource();
    // Edge case - root is set to null
    verify(sourceFile, times(1)).getName();
    verify(sourceFile, never()).getCode();
    verify(compiler, never()).getParserConfig();
    verify(compiler, never()).getDefaultErrorReporter();
    verify(compiler, never()).setOldParseTree(any(), any());
    verify(compiler, never()).prepareAst(any());
  }
  
  @Test
  public void testGetInputId() {
    // Typical use case
    jsAst.getInputId();
    verify(sourceFile, times(1)).getName();
    // Edge case - fileName is not equal to SourceFile's name
    verify(sourceFile, never()).getCode();
    verify(compiler, never()).getParserConfig();
    verify(compiler, never()).getDefaultErrorReporter();
    verify(compiler, never()).setOldParseTree(any(), any());
    verify(compiler, never()).prepareAst(any());
    verify(sourceFile, never()).clearCachedSource();
    verify(sourceFile, never()).setName(anyString());
    verify(sourceFile, never()).getCodeForCompilation();
  }
  
  @Test
  public void testGetSourceFile() {
    // Typical use case
    jsAst.getSourceFile();
    verify(sourceFile, times(1)).getCode();
    // Edge case - fileName is not equal to SourceFile's name
    verify(sourceFile, never()).getName();
    verify(compiler, never()).getParserConfig();
    verify(compiler, never()).getDefaultErrorReporter();
    verify(compiler, never()).setOldParseTree(any(), any());
    verify(compiler, never()).prepareAst(any());
    verify(sourceFile, never()).clearCachedSource();
    verify(sourceFile, never()).setName(anyString());
    verify(sourceFile, never()).getCodeForCompilation();
  }
  
  @Test
  public void testSetSourceFile() {
    // Typical use case
    String fileName = "test.js";
    when(sourceFile.getName()).thenReturn(fileName);
    jsAst.setSourceFile(sourceFile);
    verify(sourceFile, times(1)).getName();
    verify(sourceFile, never()).getCode();
    verify(compiler, never()).getParserConfig();
    verify(compiler, never()).getDefaultErrorReporter();
    verify(compiler, never()).setOldParseTree(any(), any());
    verify(compiler, never()).prepareAst(any());
    verify(sourceFile, never()).clearCachedSource();
    verify(sourceFile, times(1)).setName(eq(fileName));
    verify(sourceFile, never()).getCodeForCompilation();
    // Error scenario - fileName is not equal to SourceFile's name
    String wrongFileName = "wrong.js";
    when(sourceFile.getName()).thenReturn(wrongFileName);
    jsAst.setSourceFile(sourceFile);
    verify(sourceFile, times(2)).getName();
    verify(sourceFile, never()).getCode();
    verify(compiler, never()).getParserConfig();
    verify(compiler, never()).getDefaultErrorReporter();
    verify(compiler, never()).setOldParseTree(any(), any());
    verify(compiler, never()).prepareAst(any());
    verify(sourceFile, never()).clearCachedSource();
    verify(sourceFile, times(1)).setName(eq(fileName));
    verify(sourceFile, never()).setName(eq(wrongFileName));
    verify(sourceFile, never()).getCodeForCompilation();
  }
  
  @Test
  public void testParse() {
    // Typical use case
    jsAst.parse(compiler);
    verify(sourceFile, times(1)).getCode();
    verify(compiler, times(1)).getParserConfig();
    verify(compiler, times(1)).getDefaultErrorReporter();
    verify(compiler, times(1)).setOldParseTree(any(), any());
    verify(compiler, times(1)).prepareAst(any());
    verify(sourceFile, times(1)).clearCachedSource();
    verify(sourceFile, times(1)).setName(anyString());
    verify(sourceFile, never()).getCodeForCompilation();
    // Error scenario - IOException
    when(sourceFile.getName()).thenReturn("test.js");
    doThrow(new IOException()).when(compiler).report(any());
    jsAst.parse(compiler);
    verify(sourceFile, times(2)).getCode();
    verify(compiler, times(2)).getParserConfig();
    verify(compiler, times(2)).getDefaultErrorReporter();
    verify(compiler, times(2)).setOldParseTree(any(), any());
    verify(compiler, times(2)).prepareAst(any());
    verify(sourceFile, times(2)).clearCachedSource();
    verify(sourceFile, times(2)).setName(anyString());
    verify(sourceFile, times(1)).getCodeForCompilation();
    // Error scenario - root is null
    when(sourceFile.getName()).thenReturn("test.js");
    ParserRunner.ParseResult result = mock(ParserRunner.ParseResult.class);
    when(result.ast).thenReturn(null);
    when(ParserRunner.parse(sourceFile, sourceFile.getCode(),
          compiler.getParserConfig(),
          compiler.getDefaultErrorReporter(),
          JsAst.logger_)).thenReturn(result);
    jsAst.parse(compiler);
    verify(sourceFile, times(3)).getCode();
    verify(compiler, times(3)).getParserConfig();
    verify(compiler, times(3)).getDefaultErrorReporter();
    verify(compiler, times(3)).setOldParseTree(any(), any());
    verify(compiler, times(2)).prepareAst(any());
    verify(sourceFile, times(3)).clearCachedSource();
    verify(sourceFile, times(3)).setName(anyString());
    verify(sourceFile, times(1)).getCodeForCompilation();
  }
}