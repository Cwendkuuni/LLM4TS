import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testClearAst() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    // Typical use case
    SourceFile sourceFile1 = new SourceFile("test1.js", "var x = 10;");
    SourceFile sourceFile2 = new SourceFile("test2.js", "var y = 20;");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

}