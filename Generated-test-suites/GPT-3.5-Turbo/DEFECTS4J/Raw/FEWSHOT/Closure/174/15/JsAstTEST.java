import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertEquals("SCRIPT", astRoot.getToken().toString());
        assertEquals(1, astRoot.getChildCount());
    }
    
    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertEquals("test.js", inputId.getId());
    }
    
    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(newSourceFile, result);
    }
}