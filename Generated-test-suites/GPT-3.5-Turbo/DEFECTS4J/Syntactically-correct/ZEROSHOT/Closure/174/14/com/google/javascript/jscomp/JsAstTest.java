import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {
  private JsAst jsAst;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", "var x = 5;");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(astRoot.getType(), Token.SCRIPT);
    assertNull(astRoot.getFirstChild());
    assertNull(astRoot.getLastChild());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(astRoot, IR.script());
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertEquals(inputId.getIdName(), "test.js");
  }

  @Test
  public void testGetSourceFile() {
    SourceFile file = jsAst.getSourceFile();
    assertEquals(file.getName(), "test.js");
    assertEquals(file.getCode(), "var x = 5;");
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newFile = new SourceFile("test2.js", "let y = 10;");
    jsAst.setSourceFile(newFile);
    assertEquals(jsAst.getSourceFile().getName(), "test2.js");
    assertEquals(jsAst.getSourceFile().getCode(), "let y = 10;");
  }
}