import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsAstTest {
  @Mock
  private AbstractCompiler mockCompiler;
  @Mock
  private SourceFile mockSourceFile;
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(mockSourceFile.getName()).thenReturn("test.js");
    jsAst = new JsAst(mockSourceFile);
  }
  
  @Test
  public void testGetAstRoot_NullRoot_ParseCalled() throws IOException {
    ParserRunner.ParseResult mockParseResult = mock(ParserRunner.ParseResult.class);
    mockParseResult.ast = mock(Node.class);
    when(mockSourceFile.getCode()).thenReturn("test code");
    when(ParserRunner.parse(any(), any(), any(), any(), any())).thenReturn(mockParseResult);

    Node astRoot = jsAst.getAstRoot(mockCompiler);

    verify(ParserRunner.class, times(1)).parse(mockSourceFile, "test code",
        mockCompiler.getParserConfig(),
        mockCompiler.getDefaultErrorReporter(),
        JsAst.logger_);
    assertNotNull(astRoot);
    assertEquals(mockParseResult.ast, astRoot);
  }
  
  @Test
  public void testGetAstRoot_CachedRoot_ParseNotCalled() {
    Node mockRoot = mock(Node.class);
    jsAst.setRoot(mockRoot);

    Node astRoot = jsAst.getAstRoot(mockCompiler);

    verifyZeroInteractions(ParserRunner.class);
    assertEquals(mockRoot, astRoot);
  }
  
  @Test
  public void testClearAst_RootSetToNull_SourceFileCleared() {
    jsAst.setRoot(mock(Node.class));
    when(mockSourceFile.getCode()).thenReturn("test code");

    jsAst.clearAst();

    assertNull(jsAst.getRoot());
    verify(mockSourceFile, times(1)).clearCachedSource();
  }
  
  @Test
  public void testGetInputId_Initialized_ReturnsExpectedInputId() {
    InputId expectedInputId = new InputId("test.js");

    InputId inputId = jsAst.getInputId();

    assertEquals(expectedInputId, inputId);
  }
  
  @Test
  public void testGetSourceFile_Initialized_ReturnsExpectedSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();

    assertEquals(mockSourceFile, sourceFile);
  }
  
  @Test
  public void testSetSourceFile_CorrectFileName_SourceFileSet() {
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn("test.js");

    jsAst.setSourceFile(newSourceFile);

    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSetSourceFile_IncorrectFileName_ExceptionThrown() {
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn("other.js");

    jsAst.setSourceFile(newSourceFile);
  }
  
  @Test
  public void testParse_SuccessfulParsing_RootAndCompilerPrepared() throws IOException {
    ParserRunner.ParseResult mockParseResult = mock(ParserRunner.ParseResult.class);
    mockParseResult.ast = mock(Node.class);
    when(mockSourceFile.getCode()).thenReturn("test code");
    when(ParserRunner.parse(any(), any(), any(), any(), any())).thenReturn(mockParseResult);

    jsAst.parse(mockCompiler);

    verify(compiler, times(1)).prepareAst(mockParseResult.ast);
    assertEquals(mockParseResult.ast, jsAst.getRoot());
  }
  
  @Test
  public void testParse_IOException_ReportedError() throws IOException {
    when(mockSourceFile.getCode()).thenReturn("test code");
    when(ParserRunner.parse(any(), any(), any(), any(), any())).thenThrow(IOException.class);
    
    jsAst.parse(mockCompiler);

    verify(mockCompiler, times(1)).report(eq(JSError.make(AbstractCompiler.READ_ERROR, mockSourceFile.getName())));
    assertNotNull(jsAst.getRoot());
  }
  
  @Test
  public void testParse_NullRoot_DummyRootCreated() throws IOException {
    jsAst.setSourceFile(mock(SourceFile.class));
    when(mockCompiler.hasHaltingErrors()).thenReturn(true);

    jsAst.parse(mockCompiler);

    assertNotNull(jsAst.getRoot());
    assertTrue(jsAst.getRoot().isScript());
  }
  
  @Test
  public void testParse_SuccessfulParsing_InputIdSet() throws IOException {
    ParserRunner.ParseResult mockParseResult = mock(ParserRunner.ParseResult.class);
    mockParseResult.ast = mock(Node.class);
    when(mockSourceFile.getCode()).thenReturn("test code");
    when(ParserRunner.parse(any(), any(), any(), any(), any())).thenReturn(mockParseResult);

    jsAst.parse(mockCompiler);

    assertEquals(jsAst.getInputId(), jsAst.getRoot().getInputId());
  }
}