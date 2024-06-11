import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest extends TestCase {

    JsAst jsAst;

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
        Node astRoot = jsAst.getAstRoot(new AbstractCompiler());
        assertNull(astRoot);
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
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("new_test.js", "var y = 20;");
        jsAst.setSourceFile(newFile);
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals("new_test.js", sourceFile.getName());
    }
}