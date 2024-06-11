import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new DefaultCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);

    Assert.assertNotNull(astRoot);
    Assert.assertEquals("SCRIPT", astRoot.getType().toString());
    Assert.assertEquals("var x = 5;", astRoot.getFirstChild().toString());
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    jsAst.clearAst();

    Assert.assertNull(jsAst.getAstRoot());
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();

    Assert.assertNotNull(inputId);
    Assert.assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile result = jsAst.getSourceFile();

    Assert.assertNotNull(result);
    Assert.assertEquals("test.js", result.getName());
    Assert.assertEquals("var x = 5;", result.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile newSourceFile = new SourceFile("newtest.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);

    SourceFile result = jsAst.getSourceFile();

    Assert.assertNotNull(result);
    Assert.assertEquals("newtest.js", result.getName());
    Assert.assertEquals("var y = 10;", result.getCode());
  }
}