import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

 private JsAst jsAst;
 private AbstractCompiler compiler;
 private SourceFile sourceFile;

 @Before
 public void setUp() {
 sourceFile = new SourceFile("test.js", "test source code");
 jsAst = new JsAst(sourceFile);
 compiler = new AbstractCompiler();
 }

 @Test
 public void testGetAstRoot() {
 Node astRoot = jsAst.getAstRoot(compiler);
 assertNotNull(astRoot);
 }

 @Test(expected = NullPointerException.class)
 public void testGetAstRootNullCompiler() {
 jsAst.getAstRoot(null);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testGetInputId() {
 assertEquals("test.js", jsAst.getInputId().getName());
 }

 @Test
 public void testGetSourceFile() {
 assertEquals("test source code", jsAst.getSourceFile().getCode());
 }

 @Test
 public void testSetSourceFile() {
 SourceFile newSourceFile = new SourceFile("test2.js", "test2 source code");
 jsAst.setSourceFile(newSourceFile);
 assertEquals("test2.js", jsAst.getInputId().getName());
 assertEquals("test2 source code", jsAst.getSourceFile().getCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSourceFileDifferentFileName() {
 SourceFile newSourceFile = new SourceFile("test.js", "test source code");
 jsAst.setSourceFile(newSourceFile);
 }

 @Test(expected = IOException.class)
 public void testParseIOException() {
 jsAst.parse(compiler);
 }

 @Test
 public void testParseNullAst() {
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testParseEmptyAst() {
 jsAst = new JsAst(sourceFile);
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 assertTrue(jsAst.getAstRoot(compiler).isScript());
 }

 @Test
 public void testParseValidAst() {
 jsAst = new JsAst(sourceFile);
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 assertFalse(jsAst.getAstRoot(compiler).isScript());
 }
}