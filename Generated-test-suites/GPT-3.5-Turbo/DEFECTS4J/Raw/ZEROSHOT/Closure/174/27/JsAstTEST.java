import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    jsAst = new JsAst(new SourceFile("test.js", "var x = 5;"));
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
    assertEquals("test.js", sourceFile.getName());
    assertEquals("var x = 5;", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("test2.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

}