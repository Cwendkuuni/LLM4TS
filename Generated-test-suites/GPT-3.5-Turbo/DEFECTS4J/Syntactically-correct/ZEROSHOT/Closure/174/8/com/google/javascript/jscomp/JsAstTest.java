import org.junit.*;
import java.io.IOException;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node root = jsAst.getAstRoot(compiler);
    Assert.assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node root = jsAst.getAstRoot(compiler);
    Assert.assertNotNull(root);
    jsAst.clearAst();
    Node newRoot = jsAst.getAstRoot(compiler);
    Assert.assertNull(newRoot);
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    Assert.assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    Assert.assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newtest.js", "console.log('New Hello!');");
    jsAst.setSourceFile(newSourceFile);
    SourceFile result = jsAst.getSourceFile();
    Assert.assertEquals(newSourceFile, result);
  }

}