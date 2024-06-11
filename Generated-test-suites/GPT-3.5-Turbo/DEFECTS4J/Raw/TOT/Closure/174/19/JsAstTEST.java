import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot_NullRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    
    assertNotNull(root);
  }
  
  @Test
  public void testGetAstRoot_NonNullRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    
    assertNotNull(root);
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    
    assertNull(jsAst.getAstRoot());
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    
    assertNotNull(returnedSourceFile);
    assertEquals("test.js", returnedSourceFile.getName());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile newSourceFile = new SourceFile("new.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    
    assertNotNull(returnedSourceFile);
    assertEquals("new.js", returnedSourceFile.getName());
  }
  
  @Test
  public void testParse_Successful() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    
    assertNotNull(jsAst.getAstRoot());
    assertFalse(compiler.hasHaltingErrors());
  }
  
  @Test
  public void testParse_ParseError() {
    SourceFile sourceFile = new SourceFile("test.js", "var x 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    
    assertNull(jsAst.getAstRoot());
    assertTrue(compiler.hasHaltingErrors());
  }
  
  @Test
  public void testParse_IOException() {
    SourceFile sourceFile = new SourceFile("not_found.js", "");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    
    assertNull(jsAst.getAstRoot());
    assertTrue(compiler.hasHaltingErrors());
  }
  
}