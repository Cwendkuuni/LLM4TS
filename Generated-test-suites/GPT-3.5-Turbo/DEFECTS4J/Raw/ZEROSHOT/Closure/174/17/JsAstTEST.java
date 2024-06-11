import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
  private static final Logger logger_ = Logger.getLogger(JsAst.class.getName());
  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
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
    assertEquals("var x = 5;", sourceFile.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("test2.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }
}