import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  // Test case 1
  @Test
  public void testGetAstRoot() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Create a new instance of AbstractCompiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Call the getAstRoot method and assert the result
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  // Test case 2
  @Test
  public void testClearAst() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Call the clearAst method
    jsAst.clearAst();

    // Assert that the ast is cleared
    assertNull(jsAst.getAstRoot(null));
  }

  // Test case 3
  @Test
  public void testGetInputId() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Call the getInputId method and assert the result
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.toString());
  }

  // Test case 4
  @Test
  public void testGetSourceFile() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Call the getSourceFile method and assert the result
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals("test.js", result.getName());
  }

  // Test case 5
  @Test
  public void testSetSourceFile() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Create a new SourceFile object
    SourceFile newSourceFile = new SourceFile("newTest.js");

    // Call the setSourceFile method
    jsAst.setSourceFile(newSourceFile);

    // Call the getSourceFile method and assert the result
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals("newTest.js", result.getName());
  }

  // Test case 6
  @Test
  public void testParse() {
    // Create a new instance of JsAst
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Create a new instance of AbstractCompiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Call the parse method
    jsAst.parse(compiler);

    // Assert that the root node is not null
    assertNotNull(jsAst.getAstRoot(null));
  }
}