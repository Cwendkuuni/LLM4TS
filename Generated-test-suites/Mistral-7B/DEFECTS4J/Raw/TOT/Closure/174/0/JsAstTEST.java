import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ErrorReporter;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsAstTest {

 private JsAst jsAst;
 private AbstractCompiler compiler;
 private SourceFile sourceFile;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 sourceFile = new SourceFile("test.js", "test code");
 compiler = mock(AbstractCompiler.class);
 errorReporter = mock(ErrorReporter.class);
 jsAst = new JsAst(sourceFile);
 when(compiler.getParserConfig()).thenReturn(new ParserConfig());
 when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
 when(compiler.getErrorReporter()).thenReturn(errorReporter);
 }

 @Test
 public void testGetAstRoot() {
 Node astRoot = jsAst.getAstRoot(compiler);
 assertNotNull(astRoot);
 }

 @Test
 public void testParseWithErrors() {
 when(errorReporter.fatal(anyString())).thenAnswer(invocationOnMock -> {});
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 }

 @Test(expected = IOException.class)
 public void testParseWithIOException() {
 when(errorReporter.fatal(anyString())).thenAnswer(invocationOnMock -> {
 throw new IOException("Test IOException");
 });
 jsAst.parse(compiler);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }
}