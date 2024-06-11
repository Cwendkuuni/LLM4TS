import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    private JsAst jsAst;
    private SourceFile sourceFile;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
        compiler = new AbstractCompiler();
    }

    @Test
    public void testGetAstRoot() {
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals("test.js", inputId.getId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
        assertEquals("var x = 5;", sourceFile.getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("test2.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        jsAst.clearAst();
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testParseWithIOException() {
        SourceFile sourceFile = new SourceFile("test3.js", "var z = 15;");
        jsAst.setSourceFile(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testParseWithParseError() {
        SourceFile sourceFile = new SourceFile("test4.js", "var x = ;");
        jsAst.setSourceFile(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }
}