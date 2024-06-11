import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.InputId;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

 private AbstractCompiler compiler;
 private SourceFile sourceFile;
 private JsAst jsAst;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 sourceFile = new SourceFile("test.js", "test source");
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 Node expectedAstRoot = ParserRunner.parse(sourceFile, sourceFile.getCode(),
 compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_).ast;
 Node actualAstRoot = jsAst.getAstRoot(compiler);
 assertEquals(expectedAstRoot, actualAstRoot);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testGetInputId() {
 InputId expectedInputId = new InputId("test.js");
 InputId actualInputId = jsAst.getInputId();
 assertEquals(expectedInputId, actualInputId);
 }

 @Test
 public void testGetSourceFile() {
 assertEquals(sourceFile, jsAst.getSourceFile());
 }

 @Test
 public void testSetSourceFile() {
 SourceFile newSourceFile = new SourceFile("newTest.js", "new test source");
 jsAst.setSourceFile(newSourceFile);
 assertEquals(newSourceFile, jsAst.getSourceFile());
 }

 @Test(expected = AssertionError.class)
 public void testParseIOException() {
 compiler.setErrorReporter(errorReporter -> {
 throw new IOException("Test IOException");
 });
 jsAst.parse(compiler);
 }

 @Test
 public void testParseParserError() {
 compiler.setErrorReporter(errorReporter -> {
 errorReporter.reportError(0, 0, "Test Parser Error", null);
 });
 jsAst.parse(compiler);
 assertNull(jsAst.getAstRoot(compiler));
 }
}