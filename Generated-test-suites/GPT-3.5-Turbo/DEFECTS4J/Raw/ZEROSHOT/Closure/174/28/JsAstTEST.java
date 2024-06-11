import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {
    private JsAst jsAst;
    private Node astRoot;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
        compiler = new AbstractCompiler();
    }

    @Test
    public void testGetAstRoot() {
        astRoot = jsAst.getAstRoot(compiler);
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
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("new_test.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}