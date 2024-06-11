package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);

        assertNull(jsAst.getAstRoot(null));

        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));

        Node rootNode = new Node();
        jsAst.clearAst();
        compiler.setOldParseTree(sourceFile.getName(), rootNode);
        assertEquals(rootNode, jsAst.getAstRoot(compiler));
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
        InputId inputId = new InputId(sourceFile.getName());

        assertEquals(inputId, jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newFile = mock(SourceFile.class);

        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        SourceFile sourceFile = mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = mock(AbstractCompiler.class);

        jsAst.clearAst();
        jsAst.parse(compiler);
        assertEquals(IR.script(), jsAst.getAstRoot(compiler));
    }
}