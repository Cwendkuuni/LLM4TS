import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsAstTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getInputId());
        assertEquals("test.js", jsAst.getSourceFile().getName());
    }

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newFile = new SourceFile("newTest.js");
        jsAst.setSourceFile(newFile);

        assertEquals("newTest.js", jsAst.getSourceFile().getName());
    }

    @Test
    public void testParse() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
    }
}