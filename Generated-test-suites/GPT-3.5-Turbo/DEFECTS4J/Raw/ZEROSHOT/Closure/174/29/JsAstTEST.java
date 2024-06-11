import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {

  private JsAst jsAst;
  
  @Before
  public void setUp() {
    SourceFile sourceFile = new SourceFile("test.js");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node result = jsAst.getAstRoot(compiler);
    assertNotNull(result);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
    assertNull(jsAst.getInputId());
    assertNull(jsAst.getSourceFile());
    assertEquals("test.js", jsAst.fileName);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile file = new SourceFile("newFile.js");
    jsAst.setSourceFile(file);
    assertEquals(file, jsAst.getSourceFile());
  }

}