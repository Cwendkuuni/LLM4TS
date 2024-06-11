package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
    private SourceFile sourceFile;
    private JsAst jsAst;

    @Before
    public void setUp() {
        sourceFile = mock(SourceFile.class);
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNull(astRoot);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNull(inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile file = jsAst.getSourceFile();
        assertEquals(sourceFile, file);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile file = mock(SourceFile.class);
        jsAst.setSourceFile(file);
        SourceFile newFile = jsAst.getSourceFile();
        assertEquals(file, newFile);
    }

    @Test
    public void testParseIOException() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        jsAst.parse(compiler);
        assertNull(jsAst.getAstRoot(compiler));
    }
}