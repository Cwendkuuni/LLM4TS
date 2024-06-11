import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
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
    SourceFile sf = jsAst.getSourceFile();
    assertNotNull(sf);
    assertEquals("test.js", sf.getName());
    assertEquals("var x = 5;", sf.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("new.js", jsAst.getSourceFile().getName());
    assertEquals("var y = 10;", jsAst.getSourceFile().getCode());
  }

  // Additional test cases for potential edge cases and exception handling

  @Test
  public void testParseWithEmptySourceFile() {
    SourceFile sourceFile = new SourceFile("empty.js", "");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals("script", root.getToken().getName());
  }

  @Test(expected = IOException.class)
  public void testParseWithInvalidSourceFile() throws IOException {
    SourceFile sourceFile = new SourceFile("invalid.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
  }

  @Test
  public void testParseWithParseError() {
    SourceFile sourceFile = new SourceFile("error.js", "var x = ;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    assertTrue(compiler.hasErrors());
  }

}