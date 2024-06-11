import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.testtype.ErrorProneParserConfig.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler(new CompilerOptions());
  }

  @Test
  public void testGetAstRoot() {
    sourceFile = new SourceFile("Test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    sourceFile = new SourceFile("Test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newRoot = jsAst.getAstRoot(compiler);
    assertNull(newRoot);
  }

  @Test
  public void testGetInputId() {
    sourceFile = new SourceFile("Test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("Test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    sourceFile = new SourceFile("Test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("Test.js", sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    sourceFile = new SourceFile("Test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("NewTest.js", "new test code");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("NewTest.js", sourceFile.getName());
  }

  @Test
  public void testParseSuccess() throws IOException {
    sourceFile = new SourceFile("Test.js", "function test(){}");
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner.ParseResult result = parserConfig(ECMASCRIPT5).parse(sourceFile, sourceFile.getCode(), compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals("test", root.getFirstChild().getIdentifier().getName());
  }

  @Test
  public void testParseFailure() throws IOException {
    sourceFile = new SourceFile("Test.js", "invalid code");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testSetSourceFileClearAst() throws IOException {
    sourceFile = new SourceFile("Test.js", "function test(){}");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newRoot = jsAst.getAstRoot(compiler);
    assertNull(newRoot);

    sourceFile = new SourceFile("NewTest.js", "new test code");
    jsAst.setSourceFile(sourceFile);
    Node newRootNewParse = jsAst.getAstRoot(compiler);
    assertNull(newRootNewParse);
  }
}