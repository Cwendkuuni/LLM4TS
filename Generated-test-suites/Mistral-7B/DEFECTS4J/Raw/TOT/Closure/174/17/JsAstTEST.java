import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testJsAstCreation() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
    assertEquals("test.js", jsAst.getInputId().getName());
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testParseSuccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertTrue(root instanceof com.google.javascript.rhino.Script);
    assertEquals("test.js", root.getStaticSourceFile().getName());
  }

  @Test
  public void testParseFailure() {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("test.js", "function test() { while (true) {}");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals(new SourceName("test.js"), inputId.getSource());
  }

  @Test
  public void testGetSourceFile() {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFileFromJsAst = jsAst.getSourceFile();
    assertEquals(sourceFile, sourceFileFromJsAst);
  }
}