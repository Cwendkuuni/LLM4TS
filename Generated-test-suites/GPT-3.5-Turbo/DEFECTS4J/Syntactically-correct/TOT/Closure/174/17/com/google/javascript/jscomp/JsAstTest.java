import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    private SourceFile sourceFile;
    private JsAst jsAst;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var x = 10;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot_NullRoot_ReturnsParsedRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("script", astRoot.getToken().toString());
    }

    @Test
    public void testGetAstRoot_AlreadyParsed_ReturnsCachedRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot1 = jsAst.getAstRoot(compiler);
        Node astRoot2 = jsAst.getAstRoot(compiler);
        assertSame(astRoot1, astRoot2);
    }

    @Test
    public void testClearAst_RootCleared() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot1 = jsAst.getAstRoot(compiler);
        jsAst.clearAst();
        Node astRoot2 = jsAst.getAstRoot(compiler);
        assertNotSame(astRoot1, astRoot2);
    }

    @Test
    public void testGetInputId_ReturnsInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile_ReturnsSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
        assertEquals("var x = 10;", sourceFile.getCode());
    }

    @Test
    public void testSetSourceFile_ChangesSourceFile() {
        SourceFile newSourceFile = new SourceFile("modified.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        SourceFile updatedSourceFile = jsAst.getSourceFile();
        assertNotNull(updatedSourceFile);
        assertEquals("modified.js", updatedSourceFile.getName());
        assertEquals("var y = 20;", updatedSourceFile.getCode());
    }
}