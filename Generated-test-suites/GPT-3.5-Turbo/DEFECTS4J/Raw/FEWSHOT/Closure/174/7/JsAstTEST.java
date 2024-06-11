package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {
    private JsAst jsAst;
    private AbstractCompiler abstractCompilerMock;
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = mock(SourceFile.class);
        jsAst = new JsAst(sourceFile);
        abstractCompilerMock = mock(AbstractCompiler.class);
    }

    @Test
    public void testGetAstRootWhenRootIsNull() {
        jsAst.getAstRoot(abstractCompilerMock);

        verify(abstractCompilerMock).setOldParseTree(eq(sourceFile.getName()), any(Node.class));
        verify(sourceFile).clearCachedSource();
        assertNull(jsAst.getAstRoot(abstractCompilerMock));
    }

    @Test
    public void testGetAstRootWhenRootIsNotNull() {
        Node root = IR.script();
        jsAst.setRoot(root);

        assertEquals(root, jsAst.getAstRoot(abstractCompilerMock));
        verifyZeroInteractions(abstractCompilerMock);
        verifyZeroInteractions(sourceFile);
    }

    @Test
    public void testClearAst() {
        jsAst.setRoot(IR.script());
        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(abstractCompilerMock));
        verify(sourceFile).clearCachedSource();
    }

    @Test
    public void testGetInputId() {
        InputId inputId = new InputId(sourceFile.getName());
        assertEquals(inputId, jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = mock(SourceFile.class);
        when(newSourceFile.getName()).thenReturn(sourceFile.getName());

        jsAst.setSourceFile(newSourceFile);

        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParseWhenRootIsNullAndNoErrors() throws IOException {
        ParserRunner.ParseResult parseResult = mock(ParserRunner.ParseResult.class);
        parseResult.ast = IR.script();
        when(ParserRunner.parse(eq(sourceFile), anyString(), any(Parser.Config.class), any(ErrorReporter.class), any(Logger.class))).
                thenReturn(parseResult);

        jsAst.parse(abstractCompilerMock);

        assertEquals(parseResult.ast, jsAst.getAstRoot(abstractCompilerMock));
        verify(abstractCompilerMock).setOldParseTree(eq(sourceFile.getName()), eq(parseResult.oldAst));
        verify(sourceFile).clearCachedSource();
        verify(abstractCompilerMock).prepareAst(eq(parseResult.ast));
        verify(parseResult.ast).setStaticSourceFile(eq(sourceFile));
    }

    @Test
    public void testParseWhenRootIsNullAndHasErrors() throws IOException {
        ParserRunner.ParseResult parseResult = mock(ParserRunner.ParseResult.class);
        parseResult.ast = IR.script();
        when(abstractCompilerMock.hasHaltingErrors()).thenReturn(true);
        when(ParserRunner.parse(eq(sourceFile), anyString(), any(Parser.Config.class), any(ErrorReporter.class), any(Logger.class))).
                thenReturn(parseResult);

        jsAst.parse(abstractCompilerMock);

        assertEquals(parseResult.ast, jsAst.getAstRoot(abstractCompilerMock));
        verify(abstractCompilerMock).setOldParseTree(eq(sourceFile.getName()), eq(parseResult.oldAst));
        verify(sourceFile).clearCachedSource();
        verifyZeroInteractions(abstractCompilerMock);
        verifyZeroInteractions(parseResult.ast);
    }

    @Test
    public void testParseWhenRootIsNotNullAndHasErrors() throws IOException {
        Node root = IR.script();
        jsAst.setRoot(root);
        when(abstractCompilerMock.hasHaltingErrors()).thenReturn(true);

        jsAst.parse(abstractCompilerMock);

        assertEquals(root, jsAst.getAstRoot(abstractCompilerMock));
        verifyZeroInteractions(abstractCompilerMock);
        verifyZeroInteractions(sourceFile);
    }
}