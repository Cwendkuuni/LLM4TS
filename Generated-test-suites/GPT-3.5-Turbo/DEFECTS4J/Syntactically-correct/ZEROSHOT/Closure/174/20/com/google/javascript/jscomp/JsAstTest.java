import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {
  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, World!');");
    jsAst = new JsAst(sourceFile);
  }

  @After
  public void tearDown() {
    jsAst = null;
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertTrue(root.isScript());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
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
    assertEquals("console.log('Hello, World!');", sourceFile.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("new_test.js", "console.log('New Test');");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}