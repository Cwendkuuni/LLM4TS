
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setup() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        SourceAst astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
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
        SourceFile file = new SourceFile("test2.js", "var y = 10;");
        jsAst.setSourceFile(file);
        assertEquals(file, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.clearAst();
        jsAst.parse(compiler);
        assertNotNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithIOException() {
        AbstractCompiler compiler = new AbstractCompiler();
        SourceFile file = new SourceFile("nonexistent.js", "");
        jsAst.setSourceFile(file);
        jsAst.clearAst();
        jsAst.parse(compiler);
        assertTrue(compiler.hasHaltingErrors()); // Expecting an IOException
        assertTrue(jsAst.getAstRoot(compiler).isScript()); // Expecting a dummy block
    }
}
