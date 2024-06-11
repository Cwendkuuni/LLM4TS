import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsAstTest {

  private static final String FILE_NAME = "test.js";
  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile(FILE_NAME, "");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot_withValidSourceFile_returnsAstRoot() {
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
  }

  @Test
  public void testGetAstRoot_withEmptySourceFile_returnsDummyBlockAstRoot() {
    AbstractCompiler compiler = new Compiler();
    jsAst.setSourceFile(SourceFile.fromCode(FILE_NAME, ""));
    Node astRoot = jsAst.getAstRoot(compiler);

    assertEquals(Node.Type.SCRIPT, astRoot.getType());
    assertTrue(astRoot.hasChildren());
    assertNull(astRoot.getFirstChild());
  }

  @Test
  public void testGetAstRoot_withInvalidSourceFile_reportsReadError() {
    AbstractCompiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile(FILE_NAME, "invalid code");
    jsAst.setSourceFile(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);

    assertEquals(Node.Type.SCRIPT, astRoot.getType());
    assertTrue(compiler.hasHaltingErrors());
  }

  @Test
  public void testClearAst_resetsAstRootToNull() {
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);

    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId_returnsInputId() {
    InputId inputId = jsAst.getInputId();

    assertNotNull(inputId);
    assertEquals(FILE_NAME, inputId.getIdName());
  }

  @Test
  public void testGetSourceFile_returnsSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();

    assertNotNull(sourceFile);
    assertEquals(FILE_NAME, sourceFile.getName());
  }

  @Test
  public void testSetSourceFile_updatesSourceFile() {
    SourceFile newSourceFile = new SourceFile(FILE_NAME, "updated code");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();

    assertNotNull(sourceFile);
    assertEquals("updated code", sourceFile.getCode());
  }
}