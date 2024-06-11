import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
    assertTrue(astRoot.isScript());
    assertNotNull(astRoot.getFirstChild());
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);

    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();

    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile file = jsAst.getSourceFile();

    assertNotNull(file);
    assertEquals("test.js", file.getName());
    assertEquals("var x = 5;", file.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile newSourceFile = new SourceFile("test.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);

    SourceFile file = jsAst.getSourceFile();

    assertNotNull(file);
    assertEquals("test.js", file.getName());
    assertEquals("var y = 10;", file.getCode());
  }
}