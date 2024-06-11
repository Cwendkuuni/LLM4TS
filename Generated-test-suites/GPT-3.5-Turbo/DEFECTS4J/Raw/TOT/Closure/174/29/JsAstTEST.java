import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  private JsAst jsAst;
  private SourceFile sourceFile;
  
  @Before
  public void setup() {
    sourceFile = new SourceFile("test.js", "var a = 10;");
    jsAst = new JsAst(sourceFile);
  }
  
  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    
    assertNotNull(root);
    assertEquals(IR.block(IR.var(IR.name("a"), IR.number(10))), root);
  }
  
  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root1 = jsAst.getAstRoot(compiler);
    assertNotNull(root1);
    
    jsAst.clearAst();
    Node root2 = jsAst.getAstRoot(compiler);
    
    assertNotSame(root1, root2);
    assertNotNull(root2);
  }
  
  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getDisplayName());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    
    assertNotNull(sourceFile);
    assertEquals("test.js", sourceFile.getName());
    assertEquals("var a = 10;", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "var b = 20;");
    jsAst.setSourceFile(newSourceFile);
    
    SourceFile sourceFile = jsAst.getSourceFile();
    
    assertNotNull(sourceFile);
    assertEquals("newTest.js", sourceFile.getName());
    assertEquals("var b = 20;", sourceFile.getCode());
  }
  
  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    
    assertNotNull(root);
    assertEquals(IR.block(IR.var(IR.name("a"), IR.number(10))), root);
  }
}