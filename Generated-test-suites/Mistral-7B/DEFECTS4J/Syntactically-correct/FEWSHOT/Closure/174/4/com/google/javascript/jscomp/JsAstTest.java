package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        Node root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(mock(AbstractCompiler.class)));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().getName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
        SourceFile sourceFile2 = new SourceFile("test2.js", "test content 2");
        JsAst jsAst = new JsAst(sourceFile1);
        jsAst.setSourceFile(sourceFile2);
        assertEquals(sourceFile2, jsAst.getSourceFile());
    }

    @Test
    public void testParseWithNullCompiler() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null);
        assertNotNull(jsAst.getAstRoot(mock(AbstractCompiler.class)));
    }

    @Test
    public void testParseWithEmptyContent() {
        SourceFile sourceFile = new SourceFile("test.js", "");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        jsAst.parse(compiler);
        Node root = jsAst.getAstRoot(compiler);
        assertEquals(IR.script(), root);
    }

    @Test
    public void testParseWithIOException() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        doThrow(new IOException("Test IOException")).when(compiler).report(any());
        jsAst.parse(compiler);
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithNullAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        doReturn(null).when(compiler).getDefaultErrorReporter();
        jsAst.parse(compiler);
        Node root = jsAst.getAstRoot(compiler);
        assertEquals(IR.script(), root);
    }

    @Test
    public void testParseWithInvalidAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        doReturn(mock(Node.class)).when(compiler).getDefaultErrorReporter();
        jsAst.parse(compiler);
        Node root = jsAst.getAstRoot(compiler);
        assertEquals(IR.script(), root);
    }
}