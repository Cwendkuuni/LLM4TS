import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    // Typical use case: Verify that the AST root is obtained correctly
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
  
  @Test
  public void testClearAst() {
    // Typical use case: Verify that the AST is cleared successfully
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot());
  }
  
  @Test
  public void testGetInputId() {
    // Typical use case: Verify that the correct input id is obtained
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("testFile.js", inputId.getIdName());
  }
  
  @Test
  public void testGetSourceFile() {
    // Typical use case: Verify that the correct source file is obtained
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile obtainedSourceFile = jsAst.getSourceFile();
    assertNotNull(obtainedSourceFile);
    assertEquals("testFile.js", obtainedSourceFile.getName());
  }
  
  @Test
  public void testSetSourceFile() {
    // Typical use case: Verify that the source file is set correctly
    SourceFile sourceFile = new SourceFile("testFile.js");
    JsAst jsAst = new JsAst(null);
    jsAst.setSourceFile(sourceFile);
    assertEquals("testFile.js", jsAst.getSourceFile().getName());
  }
  
  @Test
  public void testParse() {
    // Edge case: Verify that parsing an empty source file results in a dummy block AST
    SourceFile sourceFile = new SourceFile("emptyFile.js", "");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot.isScript());
    assertTrue(astRoot.hasChildren());
  }

}