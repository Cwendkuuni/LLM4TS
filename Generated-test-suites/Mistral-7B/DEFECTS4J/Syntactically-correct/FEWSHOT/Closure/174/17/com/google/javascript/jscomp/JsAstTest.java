package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testutils.AbstractCompilerForTests;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {
    @Test
    public void testJsAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js", 1, null);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompilerForTests();

        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js", 1, null);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompilerForTests();

        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);

        jsAst.clearAst();
        Node newAstRoot = jsAst.getAstRoot(compiler);
        assertNull(newAstRoot);
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js", 1, null);
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();
        SourceName expectedInputId = new SourceName("test.js");
        assertNotNull(inputId);
        assertNotNull(inputId.getName());
        assertNotNull(inputId.getSourceName());
        assertEquals(expectedInputId.getName(), inputId.getName());
        assertEquals(expectedInputId, inputId.getSourceName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js", 1, null);
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile expectedSourceFile = sourceFile;
        SourceFile sourceFileFromJsAst = jsAst.getSourceFile();

        assertNotNull(sourceFileFromJsAst);
        assertEquals(expectedSourceFile, sourceFileFromJsAst);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile originalSourceFile = new SourceFile("test.js", "test.js", 1, null);
        JsAst jsAst = new JsAst(originalSourceFile);

        SourceFile newSourceFile = new SourceFile("newTest.js", "newTest.js", 1, null);
        jsAst.setSourceFile(newSourceFile);

        SourceFile expectedSourceFile = newSourceFile;
        SourceFile sourceFileFromJsAst = jsAst.getSourceFile();

        assertNotNull(sourceFileFromJsAst);
        assertEquals(expectedSourceFile, sourceFileFromJsAst);

        String originalFileName = originalSourceFile.getName();
        String fileNameFromJsAst = jsAst.getFileName();

        assertNotNull(fileNameFromJsAst);
        assertEquals(originalFileName, fileNameFromJsAst);
    }
}