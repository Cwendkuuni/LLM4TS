import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

    @Test
    public void testConstructor() {
        // Test constructor by creating a JsAst object and checking if the fileName is set correctly.
        SourceFile sourceFile = new SourceFile("testFile.js", "console.log('Hello World');");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("testFile.js", jsAst.fileName);
    }

    @Test
    public void testGetAstRoot() {
        // Test getAstRoot by creating a JsAst object and checking if the AST root is generated correctly.
        SourceFile sourceFile = new SourceFile("testFile.js", "console.log('Hello World');");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testClearAst() {
        // Test clearAst by creating a JsAst object, clearing the AST, and checking if the AST root is null.
        SourceFile sourceFile = new SourceFile("testFile.js", "console.log('Hello World');");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testInputId() {
        // Test getInputId by creating a JsAst object and checking if the inputId is not null.
        SourceFile sourceFile = new SourceFile("testFile.js", "console.log('Hello World');");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst.getInputId());
    }

    @Test
    public void testSetSourceFile() {
        // Test setSourceFile by creating a JsAst object, setting a new source file, and checking if the source file is updated.
        SourceFile sourceFile = new SourceFile("testFile.js", "console.log('Hello World');");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("newFile.js", "console.log('New File');");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

}