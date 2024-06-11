import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  private JsAst jsAst;
  
  @Before
  public void setUp() {
    jsAst = new JsAst(new SourceFile("test.js", "function add(a, b){ return a + b; }"));
  }
  
  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
    
    assertNotNull(astRoot);
    assertEquals("SCRIPT", astRoot.getToken().toString());
    assertNotNull(astRoot.getFirstChild());
    assertEquals("FUNCTION", astRoot.getFirstChild().getToken().toString());
    assertNotNull(astRoot.getFirstChild().getFirstChild());
    assertEquals("NAME", astRoot.getFirstChild().getFirstChild().getToken().toString());
    assertEquals("add", astRoot.getFirstChild().getFirstChild().getString());
  }
  
  @Test
  public void testClearAst() {
    jsAst.getAstRoot(new AbstractCompiler());
    
    assertNotNull(jsAst.getAstRoot());
    
    jsAst.clearAst();
    
    assertNull(jsAst.getAstRoot());
  }
  
  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
    assertEquals("test.js", inputId.toString());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    
    assertNotNull(sourceFile);
    assertEquals("test.js", sourceFile.getName());
    assertEquals("function add(a, b){ return a + b; }", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newtest.js", "function multiply(a, b){ return a * b; }");
    
    jsAst.setSourceFile(newSourceFile);
    
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}