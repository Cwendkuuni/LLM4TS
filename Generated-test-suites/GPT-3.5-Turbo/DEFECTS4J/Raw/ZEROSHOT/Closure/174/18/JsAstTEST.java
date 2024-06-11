import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
  
  private JsAst jsAst;
  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", "function testFunc() { return true; }");
    jsAst = new JsAst(sourceFile);
    compiler = new AbstractCompiler();
  }
  
  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
  
  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }
  
  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("test.js", sourceFile.getName());
    assertEquals("function testFunc() { return true; }", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "console.log('Hello, World!');");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newTest.js", jsAst.getSourceFile().getName());
    assertEquals("console.log('Hello, World!');", jsAst.getSourceFile().getCode());
  }
  
  @Test
  public void testParse() {
    jsAst.clearAst();
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
    assertFalse(compiler.hasHaltingErrors());
  }

}