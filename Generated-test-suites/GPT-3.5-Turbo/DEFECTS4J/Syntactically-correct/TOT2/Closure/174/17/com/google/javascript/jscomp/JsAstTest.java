import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  // Test case for JsAst constructor
  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
    assertEquals("test.js", jsAst.fileName);
  }

  // Test case for getAstRoot method
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(new AbstractCompiler());
    assertNotNull(root);
    assertEquals("SCRIPT", root.getToken());
  }

  // Test case for clearAst method
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  // Test case for getInputId method
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  // Test case for getSourceFile method
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile returnedFile = jsAst.getSourceFile();
    assertEquals(sourceFile, returnedFile);
  }

  // Test case for setSourceFile method
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    SourceFile newFile = new SourceFile("newTest.js", "var y = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.setSourceFile(newFile);
    assertEquals(newFile, jsAst.getSourceFile());
  }
}