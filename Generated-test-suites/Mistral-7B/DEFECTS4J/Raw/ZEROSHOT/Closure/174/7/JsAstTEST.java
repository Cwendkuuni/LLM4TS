import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    jsAst.clearAst();
    Node root = jsAst.getAstRoot(compiler);
    assertTrue(root == null);
  }

  @Test
  public void testGetInputId() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertTrue(inputId.getName().equals("Test.js"));
  }

  @Test
  public void testGetSourceFile() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertTrue(result == sourceFile);
  }

  @Test
  public void testSetSourceFile() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile oldSourceFile = new SourceFile("TestOld.js", "Test old source code");
    SourceFile newSourceFile = new SourceFile("TestNew.js", "Test new source code");
    JsAst jsAst = new JsAst(oldSourceFile);
    jsAst.setSourceFile(newSourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertTrue(result == newSourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileInvalid() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);
    Preconditions.checkArgument(false, "Invalid argument.");
    jsAst.setSourceFile(null);
  }

  @Test
  public void testParse() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    ParserConfig config = new ParserConfig();
    SourceFile sourceFile = new SourceFile("Test.js", "Test source code");
    JsAst jsAst = new JsAst(sourceFile);

    // First parse with no errors.
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    // Second parse with errors.
    compiler.reset();
    jsAst.clearAst();
    jsAst.parse(compiler);
    root = jsAst.getAstRoot(compiler);
    assertTrue(root == null);
  }
}