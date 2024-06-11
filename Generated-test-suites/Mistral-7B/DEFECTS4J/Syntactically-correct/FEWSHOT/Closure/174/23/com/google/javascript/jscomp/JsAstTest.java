package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

    private AbstractCompiler compiler;
    private SourceFile sourceFile;
    private JsAst jsAst;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        sourceFile = Mockito.mock(SourceFile.class);
        when(sourceFile.getName()).thenReturn("test.js");
        when(sourceFile.getCode()).thenReturn("function main() {}".getBytes());
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        Node expectedAst = IR.script();
        when(compiler.getParserConfig()).thenReturn(new ParserConfig());
        when(compiler.getDefaultErrorReporter()).thenReturn(Mockito.mock(ErrorReporter.class));
        when(compiler.getLogger()).thenReturn(Mockito.mock(Logger.class));

        Node actualAst = jsAst.getAstRoot(compiler);

        assertEquals(expectedAst, actualAst);

        verify(compiler, times(1)).prepareAst(expectedAst);
    }

    @Test
    public void testClearAst() {
        Node root = IR.script();
        jsAst = new JsAst(sourceFile) {
            private Node originalAst = root;

            @Override
            public Node getAstRoot(AbstractCompiler compiler) {
                return root;
            }
        };

        jsAst.clearAst();

        assertNull(jsAst.root);
        assertEquals(originalAst, jsAst.sourceFile.getAst());
    }

    @Test
    public void testGetInputId() {
        InputId expectedInputId = new InputId("test.js");

        assertEquals(expectedInputId, jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile expectedSourceFile = sourceFile;

        assertEquals(expectedSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = Mockito.mock(SourceFile.class);

        jsAst.setSourceFile(newSourceFile);

        assertEquals(newSourceFile, jsAst.sourceFile);
        assertEquals(newSourceName, jsAst.inputId.getName());
    }

    @Test(expected = AssertionError.class)
    public void testSetSourceFileWithDifferentName() {
        SourceFile newSourceFile = Mockito.mock(SourceFile.class);
        when(newSourceFile.getName()).thenReturn("differentName.js");

        jsAst.setSourceFile(newSourceFile);

        fail();
    }

    @Test
    public void testParseWithSuccess() {
        Node expectedAst = IR.script();
        expectedAst.setStaticSourceFile(sourceFile);

        when(compiler.getParserConfig()).thenReturn(new ParserConfig());
        when(compiler.getDefaultErrorReporter()).thenReturn(Mockito.mock(ErrorReporter.class));
        when(compiler.getLogger()).thenReturn(Mockito.mock(Logger.class));

        jsAst.parse(compiler);

        assertEquals(expectedAst, jsAst.root);
        verify(compiler, times(1)).prepareAst(expectedAst);
    }

    @Test
    public void testParseWithError() {
        when(compiler.getParserConfig()).thenReturn(new ParserConfig());
        when(compiler.getDefaultErrorReporter()).thenReturn(Mockito.mock(ErrorReporter.class));
        when(compiler.getLogger()).thenReturn(Mockito.mock(Logger.class));

        jsAst.parse(compiler);

        // No errors were reported, so the parse tree should be empty.
        assertNull(jsAst.root);
    }

    @Test
    public void testParseWithIOException() {
        when(compiler.getParserConfig()).thenReturn(new ParserConfig());
        when(compiler.getDefaultErrorReporter()).thenReturn(Mockito.mock(ErrorReporter.class));
        when(compiler.getLogger()).thenReturn(Mockito.mock(Logger.class));

        doThrow(new IOException("Test error")).when(compiler).prepareAst(any(Node.class));

        jsAst.parse(compiler);

        // There was an IO error, so the parse tree should be empty.
        assertNull(jsAst.root);
    }
}