import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {

    /**
     * Generated test cases for each method in the JsAst class
     */

    private JsAst jsAst;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        jsAst = new JsAst(sourceFile);
        compiler = new Compiler();
    }

    @Test
    public void testGetAstRoot() {
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals(Node.SCRIPT, astRoot.getToken());
        assertEquals(1, astRoot.getChildCount());
        assertEquals(Node.VAR, astRoot.getFirstChild().getToken());
        assertEquals("x", astRoot.getFirstFirstChild().getString());
        assertEquals(Token.NUMBER, astRoot.getFirstFirstChild().getFirstChild().getToken());
        assertEquals("10", astRoot.getFirstFirstChild().getFirstChild().getString());
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
        assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
        assertEquals("var x = 10;", sourceFile.getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("test-new.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    /**
     * Additional test cases for potential edge cases and exceptions
     */

    @Test(expected = IllegalStateException.class)
    public void testSetSourceFileWithDifferentFilename() {
        SourceFile newSourceFile = new SourceFile("test-new.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
    }

    @Test
    public void testParseWithIOException() {
        SourceFile sourceFile = new SourceFile("nonexistent.js", "");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        assertTrue(compiler.hasHaltingErrors());
    }

    @Test
    public void testParseWithParseError() {
        SourceFile sourceFile = new SourceFile("test-error.js", "var x = ;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        assertTrue(compiler.hasHaltingErrors());
    }

    @Test
    public void testParseWithSuccessfulParsing() {
        SourceFile sourceFile = new SourceFile("test-success.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        assertFalse(compiler.hasHaltingErrors());
        assertNotNull(jsAst.getAstRoot(compiler));
    }

}