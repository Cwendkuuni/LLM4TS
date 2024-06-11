import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;
 private SourceFile sourceFile;

 @Before
 public void setup() {
 // Initialize the test compiler and source file
 compiler = new AbstractCompiler();
 sourceFile = new SourceFile("test.js");
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 Node expectedAst = IR.script();
 Node actualAst = jsAst.getAstRoot(compiler);
 assertEquals("Root nodes should be equal", expectedAst, actualAst);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 Node actualAst = jsAst.getAstRoot(compiler);
 assertNull("Root node should be null after clearing", actualAst);
 }

 @Test
 public void testGetInputId() {
 InputId expectedInputId = new InputId("test.js");
 InputId actualInputId = jsAst.getInputId();
 assertEquals("Input IDs should be equal", expectedInputId, actualInputId);
 }

 @Test
 public void testGetSourceFile() {
 assertEquals("Source files should be equal", sourceFile, jsAst.getSourceFile());
 }

 @Test
 public void testSetSourceFile() {
 SourceFile newSourceFile = new SourceFile("newTest.js");
 jsAst.setSourceFile(newSourceFile);
 assertEquals("New source files should be equal", newSourceFile, jsAst.getSourceFile());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSourceFileWithDifferentName() {
 SourceFile newSourceFile = new SourceFile("test.js");
 jsAst.setSourceFile(newSourceFile);
 }

 @Test
 public void testParse() throws IOException {
 // Given a valid JavaScript source file
 String validSourceCode = "function test() { return 42; }";
 sourceFile.setCode(validSourceCode);

 // When parsing the source file
 jsAst.parse(compiler);
 Node expectedAst = ParserRunner.parse(sourceFile, validSourceCode,
 compiler.getParserConfig(),
 compiler.getDefaultErrorReporter(),
 logger_).ast;
 Node actualAst = jsAst.getAstRoot(compiler);

 // Then theparse should have succeeded and the AST should be equal
 assertNotNull("Parse should not fail", actualAst);
 assertEquals("AST roots should be equal", expectedAst, actualAst);
 }

 @Test
 public void testParseWithIOException() throws IOException {
 // Given an invalid JavaScript source file
 sourceFile.setCode("InvalidJavaScriptCode");

 // When parsing the source file
 jsAst.parse(compiler);
 Node expectedAst = IR.script();
 Node actualAst = jsAst.getAstRoot(compiler);

 // Then the parse should have failed and the AST should be a dummy block
 assertNotNull("Parse should have failed", actualAst);
 assertEquals("Root nodes should be equal", expectedAst, actualAst);
 }
}