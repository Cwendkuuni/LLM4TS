import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    // Test case to check if the AST root node is obtained correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
  
  @Test
  public void testClearAst() {
    // Test case to check if the AST is cleared correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }
  
  @Test
  public void testGetInputId() {
    // Test case to check if the input ID is obtained correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }
  
  @Test
  public void testGetSourceFile() {
    // Test case to check if the source file is obtained correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertEquals(sourceFile, result);
  }
  
  @Test
  public void testSetSourceFile() {
    // Test case to check if the source file is set correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new_test.js");
    jsAst.setSourceFile(newSourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertEquals(newSourceFile, result);
  }
  
  @Test
  public void testParse() {
    // Test case to check if the parsing of the source file is done correctly
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertFalse(compiler.hasHaltingErrors());
  }
  
}