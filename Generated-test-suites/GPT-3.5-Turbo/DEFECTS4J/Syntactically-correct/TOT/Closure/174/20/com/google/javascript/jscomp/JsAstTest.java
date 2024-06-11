import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    private SourceFile sourceFile;
    private JsAst jsAst;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("example.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
        assertNull(astRoot);
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals("example.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile file = jsAst.getSourceFile();
        assertNotNull(file);
        assertEquals(sourceFile, file);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("new-example.js", "var y = 10;");
        jsAst.setSourceFile(newFile);
        SourceFile file = jsAst.getSourceFile();
        assertNotNull(file);
        assertEquals(newFile, file);
    }

    @Test
    public void testParse() {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.clearAst();
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }

    @Test
    public void testParseWithError() {
        // Test for IOException
        SourceFile invalidFile = new SourceFile("invalid.js", null);
        jsAst.setSourceFile(invalidFile);
        AbstractCompiler compiler1 = new AbstractCompiler();
        jsAst.parse(compiler1);
        Node astRoot1 = jsAst.getAstRoot(compiler1);
        assertNull(astRoot1);
        assertTrue(compiler1.hasHaltingErrors());

        // Test for parse error
        SourceFile invalidSyntaxFile = new SourceFile("invalid-syntax.js", "var x =");
        jsAst.setSourceFile(invalidSyntaxFile);
        AbstractCompiler compiler2 = new AbstractCompiler();
        jsAst.parse(compiler2);
        Node astRoot2 = jsAst.getAstRoot(compiler2);
        assertNotNull(astRoot2);
        assertEquals("SCRIPT", astRoot2.getToken().toString());
        assertTrue(compiler2.hasHaltingErrors());
    }
}