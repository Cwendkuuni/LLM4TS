import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {
    
    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst);
        assertEquals("test.js", jsAst.getSourceFile().getName());
    }
    
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(new AbstractCompiler());
        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken());
    }
    
    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }
    
    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().getIdPath());
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newFile = new SourceFile("newtest.js", "var y = 10;");
        jsAst.setSourceFile(newFile);
        assertEquals("newtest.js", jsAst.getSourceFile().getName());
    }
}