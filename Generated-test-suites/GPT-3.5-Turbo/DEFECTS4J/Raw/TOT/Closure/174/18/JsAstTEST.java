import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  private JsAst jsAst;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    jsAst = new JsAst(sourceFile);
    compiler = new AbstractCompiler();
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals("SCRIPT", astRoot.getToken().name());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newtest.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newtest.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testParse() {
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals("SCRIPT", astRoot.getToken().name());
  }

}