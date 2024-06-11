import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.SourceFile;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getInputId());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    SourceFile newSourceFile = new SourceFile("new_test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
  
}