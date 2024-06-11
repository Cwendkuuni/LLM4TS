import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile file = new SourceFile("test2.js", "var y = 20;");
        jsAst.setSourceFile(file);
        assertEquals(file, jsAst.getSourceFile());
    }

}