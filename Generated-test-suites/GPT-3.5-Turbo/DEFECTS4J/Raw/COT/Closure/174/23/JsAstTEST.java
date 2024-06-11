import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        Node astRoot = jsAst.getAstRoot(new AbstractCompiler());

        assertNotNull(astRoot);
        assertEquals("script", astRoot.getType());
        assertEquals(1, astRoot.getChildCount());
        assertEquals("VAR", astRoot.getFirstChild().getType());
        assertEquals("x", astRoot.getFirstChild().getFirstChild().getString());
        assertEquals("NUMBER", astRoot.getFirstChild().getLastChild().getType());
        assertEquals("10", astRoot.getFirstChild().getLastChild().getString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
        assertNull(sourceFile.getCachedSource());
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals("test.js", jsAst.getInputId().getId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test1.js", "var x = 10;");
        SourceFile sourceFile2 = new SourceFile("test2.js", "var y = 20;");
        JsAst jsAst = new JsAst(sourceFile1);
        jsAst.setSourceFile(sourceFile2);

        assertEquals(sourceFile2, jsAst.getSourceFile());
    }
    
    @Test
    public void testParse_ReadError() {
        SourceFile sourceFile = new SourceFile("test.js", "");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(new AbstractCompiler());

        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
        // TODO: assert error handling
    }

    @Test
    public void testParse_Success() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(new AbstractCompiler());

        assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
        // TODO: assert parsed AST
    }
}