package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
    private JsAst jsAst;
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals(astRoot.getType(), Token.SCRIPT);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals(inputId.getId(), "test.js");
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals(sourceFile.getName(), "test.js");
        assertEquals(sourceFile.getCode(), "var x = 5;");
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("test2.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(jsAst.getSourceFile().getName(), "test2.js");
        assertEquals(jsAst.getSourceFile().getCode(), "var y = 10;");
    }
}