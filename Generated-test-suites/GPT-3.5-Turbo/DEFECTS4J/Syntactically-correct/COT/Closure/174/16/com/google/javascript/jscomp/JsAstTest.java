import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsAstTest {
  
  private JsAst jsAst;
  private DummyCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new DummyCompiler();
    jsAst = new JsAst(new SourceFile("test.js", "test code"));
  }
  
  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.script(), astRoot);
  }
  
  @Test
  public void testClearAst() {
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
    assertEquals("test code", sourceFile.getCode());
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test code");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newTest.js", jsAst.getSourceFile().getName());
    assertEquals("new test code", jsAst.getSourceFile().getCode());
  }
  
  private static class DummyCompiler extends AbstractCompiler {
    @Override
    public ParserConfig getParserConfig() {
      return new ParserConfig(new CompilerOptions());
    }
    
    @Override
    public void setOldParseTree(String filename, Node tree) {
      // Do nothing
    }
    
    @Override
    public void report(JSError error) {
      // Do nothing
    }
    
    @Override
    public boolean hasHaltingErrors() {
      return false;
    }
    
    @Override
    public void prepareAst(Node root) {
      // Do nothing
    }
  }
}