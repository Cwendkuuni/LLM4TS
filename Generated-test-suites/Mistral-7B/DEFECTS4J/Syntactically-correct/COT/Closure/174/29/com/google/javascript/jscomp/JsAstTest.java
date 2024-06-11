import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

 private SourceFile sourceFile;
 private AbstractCompiler compiler;
 private JsAst jsAst;

 @Before
 public void setup() {
 sourceFile = new SourceFile("test.js", "test source code");
 compiler = new AbstractCompiler();
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 Node expectedRoot = IR.script();
 Node actualRoot = jsAst.getAstRoot(compiler);
 assertEquals(expectedRoot, actualRoot);
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
 SourceFile newSourceFile = new SourceFile("test2.js", "test source code");
 jsAst.setSourceFile(newSourceFile);
 assertEquals(newSourceFile, jsAst.getSourceFile());
 }

 @Test(expected = AssertionError.class)
 public void testGetAstRootWithParseError() {
 compiler.setHaltingErrors(true);
 jsAst.getAstRoot(compiler);
 }

 @Test
 public void testParseWithEmptySourceCode() {
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testParseWithValidSourceCode() {
 String validSourceCode = "function testFunction() {}";
 sourceFile.setCode(validSourceCode);
 jsAst.parse(compiler);
 Node expectedRoot = IR.script().setExpression(IR.callExpr(
 IR.identifier("testFunction"), IR.lit(null)));
 Node actualRoot = jsAst.getAstRoot(compiler);
 assertEquals(expectedRoot, actualRoot);
 }

 @Test
 public void testParseWithIOException() throws IOException {
 InputId inputId = new InputId("test.js");
 java.io.File file = new java.io.File("non-existent-file.js");
 sourceFile = new SourceFile(file, inputId.getCanonicalName());
 compiler.setParserConfig(new ParserConfig());
 jsAst = new JsAst(sourceFile);
 ParserRunner.ParseResult parseResult = ParserRunner.parse(sourceFile, null, compiler.getParserConfig(),
 compiler.getDefaultErrorReporter(), logger_);
 jsAst.parse(compiler);
 Node expectedRoot = IR.script();
 Node actualRoot = jsAst.getAstRoot(compiler);
 assertEquals(expectedRoot, actualRoot);
 }
}