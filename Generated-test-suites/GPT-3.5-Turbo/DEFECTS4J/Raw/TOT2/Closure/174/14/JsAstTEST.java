import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    
    Node astRoot = jsAst.getAstRoot(compiler);
    
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(new Compiler());
    
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newFile = new SourceFile("newtest.js", "function newTest() {}");
    
    jsAst.setSourceFile(newFile);
    assertEquals("newtest.js", jsAst.getSourceFile().getName());
  }

  // Add more test cases for other methods in JsAst class as needed

}