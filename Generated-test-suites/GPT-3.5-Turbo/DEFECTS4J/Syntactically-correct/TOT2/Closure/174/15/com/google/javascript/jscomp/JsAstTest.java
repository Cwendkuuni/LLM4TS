import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() { return true; }");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();

        Node root = jsAst.getAstRoot(compiler);

        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken().toString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() { return true; }");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() { return true; }");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() { return true; }");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test1.js", "function test() { return true; }");
        SourceFile sourceFile2 = new SourceFile("test2.js", "function test() { return true; }");
        JsAst jsAst = new JsAst(sourceFile1);

        jsAst.setSourceFile(sourceFile2);

        assertEquals(sourceFile2, jsAst.getSourceFile());
    }

}