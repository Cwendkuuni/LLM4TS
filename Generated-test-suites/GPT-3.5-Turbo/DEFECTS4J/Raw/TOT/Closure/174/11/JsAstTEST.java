import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.SourceFile;

public class JsAstTest {

  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js",
        "function foo() { return 5; }");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals("script", root.getToken().toString());
    assertEquals(1, root.getChildCount());
    assertEquals("function", root.getFirstChild().getToken().toString());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot());
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assertEquals("test.js", sourceFile.getName());
    assertEquals("function foo() { return 5; }", sourceFile.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newtest.js",
        "function bar() { return 10; }");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newtest.js", jsAst.getSourceFile().getName());
    assertEquals("function bar() { return 10; }", jsAst.getSourceFile().getCode());
  }
}