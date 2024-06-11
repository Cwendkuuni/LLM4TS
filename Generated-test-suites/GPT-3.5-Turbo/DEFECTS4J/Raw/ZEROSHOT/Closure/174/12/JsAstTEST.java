
import static org.junit.Assert.*;

import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    
    Node astRoot = jsAst.getAstRoot(compiler);
    
    assertNotNull(astRoot);
    assertEquals("script", astRoot.getType());
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    
    assertNull(jsAst.getAstRoot(null));
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    InputId expectedInputId = new InputId("testfile");
    
    InputId inputId = jsAst.getInputId();
    
    assertEquals(expectedInputId, inputId);
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile getSourceFile = jsAst.getSourceFile();
    
    assertEquals(sourceFile, getSourceFile);
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTestFile");
    
    jsAst.setSourceFile(newSourceFile);
    
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testParse() throws IOException {
    SourceFile sourceFile = new SourceFile("testfile");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    
    jsAst.parse(compiler);
    
    assertNotNull(jsAst.getAstRoot(compiler));
    assertEquals("script", jsAst.getAstRoot(compiler).getType());
  }
}
