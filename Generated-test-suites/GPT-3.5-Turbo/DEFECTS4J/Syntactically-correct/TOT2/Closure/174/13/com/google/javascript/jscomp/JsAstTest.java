import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() { return true; }");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        assertNotNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertEquals(null, jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        assertNotNull(jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        assertNotNull(jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("newTest.js", "function newTest() { return false; }");
        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
    }

}