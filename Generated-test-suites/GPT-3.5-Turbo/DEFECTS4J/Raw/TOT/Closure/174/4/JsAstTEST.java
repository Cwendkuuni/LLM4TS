import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    
    Node astRoot = jsAst.getAstRoot(compiler);
    
    assertNotNull(astRoot);
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    
    assertNull(jsAst.getAstRoot(null));
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new_test.js");
    
    jsAst.setSourceFile(newSourceFile);
    
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = new InputId("test.js");
    
    assertEquals(inputId, jsAst.getInputId());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    
    assertEquals(sourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testParse() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    
    jsAst.parse(compiler);
    
    assertNotNull(jsAst.getAstRoot(compiler));
  }
}