import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    
    assertNull(jsAst.getAstRoot(null));
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile sf = jsAst.getSourceFile();
    
    assertNotNull(sf);
    assertEquals("testFile.js", sf.getName());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile newSourceFile = new SourceFile("newTestFile.js");
    jsAst.setSourceFile(newSourceFile);
    
    assertEquals("newTestFile.js", jsAst.getSourceFile().getName());
  }

}