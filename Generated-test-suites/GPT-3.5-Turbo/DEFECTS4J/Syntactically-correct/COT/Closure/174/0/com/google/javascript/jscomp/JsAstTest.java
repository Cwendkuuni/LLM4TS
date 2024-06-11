
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new Compiler();
        Node root = jsAst.getAstRoot(compiler);

        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken().toString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();

        assertNotNull(inputId);
        assertEquals("test.js", inputId.getName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile result = jsAst.getSourceFile();

        assertNotNull(result);
        assertEquals("test.js", result.getName());
        assertEquals("var x = 5;", result.getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.setSourceFile(new SourceFile("new.js", "var y = 10;"));

        SourceFile result = jsAst.getSourceFile();
        assertNotNull(result);
        assertEquals("new.js", result.getName());
        assertEquals("var y = 10;", result.getCode());
    }

}
