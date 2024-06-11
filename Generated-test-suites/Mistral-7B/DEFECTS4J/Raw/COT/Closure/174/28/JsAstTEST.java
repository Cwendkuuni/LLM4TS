import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.SourceName;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private JsAst jsAst;
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "", new SourceName("test.js"));
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node expectedAst = IR.script();
    Node actualAst = jsAst.getAstRoot(compiler);
    assertEquals(expectedAst, actualAst);
  }

  @Test
  public void testClearAst() {
    Node initialAst = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node clearedAst = jsAst.getAstRoot(compiler);
    assertNull(clearedAst);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootWithNullCompiler() {
    jsAst.getAstRoot(null);
  }

  @Test
  public void testSetSourceFile() {
    String newFileName = "newFile.js";
    SourceFile newSourceFile = new SourceFile(newFileName, "", new SourceName(newFileName));
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newFileName, jsAst.getSourceFile().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileWithDifferentFileName() {
    String newFileName = "newFile.js";
    SourceFile sourceFile = new SourceFile("test.js", "", new SourceName("test.js"));
    jsAst.setSourceFile(new SourceFile("differentFile.js", "", new SourceName("differentFile.js")));
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() {
    jsAst.parse(compiler);
  }

  @Test
  public void testParseWithHaltingErrors() {
    compiler.setHaltingErrors(true);
    jsAst.parse(compiler);
    Node actualAst = jsAst.getAstRoot(compiler);
    Node expectedAst = IR.script();
    assertEquals(expectedAst, actualAst);
  }

  @Test
  public void testParseWithValidCode() {
    String validCode = "function test() {}";
    sourceFile.setCode(validCode);
    jsAst.parse(compiler);
    Node actualAst = jsAst.getAstRoot(compiler);
    Node expectedAst = IR.script().call(IR.funcDecl(IR.identifier("test"), IR.block()));
    assertEquals(expectedAst, actualAst);
  }
}