import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class JsAstTest {
    private SourceFile testFile = new SourceFile("test.js", "function test() {}");

    @Test
    public void testConstructor() {
        JsAst jsAst = new JsAst(testFile);
        assertEquals("test.js", jsAst.getInputId().getIdName());
        assertEquals(testFile, jsAst.getSourceFile());
        assertNull(jsAst.getAstRoot(null));
        assertNull(jsAst.getSourceFile().getCachedSourceFile());
    }

    @Test
    public void testClearAst() {
        JsAst jsAst = new JsAst(testFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
        assertNull(jsAst.getSourceFile().getCachedSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        JsAst jsAst = new JsAst(testFile);
        SourceFile newFile = new SourceFile("newTest.js", "function newTest() {}");
        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
    }

    @Test
    public void testParseIOException() {
        JsAst jsAst = new JsAst(testFile);
        
        // Mocking an IOException
        AbstractCompiler mockCompiler = new AbstractCompiler();
        mockCompiler.setHasHaltingErrors(true);
        
        jsAst.getAstRoot(mockCompiler);
        assertNotNull(jsAst.getAstRoot(null));
    }
}