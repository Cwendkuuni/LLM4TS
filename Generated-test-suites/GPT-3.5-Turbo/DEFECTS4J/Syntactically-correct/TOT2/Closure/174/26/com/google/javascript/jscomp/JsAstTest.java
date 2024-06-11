import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "function add(a, b) { return a + b; }");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new Compiler();
        Node root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
        assertEquals(Token.ROOT, root.getToken());
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new Compiler()));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals("test.js", inputId.id);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("new_test.js", "var x = 10;");
        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        AbstractCompiler compiler = new Compiler();
        jsAst.getAstRoot(compiler);

        Node root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
        assertEquals(Token.ROOT, root.getToken());
    }

    // Add more test cases for edge cases and error scenarios as needed

}