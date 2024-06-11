import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test source code");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test source code");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertEquals(null, jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test source code");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile.getName(), jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test source code");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test source code 1");
    JsAst jsAst = new JsAst(new SourceFile("test.js", "test source code"));
    jsAst.setSourceFile(sourceFile1);
    assertEquals(sourceFile1, jsAst.getSourceFile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentNames() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test source code 1");
    JsAst jsAst = new JsAst(new SourceFile("test.js", "test source code"));
    jsAst.setSourceFile(sourceFile1);
  }

  @Test
  public void testParseSuccess() {
    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js", code);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseFailure() {
    String code = "function test() {\n" +
        "  }"; // missing return statement
    SourceFile sourceFile = new SourceFile("test.js", code);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test source code");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    throw new IOException("IOException message");
  }
}