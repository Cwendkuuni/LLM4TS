import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Typical use case
    JsAst jsAst = new JsAst(new SourceFile("test.js", "console.log('Hello, World!');"));
    assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
    
    // Edge case: invalid source file
    JsAst jsAstInvalid = new JsAst(null);
    assertNull(jsAstInvalid.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testClearAst() {
    // Typical use case
    JsAst jsAst = new JsAst(new SourceFile("test.js", "console.log('Hello, World!');"));
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    // Typical use case
    JsAst jsAst = new JsAst(new SourceFile("test.js", "console.log('Hello, World!');"));
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, World!');");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, World!');");
    JsAst jsAst = new JsAst(new SourceFile("test2.js", "alert('Goodbye!');"));
    jsAst.setSourceFile(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());

    // Edge case: setting source file with different name
    SourceFile sourceFile2 = new SourceFile("test3.js", "document.write('Testing...');");
    assertThrows(IllegalStateException.class, () -> {
      jsAst.setSourceFile(sourceFile2);
    });
  }

}