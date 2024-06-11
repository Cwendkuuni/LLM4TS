package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = mock(SourceFile.class);
        when(sourceFile.getName()).thenReturn("test.js");
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
        Node astRoot = jsAst.getAstRoot(mock(AbstractCompiler.class));
        assertNull(astRoot);
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile file = mock(SourceFile.class);
        when(file.getName()).thenReturn("newTest.js");
        jsAst.setSourceFile(file);
        SourceFile sourceFile = jsAst.getSourceFile();
        assertEquals("newTest.js", sourceFile.getName());
    }

    @Test
    public void testParse() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        SourceFile sourceFile = mock(SourceFile.class);
        when(sourceFile.getName()).thenReturn("test.js");
        when(sourceFile.getCode()).thenReturn("");
        when(compiler.getParserConfig()).thenReturn(mock(ParserConfig.class));
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
        jsAst.setSourceFile(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertEquals("SCRIPT", astRoot.getToken().toString());
    }
}