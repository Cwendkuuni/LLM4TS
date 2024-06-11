import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}