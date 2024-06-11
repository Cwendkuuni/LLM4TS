package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        SourceFile sourceFile = mock(SourceFile.class);
        ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
        Node ast = mock(Node.class);

        when(sourceFile.getName()).thenReturn("test-file.js");
        when(sourceFile.getCode()).thenReturn("var x = 5;");
        when(compiler.getParserConfig()).thenReturn(new ParserConfig());
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
        when(parseResult.ast).thenReturn(ast);

        JsAst jsAst = new JsAst(sourceFile);

        jsAst.getAstRoot(compiler);

        verify(ast).setInputId(any(InputId.class));
        verify(compiler).setOldParseTree(eq("test-file.js"), any(Node.class));
        verify(compiler).prepareAst(ast);
        verify(ast).setStaticSourceFile(sourceFile);

    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(jsAst.getInputId(), new InputId(sourceFile.getName()));
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(jsAst.getSourceFile(), sourceFile);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = mock(SourceFile.class);
        SourceFile sourceFile2 = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile1);
        jsAst.setSourceFile(sourceFile2);
        assertEquals(jsAst.getSourceFile(), sourceFile2);
    }

}