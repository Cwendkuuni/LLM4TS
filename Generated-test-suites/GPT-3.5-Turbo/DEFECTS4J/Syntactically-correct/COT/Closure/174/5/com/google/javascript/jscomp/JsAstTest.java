import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot() {
    // Basic test to check if the AST root is returned correctly
    assertNotNull(jsAst.getAstRoot(null));
  }
  
  @Test
  public void testClearAst() {
    // Basic test to check if the AST is cleared correctly
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }
  
  @Test
  public void testGetInputId() {
    // Basic test to check if the input ID is returned correctly
    assertNotNull(jsAst.getInputId());
  }
  
  @Test
  public void testGetSourceFile() {
    // Basic test to check if the source file is returned correctly
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }
  
  @Test
  public void testSetSourceFile() {
    // Basic test to check if the source file is set correctly
    SourceFile sourceFile = new SourceFile("newtest.js", "var y = 20;");
    jsAst.setSourceFile(sourceFile);
    assertEquals("newtest.js", jsAst.getSourceFile().getName());
  }
}