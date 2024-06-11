import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class JsAstTest {

  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    // Verify that the AST root is not null
    Node root = jsAst.getAstRoot(new AbstractCompiler());
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    // Verify that clearing the AST sets the root to null
    jsAst.clearAst();
    Node root = jsAst.getAstRoot(new AbstractCompiler());
    assertNull(root);
  }

  @Test
  public void testGetInputId() {
    // Verify that the input ID is not null
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    // Verify that the source file is not null
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    // Verify that setting the source file updates the file name
    SourceFile newSourceFile = new SourceFile("newTest.js", "console.log('Hello, new world!');");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("newTest.js", sourceFile.getName());
  }

  @Test
  public void testParse() {
    // Verify that parsing the source file sets the AST root correctly
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testParseErrorHandling() {
    // Verify that parse errors are handled correctly
    SourceFile errorSourceFile = new SourceFile("error.js", "console.log('Hello, world!");
    jsAst = new JsAst(errorSourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

}