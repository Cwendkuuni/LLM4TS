package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

    private static final Logger logger_ = Logger.getLogger(JsAst.class.getName());

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);

        assertEquals(jsAst.getInputId(), astRoot.getInputId());
        assertEquals(jsAst.getSourceFile(), astRoot.getStaticSourceFile());
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(null));
        assertNull(jsAst.getSourceFile().getCachedSource());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("test_new.js", "console.log('New file');");
        jsAst.setSourceFile(newFile);

        assertEquals(newFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() throws IOException {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.parse(compiler);

        Node astRoot = jsAst.getAstRoot(compiler);

        assertEquals(jsAst.getInputId(), astRoot.getInputId());
        assertEquals(jsAst.getSourceFile(), astRoot.getStaticSourceFile());
        assertEquals("console.log('Hello, world!');", astRoot.toString());
    }
}