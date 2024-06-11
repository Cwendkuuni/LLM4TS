import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private ParserConfig parserConfig;
  private ParserRunner parserRunner;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    sourceFile = mock(SourceFile.class);
    parserConfig = mock(ParserConfig.class);
    parserRunner = mock(ParserRunner.class);
  }

  @Test
  public void testGetAstRoot() {
    when(parserRunner.parse(sourceFile, "testCode", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult());

    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = mock(SourceFile.class);
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParseWithNoErrors() {
    when(parserRunner.parse(sourceFile, "testCode", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult());

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testParseWithErrors() {
    when(parserRunner.parse(sourceFile, "testCode", parserConfig, compiler.getErrorReporter(), logger_))
        .thenThrow(new IOException());

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testParseWithHaltingErrors() {
    CompilerOptions options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    when(compiler.hasHaltingErrors()).thenReturn(true);

    when(parserRunner.parse(sourceFile, "testCode", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult());

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
}