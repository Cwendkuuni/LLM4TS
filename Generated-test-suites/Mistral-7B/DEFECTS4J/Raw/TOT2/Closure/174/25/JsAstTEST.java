// Combined JUnit 4 test file (JsAstTest.java) for the given Java class.

import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import org.junit.Test;

public class JsAstTest {
  @Test
  public void testJsAst() {
    // given
    SourceFile sourceFile = new SourceFile("test.js", "test code");

    // when
    JsAst jsAst = new JsAst(sourceFile);

    // then
    assertNotNull(jsAst);
  }

  @Test
  public void testGetAstRoot_WithNullCompiler() {
    // given
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    // when
    Node root = jsAst.getAstRoot(null);

    // then
    assertNotNull(root);
  }

  @Test
  public void testGetAstRoot_WithParseError() {
    // given
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    // when
    compiler.report(JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));

    // then
    Node root = jsAst.getAstRoot(compiler);

    // ensure that a dummy block is used as the parse tree
    assertEquals(IR.script(), root);
  }

  @Test
  public void testGetAstRoot_WithIOException() throws IOException {
    // given
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    // when
    throw new IOException();

    // then
    Node root = jsAst.getAstRoot(compiler);

    // ensure that a dummy block is used as the parse tree
    assertEquals(IR.script(), root);
  }

  @Test
  public void testClearAst() {
    // given
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    // when
    jsAst.clearAst();

    // then
    assertNull(jsAst.getAstRoot(null));
  }

  @Test(expected = NullPointerException.class)
  public void testJsAst_WithNullSourceFile() {
    // given

    // when
    // then exception is thrown
    new JsAst(null);
  }
}

//