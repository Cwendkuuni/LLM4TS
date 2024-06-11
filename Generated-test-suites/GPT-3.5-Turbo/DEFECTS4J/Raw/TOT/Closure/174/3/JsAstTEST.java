import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.SourceFile;

public class JsAstTest {
  private JsAst jsAst;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", "var x = 10;");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    assertNotNull(jsAst.getAstRoot());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot());
  }

  @Test
  public void testGetInputId() {
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "let y = 20;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}