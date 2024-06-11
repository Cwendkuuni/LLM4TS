import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeFactory.code;
import static com.google.javascript.jscomp.testing.TypeChecktesting.checkTypeErrors;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.testing.JsCompilerTest;
import com.google.javascript.jscomp.testing.TypeCheckTestingCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest extends JsCompilerTest {

  private JsAst jsAst;
  private SourceFile sourceFile;
  private Node root;

  @Before
  public void setUp() throws Exception {
    sourceFile = new SourceFile("Test.js", code("function test() {}"));
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new TypeCheckTestingCompiler(new CompilerOptions());
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(Node.SCRIPT, astRoot.getType());
  }

  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new TypeCheckTestingCompiler(new CompilerOptions());
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node clearedAstRoot = jsAst.getAstRoot(compiler);
    assertNull(clearedAstRoot);
  }

  @Test
  public void testGetInputId() {
    assertEquals("Test.js", jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("Test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("NewTest.js", code("function newTest() {}"));
    jsAst.setSourceFile(newSourceFile);
    assertEquals("NewTest.js", jsAst.getInputId().getName());
  }

  @Test
  public void testParse() throws IOException {
    AbstractCompiler compiler = new TypeCheckTestingCompiler(new CompilerOptions());
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(Node.SCRIPT, astRoot.getType());
  }

  @Test
  public void testParseWithErrors() throws IOException {
    AbstractCompiler compiler = new TypeCheckTestingCompiler(new CompilerOptions());
    sourceFile = new SourceFile("TestWithErrors.js", code("function test() { x: 1; }"));
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    // There should be a type error for 'x'.
    checkTypeErrors(compiler, jsAst.getInputId(), 1);
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() throws IOException {
    AbstractCompiler compiler = new TypeCheckTestingCompiler(new CompilerOptions());
    sourceFile = new SourceFile("TestWithIOException.js", code(""));
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  @Test
  public void testSetSourceFileWithInvalidFileName() {
    String invalidFileName = "";
    Preconditions.checkArgument(!invalidFileName.isEmpty(), "FileName cannot be empty.");
    jsAst.setSourceFile(new SourceFile(invalidFileName, code("")));
    fail("Expected IllegalArgumentException.");
  }
}