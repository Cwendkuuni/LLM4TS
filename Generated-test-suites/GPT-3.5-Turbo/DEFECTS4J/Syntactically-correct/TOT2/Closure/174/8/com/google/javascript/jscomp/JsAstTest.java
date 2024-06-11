import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testConstructor() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals("test.js", jsAst.getInputId().getId());
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testGetAstRoot() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    AbstractCompiler compiler = new Compiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals(IR.block(IR.var(IR.name("x"), IR.number(5))), root);
  }

  @Test
  public void testClearAst() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFile() {
    // Error scenario
    SourceFile sourceFile1 = new SourceFile("test1.js", "var x = 5;");
    SourceFile sourceFile2 = new SourceFile("test2.js", "var y = 10;");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
  }

  @Test
  public void testParse() {
    // Edge case: IOException during parsing
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    compiler.reportWarning = true;
    assertNull(jsAst.getAstRoot(compiler));
  }
}