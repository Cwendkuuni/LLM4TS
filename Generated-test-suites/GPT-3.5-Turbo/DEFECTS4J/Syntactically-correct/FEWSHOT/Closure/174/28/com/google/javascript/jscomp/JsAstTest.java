package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;

public class JsAstTest {
    private JsAst jsAst;
    private AbstractCompiler compiler;
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var a = 5;");
        jsAst = new JsAst(sourceFile);
        compiler = mock(AbstractCompiler.class);
    }

    @Test
    public void testGetAstRoot() {
        Node astRoot = jsAst.getAstRoot(compiler);

        // Verify that the root node is not null
        assertNotNull(astRoot);

        // Verify that the root node has the correct input ID
        assertEquals(sourceFile.getName(), astRoot.getInputId().getId());

        // Verify that the root node has the correct source file
        assertEquals(sourceFile, astRoot.getStaticSourceFile());
    }

    @Test
    public void testClearAst() {
        // Set the root node to a non-null value
        jsAst.getAstRoot(compiler);
        assertNotNull(jsAst.getAstRoot(compiler));

        // Clear the AST
        jsAst.clearAst();

        // Verify that the root node is null after clearing the AST
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testGetInputId() {
        // Verify that the input ID is not null
        assertNotNull(jsAst.getInputId());

        // Verify that the input ID has the correct value
        assertEquals("test.js", jsAst.getInputId().getId());
    }

    @Test
    public void testGetSourceFile() {
        // Verify that the source file is not null
        assertNotNull(jsAst.getSourceFile());

        // Verify that the source file has the correct name
        assertEquals("test.js", jsAst.getSourceFile().getName());

        // Verify that the source file has the correct code
        assertEquals("var a = 5;", jsAst.getSourceFile().getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("test_new.js", "var b = 10;");

        // Set the source file to a new source file
        jsAst.setSourceFile(newSourceFile);

        // Verify that the source file has been updated
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        // Parse the source file
        jsAst.parse(compiler);

        // Verify that the root node is not null
        assertNotNull(jsAst.getAstRoot(compiler));

        // Verify that the root node has the correct input ID
        assertEquals(sourceFile.getName(), jsAst.getAstRoot(compiler).getInputId().getId());

        // Verify that the root node has the correct source file
        assertEquals(sourceFile, jsAst.getAstRoot(compiler).getStaticSourceFile());
    }
}