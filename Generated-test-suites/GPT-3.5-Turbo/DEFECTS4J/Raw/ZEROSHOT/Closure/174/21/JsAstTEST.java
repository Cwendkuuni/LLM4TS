import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    // Create a SourceFile for testing
    SourceFile sourceFile = new SourceFile("test.js", "alert('Hello, world!');");
    
    // Create a JsAst instance with the SourceFile
    JsAst jsAst = new JsAst(sourceFile);
    
    // Create a dummy compiler for testing
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Test the getAstRoot method
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(1, astRoot.getChildCount());
    assertEquals(Token.SCRIPT, astRoot.getToken());
    assertEquals(Token.BLOCK, astRoot.getFirstChild().getToken());
    assertEquals(Token.CALL, astRoot.getFirstChild().getFirstChild().getToken());
    assertEquals("alert", astRoot.getFirstChild().getFirstChild().getFirstChild().getString());
    assertEquals("'Hello, world!'", astRoot.getFirstChild().getFirstChild().getLastChild().getString());
  }
  
  @Test
  public void testClearAst() {
    // Create a SourceFile for testing
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    
    // Create a JsAst instance with the SourceFile
    JsAst jsAst = new JsAst(sourceFile);
    
    // Create a dummy compiler for testing
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Test the clearAst method
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }
  
  @Test
  public void testGetInputId() {
    // Create a SourceFile for testing
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    
    // Create a JsAst instance with the SourceFile
    JsAst jsAst = new JsAst(sourceFile);
    
    // Test the getInputId method
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }
  
  @Test
  public void testGetSourceFile() {
    // Create a SourceFile for testing
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    
    // Create a JsAst instance with the SourceFile
    JsAst jsAst = new JsAst(sourceFile);
    
    // Test the getSourceFile method
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals("test.js", result.getName());
    assertEquals(sourceFile.getCode(), result.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    // Create a SourceFile for testing
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    
    // Create a JsAst instance with the SourceFile
    JsAst jsAst = new JsAst(sourceFile);
    
    // Create a new SourceFile
    SourceFile newSourceFile = new SourceFile("test.js", "alert('Hello, world!');");
    
    // Test the setSourceFile method
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}