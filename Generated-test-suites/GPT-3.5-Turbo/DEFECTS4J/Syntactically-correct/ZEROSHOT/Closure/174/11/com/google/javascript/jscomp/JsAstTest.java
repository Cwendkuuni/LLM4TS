import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
    assertEquals("SCRIPT", astRoot.getToken().toString());
    assertEquals(1, astRoot.getChildCount());
    assertEquals("VAR", astRoot.getFirstChild().getToken().toString());
    assertEquals("x", astRoot.getFirstChild().getFirstChild().getString());
    assertEquals("10", astRoot.getFirstChild().getFirstChild().getNext().getString());
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);

    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();

    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile result = jsAst.getSourceFile();

    assertNotNull(result);
    assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new.js", "var y = 20;");

    jsAst.setSourceFile(newSourceFile);

    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}