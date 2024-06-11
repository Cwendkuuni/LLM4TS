import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertEquals(null, jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("test2.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("test2.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.clearAst();
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }
}