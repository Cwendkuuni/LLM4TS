import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assert(sourceFile != sourceFile);
    assert(sourceFile.getName().equals("newTest.js"));
  }

  @Test
  public void testParse() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = AssertionError.class)
  public void testParseWithError() {
    compiler.setErrorReporter(new ErrorReporter() {
      @Override
      public void reportError(JSError error) {
        throw new AssertionError("Unexpected error: " + error.getMessage());
      }
    });
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  private static interface ErrorReporter {
    void reportError(JSError error);
  }
}