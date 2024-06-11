import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        
        jsAst.clearAst();
        
        assertNull(jsAst.getAstRoot());
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        
        InputId inputId = jsAst.getInputId();
        
        assertNotNull(inputId);
        assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        
        SourceFile result = jsAst.getSourceFile();
        
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);
        
        SourceFile newSourceFile = new SourceFile("newTest.js");
        jsAst.setSourceFile(newSourceFile);
        
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}