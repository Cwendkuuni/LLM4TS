import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Create a new JsAst instance
    SourceFile sourceFile = new SourceFile();
    JsAst jsAst = new JsAst(sourceFile);

    // Create a new AbstractCompiler instance
    AbstractCompiler compiler = new AbstractCompiler();

    // Test the getAstRoot method
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    // Create a new SourceFile instance
    SourceFile sourceFile = new SourceFile();

    // Create a new JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Test the clearAst method
    jsAst.clearAst();

    // Check if the astRoot is null
    assertNull(jsAst.getAstRoot());
  }

  @Test
  public void testGetInputId() {
    // Create a new SourceFile instance
    SourceFile sourceFile = new SourceFile();

    // Create a new JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Test the getInputId method
    InputId inputId = jsAst.getInputId();

    // Check if the inputId is not null
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    // Create a new SourceFile instance
    SourceFile sourceFile = new SourceFile();

    // Create a new JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Test the getSourceFile method
    SourceFile result = jsAst.getSourceFile();

    // Check if the result is equal to the sourceFile
    assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
    // Create a new SourceFile instance
    SourceFile sourceFile = new SourceFile();

    // Create a new JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Create a new SourceFile instance
    SourceFile newSourceFile = new SourceFile();

    // Test the setSourceFile method
    jsAst.setSourceFile(newSourceFile);

    // Test the getSourceFile method
    SourceFile result = jsAst.getSourceFile();

    // Check if the result is equal to the newSourceFile
    assertEquals(newSourceFile, result);
  }

}