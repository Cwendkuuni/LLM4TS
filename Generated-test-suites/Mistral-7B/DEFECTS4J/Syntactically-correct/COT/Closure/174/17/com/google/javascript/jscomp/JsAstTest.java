import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    assertEquals("test.js", jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newFile.js", "new file content");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newFile.js", jsAst.getSourceFile().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentFileName() {
    SourceFile newSourceFile = new SourceFile("test.js", "new file content");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullCompiler() {
    jsAst.parse(null);
  }

  @Test
  public void testParse() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithHaltingErrors() {
    compiler.setHaltingErrors(true);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    compiler.setHaltingErrors(false);
    try {
      jsAst.parse(new AbstractCompiler() {
        @Override
        public void report(JSError error) {
          throw new IOException("Simulated IO error");
        }
      });
    } catch (IOException e) {
      // Expected
    }
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseEmptyFile() {
    sourceFile.setCode("");
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot);
  }

  @Test
  public void testParseWithAst() {
    Node root = IR.script(
        "function test() { return 1; }",
        "test()"
    );
    Node expectedAST = IR.program(
        IR.functionDeclaration("test", null, null, IR.block(
            IR.returnStatement(IR.intLiteral(1))
        ))
    );
    jsAst.parse(compiler);
    jsAst.setSourceFile(sourceFile);
    jsAst.root = root;
    Node actualAST = jsAst.getAstRoot(compiler);
    assertEquals(expectedAST, actualAST);
  }
}