package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsAstTest {
    @Test
    public void testJsAstInstance() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst);
    }

    @Test
    public void testGetAstRootNull() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetAstRootNotNull() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node root = jsAst.getAstRoot(compiler);
        assertTrue(root != null);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(new AbstractCompiler());
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(sourceFile.getName(), jsAst.getInputId().getName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile oldSourceFile = new SourceFile("oldTest.js", "old test content");
        SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
        JsAst jsAst = new JsAst(oldSourceFile);
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParseNullCompiler() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null);
    }

    @Test
    public void testParseSuccess() {
        String testContent = "function test() {\n" +
                "  var x = 1;\n" +
                "}";

        SourceFile sourceFile = new SourceFile("test.js", testContent);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node root = jsAst.getAstRoot(compiler);
        assertTrue(root != null);
    }

    @Test
    public void testParseFailure() {
        String testContent = "function test() {\n" +
                "  var x = 1;\n" +
                "  var y = { x: 1, y: 2 }; // SyntaxError: Unexpected token {";

        SourceFile sourceFile = new SourceFile("test.js", testContent);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node root = jsAst.getAstRoot(compiler);
        assertTrue(root == null);
    }
}