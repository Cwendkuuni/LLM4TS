import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  private JsAst jsAst;
  
  @Before
  public void setUp() {
    jsAst = new JsAst(new SourceFile("test.js"));
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
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newtest.js");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newtest.js", jsAst.getSourceFile().getName());
  }
  
  @Test
  public void testParse() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot.isScript());
  }
  
  @Test
  public void testParseWithIOException() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.setSourceFile(new SourceFile("nonexistent.js"));
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot.isScript());
    // Assert the read error was reported
    assertTrue(compiler.getErrors().hasErrors());
  }
  
  @Test
  public void testParseWithParseError() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.setSourceFile(new SourceFile("syntax_error.js"));
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot.isScript());
    // Assert the parse error was reported
    assertTrue(compiler.getErrors().hasErrors());
  }
  
}