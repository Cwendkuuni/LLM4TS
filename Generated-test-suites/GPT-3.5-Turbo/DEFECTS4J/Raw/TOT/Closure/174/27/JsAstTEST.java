import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.InputId;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;

public class JsAstTest {

  // Test case for constructor
  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    assertEquals("test.js", jsAst.getSourceFile().getName());
    assertNull(jsAst.getInputId());
    assertNotNull(jsAst.getAstRoot(null));
    assertNotNull(jsAst.getSourceFile());
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }

  // Test case for getAstRoot when root is null
  @Test
  public void testGetAstRootWhenRootIsNull() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = jsAst.getAstRoot(compiler);

    assertEquals(sourceFile.getName(), rootNode.getStaticSourceFile().getName());
    assertNotNull(rootNode);
  }

  // Test case for getAstRoot when root is not null
  @Test
  public void testGetAstRootWhenRootIsNotNull() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = jsAst.getAstRoot(compiler);

    Node rootNode2 = jsAst.getAstRoot(compiler);

    assertSame(rootNode, rootNode2);
  }

  // Test case for clearAst
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = jsAst.getAstRoot(compiler);

    assertNotNull(rootNode);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
    assertNull(rootNode.getStaticSourceFile());
  }

  // Test case for clearCachedSource
  @Test
  public void testClearCachedSource() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = jsAst.getAstRoot(compiler);

    assertNotNull(rootNode);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
    assertNull(rootNode.getStaticSourceFile());
    jsAst.setSourceFile(sourceFile);

    SourceFile newSourceFile = new SourceFile("test.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  // Test case for getInputId
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();

    assertEquals(sourceFile.getName(), inputId.getId());
  }

  // Test case for setSourceFile
  @Test
  public void testSetSourceFile() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName, "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile newSourceFile = new SourceFile(fileName, "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  // Test case for parse when IOException is thrown
  @Test
  public void testParseWhenIOExceptionIsThrown() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    // Simulate an IOException
    try {
      jsAst.parse(compiler);
      fail("IOException is not thrown");
    } catch (Exception e) {
      assertTrue(e instanceof IOException);
    }

    // Check that error is reported
    JSError error = compiler.getErrors().get(0);
    assertEquals(AbstractCompiler.READ_ERROR, error.getType());
    assertEquals(sourceFile.getName(), error.sourceName);
  }

  // Test case for parse when root is null and has no halting errors
  @Test
  public void testParseWhenRootIsNullAndHasNoHaltingErrors() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    jsAst.parse(compiler);
    Node rootNode = jsAst.getAstRoot(compiler);

    assertNotNull(rootNode);
    assertTrue(compiler.getErrors().isEmpty());
  }

  // Test case for parse when root is null and has halting errors
  @Test
  public void testParseWhenRootIsNullAndHasHaltingErrors() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = ");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    jsAst.parse(compiler);
    Node rootNode = jsAst.getAstRoot(compiler);

    assertNull(rootNode);
    assertFalse(compiler.getErrors().isEmpty());
  }
}