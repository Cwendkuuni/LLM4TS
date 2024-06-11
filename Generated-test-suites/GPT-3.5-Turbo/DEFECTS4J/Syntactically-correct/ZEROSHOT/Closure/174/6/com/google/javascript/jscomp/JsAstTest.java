import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.*;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test.js");
    SourceFile sourceFile2 = new SourceFile("test2.js");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    SourceFile result = jsAst.getSourceFile();
    assertEquals(sourceFile2, result);
  }
  
  @Test
  public void testParse() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

}