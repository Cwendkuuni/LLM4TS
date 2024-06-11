

import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.ParseResultProvider;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

  private StaticSourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    sourceFile = new StaticSourceFile("test.js", "function add(a, b) { return a + b; }");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = jsAst.getAstRoot(compiler);
    assertEquals("script", rootNode.getType());
    assertEquals(sourceFile, rootNode.getStaticSourceFile());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    AbstractCompiler compiler = new Compiler();
    Node rootNode = jsAst.getAstRoot(compiler);
    assertEquals("script", rootNode.getType());
    assertEquals(sourceFile, rootNode.getStaticSourceFile());
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    assertEquals(sourceFile, returnedSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new StaticSourceFile("newTest.js", "function subtract(a, b) { return a - b; }");
    jsAst.setSourceFile(newSourceFile);
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    assertEquals(newSourceFile, returnedSourceFile);
  }
}

