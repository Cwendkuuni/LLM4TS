
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.IOException;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Mock
  AbstractCompiler compilerMock;

  @Test
  public void testGetAstRoot() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello';");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compilerMock);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello';");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compilerMock));
  }

  @Test
  public void testGetInputId() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello';");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(jsAst.getInputId().getIdName(), "test.js");
  }

  @Test
  public void testSetSourceFile() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello';");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("test2.js", "var y = 'hello';");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(jsAst.getSourceFile().getName(), "test2.js");
  }
  
  @Test
  public void testParseWithIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello';");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler mockCompiler = Mockito.mock(AbstractCompiler.class);
    Mockito.doThrow(IOException.class).when(mockCompiler).report(JS_Error.make(Mockito.any(), Mockito.anyString()));
    jsAst.parse(mockCompiler);
    assertNull(jsAst.getAstRoot(compilerMock));
  }

  @Test
  public void testParseWithParseError() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 'hello'");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler mockCompiler = Mockito.mock(AbstractCompiler.class);
    Mockito.doReturn(true).when(mockCompiler).hasHaltingErrors();
    jsAst.parse(mockCompiler);
    assertNotNull(jsAst.getAstRoot(compilerMock));
  }

}
