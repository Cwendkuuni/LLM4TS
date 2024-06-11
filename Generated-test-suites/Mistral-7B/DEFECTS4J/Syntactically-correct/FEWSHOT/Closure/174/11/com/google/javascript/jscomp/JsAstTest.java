package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserConfig;
import com.google.javascript.rhino.ParserRunner.ParseResult;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {
    private AbstractCompiler compiler;
    private SourceFile sourceFile;
    private JsAst jsAst;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        sourceFile = mock(SourceFile.class);
        when(sourceFile.getName()).thenReturn("test.js");
        when(sourceFile.getCode()).thenReturn("test code".getBytes());
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        ParserConfig parserConfig = mock(ParserConfig.class);
        when(parserConfig.getSourceNameFactory()).thenReturn(new SourceName.SimpleFactory());

        AbstractCompiler compilerMock = mock(AbstractCompiler.class);
        when(compilerMock.getParserConfig()).thenReturn(parserConfig);

        Node expectedAst = IR.script();
        when(compilerMock.getDefaultErrorReporter()).thenReturn(err -> {});
        when(compilerMock.getLogger()).thenReturn(logger -> {});

        jsAst.parse(compilerMock);
        Node actualAst = jsAst.getAstRoot(compilerMock);

        assertNotNull(actualAst);
        Preconditions.checkState(expectedAst.equals(actualAst));
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        Node actualAst = jsAst.getAstRoot(compiler);

        assertNull(actualAst);
    }

    @Test
    public void testParseWithErrors() {
        ParserConfig parserConfig = mock(ParserConfig.class);
        when(parserConfig.getSourceNameFactory()).thenReturn(new SourceName.SimpleFactory());

        AbstractCompiler compilerMock = mock(AbstractCompiler.class);
        when(compilerMock.getParserConfig()).thenReturn(parserConfig);
        when(compilerMock.getDefaultErrorReporter()).thenReturn(err -> {});
        when(compilerMock.getLogger()).thenReturn(logger -> {});

        jsAst.parse(compilerMock);
        Node actualAst = jsAst.getAstRoot(compilerMock);

        assertNull(actualAst);
    }
}