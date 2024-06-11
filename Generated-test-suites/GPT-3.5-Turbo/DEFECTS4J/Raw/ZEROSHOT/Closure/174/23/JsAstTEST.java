import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsAstTest {
  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.root(IR.var(IR.name("x"), IR.number(5))), astRoot);
  }

  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("test.js", sourceFile.getName());
    assertEquals("var x = 5;", sourceFile.getCode());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newTest.js", jsAst.getSourceFile().getName());
    assertEquals("var y = 10;", jsAst.getSourceFile().getCode());
  }
}