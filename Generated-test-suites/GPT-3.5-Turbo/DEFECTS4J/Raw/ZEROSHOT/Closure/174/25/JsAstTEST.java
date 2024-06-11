import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
  
  private JsAst jsAst;
  private SourceFile sourceFile;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js");
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
    SourceFile sourceFileResult = jsAst.getSourceFile();
    assertEquals(sourceFile, sourceFileResult);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newfile.js");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParse() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

}