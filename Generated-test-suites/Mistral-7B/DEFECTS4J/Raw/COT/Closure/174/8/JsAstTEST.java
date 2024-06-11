import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = mock(SourceFile.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    JsAst jsAst = new JsAst(sourceFile);
    Node result = jsAst.getAstRoot(compiler);

    assertNotNull(result);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(null);
    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(null));
  }

  @Test(expected = NullPointerException.class)
  public void testGetInputIdNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.getInputId();
  }

  @Test
  public void testGetSourceFileNullAstRoot() {
    SourceFile sourceFile = mock(SourceFile.class);
    JsAst jsAst = new JsAst(sourceFile);

    assertNotNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFileSameFileName() {
    SourceFile sourceFile1 = mock(SourceFile.class);
    SourceFile sourceFile2 = mock(SourceFile.class);
    Preconditions.checkState(sourceFile1.getName().equals(sourceFile2.getName()));

    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);

    assertSame(sourceFile2, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFileDifferentFileName() {
    SourceFile sourceFile1 = mock(SourceFile.class);
    SourceFile sourceFile2 = mock(SourceFile.class);
    Preconditions.checkState(!sourceFile1.getName().equals(sourceFile2.getName()));

    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);

    assertSame(sourceFile2, jsAst.getSourceFile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentFileName() {
    SourceFile sourceFile1 = mock(SourceFile.class);
    SourceFile sourceFile2 = mock(SourceFile.class);
    Preconditions.checkState(!sourceFile1.getName().equals(sourceFile2.getName()));

    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
  }

  @Test
  public void testParseSuccess() {
    SourceFile sourceFile = mock(SourceFile.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseIOException() {
    SourceFile sourceFile = mock(SourceFile.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    JsAst jsAst = new JsAst(sourceFile);

    compiler.report(JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));

    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseNullAst() {
    SourceFile sourceFile = mock(SourceFile.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    if (compiler.hasHaltingErrors()) {
      Node result = IR.script();
      jsAst.setSourceFile(sourceFile);
      assertSame(result, jsAst.getAstRoot(compiler));
    } else {
      Node result = jsAst.getAstRoot(compiler);
      assertNotNull(result);
    }
  }
}