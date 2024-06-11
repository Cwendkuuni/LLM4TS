
import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();

    assertNotNull(inputId);
    assertEquals("test.js", inputId.getExternalId());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile returnedSourceFile = jsAst.getSourceFile();

    assertNotNull(returnedSourceFile);
    assertEquals(sourceFile, returnedSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new_test.js");

    jsAst.setSourceFile(newSourceFile);

    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}
