import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsCompiler.SourceFile;
import static com.google.javascript.jscomp.parser.ParserConfig.ParserStyle.ECMASCRIPT3;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.parser.ParserConfig;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsAstTest {

  private static final String TEST_SOURCE = "testSource";
  private static final String TEST_MODULE = "testModule";

  @Mock
  private AbstractCompiler compiler;

  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    sourceFile = new SourceFile(TEST_SOURCE, TEST_MODULE);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node expectedAst = IR.script();
    Preconditions.checkState(expectedAst != null);
    when(compiler.getParserConfig()).thenReturn(new ParserConfig(ECMASCRIPT3));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));

    Node actualAst = jsAst.getAstRoot(compiler);
    assertEquals(expectedAst, actualAst);
  }

  @Test
  public void testClearAst() {
    Node root = IR.script();
    jsAst.root = root;
    jsAst.clearAst();
    assertNull(jsAst.root);
  }

  @Test
  public void testGetInputId() {
    assertSame(new InputId(TEST_SOURCE), jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertSame(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newSource", TEST_MODULE);
    jsAst.setSourceFile(newSourceFile);
    assertSame(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParseSuccess() throws IOException {
    String validCode = "function main() {}";
    when(compiler.getParserConfig()).thenReturn(new ParserConfig(ECMASCRIPT3));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));

    ParserRunner.ParseResult parseResult = ParserRunner.parse(sourceFile, validCode,
        compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_);
    Node expectedAst = parseResult.ast;
    Node actualAst = jsAst.parse(compiler);

    assertEquals(expectedAst, actualAst);
  }

  @Test
  public void testParseFailure() throws IOException {
    String invalidCode = "function main() {^";
    when(compiler.getParserConfig()).thenReturn(new ParserConfig(ECMASCRIPT3));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));

    jsAst.parse(compiler);
    verify(compiler, times(1)).report(argThat(error -> error.getMessage().contains("Unexpected end of input")));
  }
}