import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

    private static final String FILE_NAME = "test.js";
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile(FILE_NAME, "function test() { return true; }");
    }

    @Test
    public void testGetAstRoot() {
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(new Compiler());
        
        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken());
    }

    @Test
    public void testClearAst() {
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(new Compiler());
        
        assertNotNull(root);
        
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new Compiler()));
    }

    @Test
    public void testGetInputId() {
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();

        assertNotNull(inputId);
        assertEquals(FILE_NAME, inputId.idName);
    }

    @Test
    public void testGetSourceFile() {
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile returnedSourceFile = jsAst.getSourceFile();

        assertNotNull(returnedSourceFile);
        assertEquals(FILE_NAME, returnedSourceFile.getName());
    }

    @Test
    public void testSetSourceFile() {
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("newFile.js", "var x = 10;");

        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}