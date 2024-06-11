import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot());
    }

    @Test
    public void testGetInputId() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertEquals("test.js", inputId.getId());
    }

    @Test
    public void testGetSourceFile() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("newTest.js");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        // Error scenario - IOException
        SourceFile sourceFile = new SourceFile("nonexistent.js");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot); // Dummy block should be created

        // Error scenario - Parse error
        sourceFile = new SourceFile("invalid.js");
        jsAst = new JsAst(sourceFile);
        astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot); // Dummy block should be created
    }
}