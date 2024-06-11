import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
  private static final Logger logger_ = Logger.getLogger(JsAst.class.getName());

  private JsAst jsAst;

  @Before
  public void setUp() {
    jsAst = new JsAst(new SourceFile("testFile.js", ""));
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    // Assert that the AST root is not null
    Assert.assertNotNull(astRoot);
    // Assert that the AST root has the correct input ID
    Assert.assertEquals(jsAst.getInputId(), astRoot.getInputId());
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    // Assert that the AST root is null after clearing
    Assert.assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    // Assert that the input ID is not null
    Assert.assertNotNull(inputId);
    // Assert that the input ID has the correct ID
    Assert.assertEquals("testFile.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    // Assert that the source file is not null
    Assert.assertNotNull(sourceFile);
    // Assert that the source file has the correct name
    Assert.assertEquals("testFile.js", sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile file = new SourceFile("newTestFile.js", "");
    jsAst.setSourceFile(file);
    // Assert that the source file is set correctly
    Assert.assertEquals(file, jsAst.getSourceFile());
  }

  @Test
  public void testParse() {
    AbstractCompiler compiler = new Compiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    // Assert that the AST root is not null after parsing
    Assert.assertNotNull(astRoot);
    // Assert that the compiler has the old parse tree set
    Assert.assertNotNull(compiler.getOldParseTree("testFile.js"));
    // Assert that the root has the correct static source file set
    Assert.assertEquals(jsAst.getSourceFile(), astRoot.getStaticSourceFile());
  }

}