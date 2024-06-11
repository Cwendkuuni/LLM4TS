import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.TestUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    sourceFile = new SourceFile("test.js", "test content");
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals(sourceFile.getName(), inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals(sourceFile, jsAst.sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newFile.js", "new content");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals(newSourceFile, sourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentName() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("differentFile.js", "different content");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParseNullCompiler() {
    JsAst jsAst = new JsAst(sourceFile);
    try {
      jsAst.parse(null);
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner parserRunner = ParserRunner.create();
    try {
      jsAst.parse(parserRunner);
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testParseValid() {
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner parserRunner = ParserRunner.create();
    jsAst.parse(parserRunner);
    Node root = jsAst.getAstRoot(parserRunner);
    assertNotNull(root);
  }

  @Test
  public void testParseWithHaltingErrors() {
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner parserRunner = new ParserRunner() {
      @Override
      public void reportError(int line, int lineOffset, String message) {
        // do nothing
      }
    };
    jsAst.parse(parserRunner);
    Node root = jsAst.getAstRoot(parserRunner);
    assertNotNull(root);
    assertEquals(IR.script(), root);
  }
}