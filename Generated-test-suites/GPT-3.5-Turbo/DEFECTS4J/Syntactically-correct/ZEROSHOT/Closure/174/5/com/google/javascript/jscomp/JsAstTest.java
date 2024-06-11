import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    jsAst = new JsAst(new SourceFile("fileName.js", "sourceCode"));
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
    Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
    assertNull(astRoot);
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
    SourceFile newSourceFile = new SourceFile("newFileName.js", "newSourceCode");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
}