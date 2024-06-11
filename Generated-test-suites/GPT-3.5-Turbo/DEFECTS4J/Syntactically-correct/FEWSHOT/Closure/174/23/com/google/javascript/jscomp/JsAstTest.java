package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
    private JsAst jsAst;
    private AbstractCompiler compilerMock;
    private SourceFile sourceFileMock;
    private Node astRoot;
    private ParserRunner.ParseResult parseResultMock;

    @Before
    public void setUp() throws IOException {
        compilerMock = mock(AbstractCompiler.class);
        sourceFileMock = mock(SourceFile.class);
        parseResultMock = mock(ParserRunner.ParseResult.class);
        astRoot = mock(Node.class);

        when(compilerMock.getParserConfig()).thenReturn(mock(Parser.Config.class));
        when(sourceFileMock.getName()).thenReturn("input.js");
        when(sourceFileMock.getCode()).thenReturn("var x = 10;");
        when(parseResultMock.ast).thenReturn(astRoot);
        when(ParserRunner.parse(sourceFileMock, sourceFileMock.getCode(),
          compilerMock.getParserConfig(),
          compilerMock.getDefaultErrorReporter(),
          Logger.getLogger(JsAst.class.getName()))).thenReturn(parseResultMock);

        jsAst = new JsAst(sourceFileMock);
    }

    @Test
    public void testGetAstRoot() {
        Node root = jsAst.getAstRoot(compilerMock);
        assertEquals(astRoot, root);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compilerMock));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertEquals(new InputId("input.js"), inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertEquals(sourceFileMock, sourceFile);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = mock(SourceFile.class);
        when(newFile.getName()).thenReturn("newFile.js");
        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() throws IOException {
        jsAst.getAstRoot(compilerMock);
        assertEquals(astRoot, jsAst.getAstRoot(compilerMock));
    }
}