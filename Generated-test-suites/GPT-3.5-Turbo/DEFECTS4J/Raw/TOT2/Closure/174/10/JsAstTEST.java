import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;

public class JsAstTest {
  
  private JsAst jsAst;
  private Compiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", "function test() {}");
    jsAst = new JsAst(sourceFile);
    compiler = new Compiler();
  }

  @Test
  public void testGetAstRoot() {
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testInputId() {
    assertEquals("test.js", jsAst.getInputId().id);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newFile = new SourceFile("new_test.js", "function newTest() {}");
    jsAst.setSourceFile(newFile);
    assertEquals("new_test.js", jsAst.getInputId().id);
  }

  @Test
  public void testParseWithIOException() {
    SourceFile invalidFile = new SourceFile("invalid.js", "invalid code");
    JsAst invalidAst = new JsAst(invalidFile);
    CompilerOptions options = new CompilerOptions();
    assertTrue(compiler.hasHaltingErrors());
  }

  @Test
  public void testParseWithError() {
    SourceFile errorFile = new SourceFile("error.js", "function test() {");
    JsAst errorAst = new JsAst(errorFile);
    Node root = errorAst.getAstRoot(compiler);
    assertEquals(1, compiler.getErrors().size());
    assertEquals(JSError.make(AbstractCompiler.PARSE_ERROR, "error.js"), compiler.getErrors().get(0));
  }
  
}