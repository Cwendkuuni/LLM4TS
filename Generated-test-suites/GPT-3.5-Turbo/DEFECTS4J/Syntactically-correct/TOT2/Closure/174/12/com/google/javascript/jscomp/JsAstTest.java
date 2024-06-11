import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid source file
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().getId());
    }

    @Test
    public void testGetAstRoot() {
        // Test getAstRoot with empty compiler
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testClearAst() {
        // Test clearAst method
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        // Test getInputId method
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().getId());
    }

    @Test
    public void testSetSourceFile() {
        // Test setSourceFile method with valid file name
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newFile = new SourceFile("newFile.js", "var y = 10;");
        jsAst.setSourceFile(newFile);
        assertEquals("newFile.js", jsAst.getSourceFile().getName());
    }

    // Add more test cases for additional methods if necessary

}