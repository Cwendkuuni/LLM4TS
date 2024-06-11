import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  private JsAst jsAst;

  @Before
  public void setUp() {
    // Create a temporary source file for testing
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
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
    SourceFile newFile = new SourceFile("new.js", "var y = 10;");
    jsAst.setSourceFile(newFile);
    
    assertEquals("new.js", jsAst.getSourceFile().getName());
    assertEquals("var y = 10;", jsAst.getSourceFile().getCode());
  }

  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    
    assertNotNull(jsAst.getAstRoot(compiler));
  }
}