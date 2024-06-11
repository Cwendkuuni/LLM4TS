import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testJsAst_GetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testJsAst_ClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot == null);
  }

  @Test
  public void testJsAst_GetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertTrue(inputId.getName().equals("test.js"));
  }

  @Test
  public void testJsAst_GetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile getSourceFile = jsAst.getSourceFile();
    assertTrue(getSourceFile == sourceFile);
  }

  @Test
  public void testJsAst_SetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test1.js", "test1.js");
    JsAst jsAst = new JsAst(sourceFile1);
    SourceFile sourceFile2 = new SourceFile("test2.js", "test2.js", "test2.js");
    jsAst.setSourceFile(sourceFile2);
    SourceFile getSourceFile = jsAst.getSourceFile();
    assertTrue(getSourceFile == sourceFile2);
  }

  @Test
  public void testJsAst_Parse() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    ParserConfig parserConfig = new ParserConfig();
    parserConfig.setAllowReserved(true);
    jsAst.parse(compiler);

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testJsAst_Parse_NullSourceFile() {
    JsAst jsAst = new JsAst(null);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
  }
}