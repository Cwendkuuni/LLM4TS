import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));

    // Edge case: Clearing an already empty AST
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getId());
  }

  @Test
  public void testGetSourceFile() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals(sourceFile, result);

    // Edge case: Set source file after initialization
    SourceFile newSourceFile = new SourceFile("new_test.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals(newSourceFile, result);
  }
}