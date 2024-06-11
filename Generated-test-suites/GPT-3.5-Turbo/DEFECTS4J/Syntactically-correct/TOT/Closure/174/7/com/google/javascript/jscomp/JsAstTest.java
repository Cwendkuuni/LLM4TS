import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsAstTest {
  
  private JsAst jsAst;
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  
  @Before
  public void setUp() {
    sourceFile = mock(SourceFile.class);
    compiler = mock(AbstractCompiler.class);
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot_whenRootIsNull_parseIsCalled() {
    jsAst.getAstRoot(compiler);
    
    verify(compiler, times(1)).setOldParseTree(sourceFile.getName(), null);
  }
  
  @Test
  public void testGetAstRoot_whenRootIsNotNull_parseNotCalled() {
    jsAst.getAstRoot(compiler);
    jsAst.getAstRoot(compiler);
    
    verify(compiler, times(1)).setOldParseTree(sourceFile.getName(), null);
  }
  
  @Test
  public void testClearAst_rootIsSetToNull() {
    jsAst.clearAst();
    
    Assert.assertNull(jsAst.getAstRoot(compiler));
  }
  
  @Test
  public void testClearAst_sourceFileIsCleared() {
    jsAst.clearAst();
    
    verify(sourceFile, times(1)).clearCachedSource();
  }
  
  @Test
  public void testGetInputId_returnsInputId() {
    InputId inputId = new InputId("test");
    when(sourceFile.getName()).thenReturn("test");
    
    InputId result = jsAst.getInputId();
    
    Assert.assertEquals(inputId, result);
  }
  
  @Test
  public void testGetSourceFile_returnsSourceFile() {
    SourceFile result = jsAst.getSourceFile();
    
    Assert.assertEquals(sourceFile, result);
  }
  
  @Test
  public void testSetSourceFile_changesSourceFile() {
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn("test");
    
    jsAst.setSourceFile(newSourceFile);
    
    Assert.assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSetSourceFile_withDifferentFileName_throwsException() {
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn("test2");
    
    jsAst.setSourceFile(newSourceFile);
  }
  
  @Test
  public void testParse_whenParsingSuccessful_compilerMethodsAreCalled() throws IOException {
    String code = "console.log('hello world');";
    ParserRunner.ParseResult parseResult = new ParserRunner.ParseResult(IR.script(), null);
    when(sourceFile.getCode()).thenReturn(code);
    when(sourceFile.getName()).thenReturn("test");
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    
    jsAst.parse(compiler);
    
    verify(compiler, times(1)).setOldParseTree(sourceFile.getName(), null);
    verify(compiler, times(1)).prepareAst(any(Node.class));
    verify(sourceFile, times(1)).clearCachedSource();
    Assert.assertNotNull(jsAst.getAstRoot(compiler));
  }
  
  @Test
  public void testParse_whenParsingThrowsIOException_reportIsCalled() throws IOException {
    when(sourceFile.getName()).thenReturn("test");
    when(sourceFile.getCode()).thenThrow(new IOException());
    
    jsAst.parse(compiler);
    
    verify(compiler, times(1)).report(any(JSError.class));
  }
  
  
  @Test
  public void testParse_whenRootIsNull_returnsDummyScriptNode() throws IOException {
    when(sourceFile.getName()).thenReturn("test");
    when(sourceFile.getCode()).thenReturn(null);
    
    jsAst.parse(compiler);
    
    Assert.assertEquals(IR.script(), jsAst.getAstRoot(compiler));
  }
  
  @Test
  public void testParse_whenCompilerHasHaltingErrors_returnsDummyScriptNode() throws IOException {
    when(sourceFile.getName()).thenReturn("test");
    when(sourceFile.getCode()).thenReturn("alert('hello world');");
    when(compiler.hasHaltingErrors()).thenReturn(true);
    
    jsAst.parse(compiler);
    
    Assert.assertEquals(IR.script(), jsAst.getAstRoot(compiler));
  }
}