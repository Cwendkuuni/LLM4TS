import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class JsAstTest {
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    
    assertNotNull(astRoot);
    assertEquals(Token.SCRIPT, astRoot.getToken());
    assertNotNull(astRoot.getFirstChild());
    assertEquals(Token.VAR, astRoot.getFirstChild().getToken());
    assertNotNull(astRoot.getFirstChild().getFirstChild());
    assertEquals(Token.NAME, astRoot.getFirstChild().getFirstChild().getToken());
    assertEquals("x", astRoot.getFirstChild().getFirstChild().getString());
    assertNotNull(astRoot.getFirstChild().getFirstChild().getNext());
    assertEquals(Token.NUMBER, astRoot.getFirstChild().getFirstChild().getNext().getToken());
    assertEquals("10", astRoot.getFirstChild().getFirstChild().getNext().getString());
  }
  
  @Test
  public void testClearAst() {
    jsAst.clearAst();
    
    Node astRoot = jsAst.getAstRoot(new Compiler());
    assertEquals(Token.SCRIPT, astRoot.getToken());
    assertNull(astRoot.getFirstChild());
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
    assertEquals("var x = 10;", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 20;");
    jsAst.setSourceFile(newSourceFile);
    
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assertEquals("newTest.js", sourceFile.getName());
    assertEquals("var y = 20;", sourceFile.getCode());
  }
  
  @Test
  public void testParse() {
    Compiler compiler = new Compiler();
    jsAst.parse(compiler);
    
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
    assertEquals(Token.SCRIPT, astRoot.getToken());
  }
}