import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parsing.ParserRunner.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public ParserConfig getParserConfig() {
        return ParserConfig.createDefault();
      }

      @Override
      public CompilerOptions getOptions() {
        return new CompilerOptions();
      }

      @Override
      public void report(JSError error) {
        fail("unexpected error: " + error.getMessage());
      }

      @Override
      public void fatalError(JSError error) {
        fail("unexpected error: " + error.getMessage());
      }
    };

    sourceFile = new SourceFile("test.js", "test source code");
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);
    assertNull(newAstRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("test source code", sourceFile.getCode());
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test source code");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("new test source code", sourceFile.getCode());
  }

  @Test
  public void testParse() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = IOException.class)
  public void testIOException() {
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner.ParseResult result = ParserRunner.parse(sourceFile, sourceFile.getCode(),
        compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_);
    jsAst.parse(compiler);
  }

  @Test
  public void testParseWithError() {
    sourceFile.setCode("function foo() { throw new Error('Test error'); }");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseWithEmptyFile() {
    sourceFile.setCode("");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot instanceof IR.Program);
    assertEquals(0, ((IR.Program) astRoot).getStatements().size());
  }
}