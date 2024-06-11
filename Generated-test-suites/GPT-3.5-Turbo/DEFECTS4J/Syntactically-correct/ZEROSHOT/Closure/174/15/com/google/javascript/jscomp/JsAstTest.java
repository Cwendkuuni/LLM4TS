import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);

        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();

        assertNull(jsAst.root);
        assertEquals("", sourceFile.getCachedSource());
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();

        assertNotNull(inputId);
        assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile sf = jsAst.getSourceFile();

        assertNotNull(sf);
        assertEquals("test.js", sf.getName());
        assertEquals("var x = 5;", sf.getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);

        assertEquals("newTest.js", jsAst.getSourceFile().getName());
        assertEquals("var y = 10;", jsAst.getSourceFile().getCode());
    }
}