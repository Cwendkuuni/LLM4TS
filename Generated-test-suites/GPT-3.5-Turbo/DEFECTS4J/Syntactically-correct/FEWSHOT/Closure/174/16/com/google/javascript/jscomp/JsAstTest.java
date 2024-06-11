package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        jsAst = new JsAst(sourceFile);
    }

    @After
    public void tearDown() {
        jsAst = null;
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals(IR.script(), astRoot);
    }

    @Test
    public void testClearAst() {
        assertNull(jsAst.root);
        jsAst.clearAst();
        assertNull(jsAst.root);
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
        SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals("newTest.js", jsAst.fileName);
        assertEquals("newTest.js", jsAst.getSourceFile().getName());
        assertEquals("var y = 20;", jsAst.getSourceFile().getCode());
    }
}