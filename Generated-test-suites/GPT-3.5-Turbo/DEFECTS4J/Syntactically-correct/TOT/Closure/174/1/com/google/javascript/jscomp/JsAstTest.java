import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceAst;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {
    private JsAst jsAst;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
        compiler = new AbstractCompiler();
    }

    @Test
    public void testGetAstRoot() {
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
        assertNull(jsAst.getSourceFile().getCachedSource());
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
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("new_test.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals("new_test.js", jsAst.getSourceFile().getName());
        assertEquals("var y = 10;", jsAst.getSourceFile().getCode());
    }

    @Test
    public void testParse_success() {
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }

    @Test
    public void testParse_ioException() {
        SourceFile sourceFile = new SourceFile("nonexistent_file.js", "");
        JsAst newJsAst = new JsAst(sourceFile);
        newJsAst.parse(compiler);

        Node astRoot = newJsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().toString());

        // Verify the error was reported
        assertEquals(1, compiler.getErrors().length);
        JSError error = compiler.getErrors()[0];
        assertEquals(AbstractCompiler.READ_ERROR, error.getType().key);
        assertEquals("nonexistent_file.js", error.sourceName);
    }

    @Test
    public void testParse_parseError() {
        SourceFile sourceFile = new SourceFile("parse_error.js", "var x = 5;");
        JsAst newJsAst = new JsAst(sourceFile);
        AbstractCompiler newCompiler = new AbstractCompiler();

        // Set an invalid parser configuration to trigger a parse error
        newCompiler.setParserConfig(null);

        newJsAst.parse(newCompiler);

        Node astRoot = newJsAst.getAstRoot(newCompiler);
        assertNotNull(astRoot);
        assertEquals("BLOCK", astRoot.getToken().toString());

        // Verify the error was reported
        assertEquals(1, newCompiler.getErrors().length);
        JSError error = newCompiler.getErrors()[0];
        assertEquals(AbstractCompiler.PARSE_ERROR, error.getType().key);
        assertEquals("parse_error.js", error.sourceName);
    }

    @Test
    public void testPrepareAst() {
        jsAst.parse(compiler);
        jsAst.prepareAst(jsAst.getAstRoot(compiler));
        // No assertions, just checking for any exceptions
    }

    @Test
    public void testSetStaticSourceFile() {
        jsAst.parse(compiler);
        jsAst.getAstRoot(compiler).setStaticSourceFile(jsAst.getSourceFile());
        // No assertions, just checking for any exceptions
    }

}