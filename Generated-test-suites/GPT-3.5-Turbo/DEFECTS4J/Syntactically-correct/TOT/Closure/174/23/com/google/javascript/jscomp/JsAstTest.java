import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsAstTest {
  
  private JsAst jsAst;
  private SourceFile sourceFile;
  
  @Before
  public void setUp() {
    sourceFile = Mockito.mock(SourceFile.class);
    when(sourceFile.getName()).thenReturn("test.js");
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot_astNotParsed_parseCalled() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
    parseResult.ast = Mockito.mock(Node.class);
    parseResult.oldAst = Mockito.mock(Node.class);
    when(parseResult.ast.isScript()).thenReturn(true);
    try {
      when(ParserRunner.parse(eq(sourceFile), anyString(), 
          any(Parser.Config.class), any(ErrorReporter.class), any(Logger.class))).thenReturn(parseResult);
    } catch(IOException e) {
      fail("IOException occurred while parsing");
    }
    
    Node root = jsAst.getAstRoot(compiler);
    
    verify(compiler).setOldParseTree(eq("test.js"), eq(parseResult.oldAst));
    verify(parseResult.ast).setInputId(any(InputId.class));
    verify(parseResult.ast).setStaticSourceFile(eq(sourceFile));
    assertEquals(parseResult.ast, root);
  }
  
  @Test
  public void testGetAstRoot_astAlreadyParsed_returnExistingAst() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
    parseResult.ast = Mockito.mock(Node.class);
    parseResult.oldAst = Mockito.mock(Node.class);
    jsAst.getAstRoot(compiler);
    Node existingRoot = jsAst.getAstRoot(compiler);
    
    verifyNoMoreInteractions(compiler);
    assertEquals(parseResult.ast, existingRoot);
  }
  
  @Test
  public void testClearAst_astExists_astCleared() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
    parseResult.ast = Mockito.mock(Node.class);
    parseResult.oldAst = Mockito.mock(Node.class);
    jsAst.getAstRoot(compiler);
    
    jsAst.clearAst();
    
    Node root = jsAst.getAstRoot(compiler);
    verify(compiler, times(2)).setOldParseTree(eq("test.js"), eq(parseResult.oldAst));
    verify(parseResult.ast, times(2)).setInputId(any(InputId.class));
    verify(parseResult.ast, times(2)).setStaticSourceFile(eq(sourceFile));
    assertNull(root);
  }
  
  @Test
  public void testGetInputId_returnInputId() {
    InputId expectedInputId = Mockito.mock(InputId.class);
    jsAst = new JsAst(sourceFile);
    when(sourceFile.getInputId()).thenReturn(expectedInputId);
    
    InputId inputId = jsAst.getInputId();
    
    verify(sourceFile).getInputId();
    assertEquals(expectedInputId, inputId);
  }
  
  @Test
  public void testGetSourceFile_returnSourceFile() {
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    
    assertEquals(sourceFile, returnedSourceFile);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSetSourceFile_fileNameMismatch_throwIllegalStateException() {
    SourceFile mismatchedSourceFile = Mockito.mock(SourceFile.class);
    when(mismatchedSourceFile.getName()).thenReturn("other.js");
    
    jsAst.setSourceFile(mismatchedSourceFile);
  }
}