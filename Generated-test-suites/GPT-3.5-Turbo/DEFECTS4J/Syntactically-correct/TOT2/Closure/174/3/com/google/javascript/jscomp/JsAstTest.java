import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  @Test
  public void testConstructor() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    assertNotNull(jsAst);
  }

  @Test
  public void testGetAstRoot() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
    
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    
    assertNull(jsAst.getRoot());
  }

  @Test
  public void testGetInputId() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    
    assertEquals("test.js", returnedSourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    // Typical use case
    SourceFile sourceFile1 = new SourceFile("test1.js", "function test() {}");
    SourceFile sourceFile2 = new SourceFile("test2.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile1);
    
    jsAst.setSourceFile(sourceFile2);
    
    assertEquals("test2.js", jsAst.getSourceFile().getName());
  }

}