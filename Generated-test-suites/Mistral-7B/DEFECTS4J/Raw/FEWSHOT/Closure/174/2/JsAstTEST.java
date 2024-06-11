package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsAstTest {
    @Test
    public void testJsAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(null);
        assertNotNull(root);
        assertEquals("test content", sourceFile.getCode());
    }

    @Test
    public void testParseSuccess() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null);
        Node root = jsAst.getAstRoot(null);
        assertNotNull(root);
    }

    @Test
    public void testParseFailure() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null);
        Node root = jsAst.getAstRoot(null);
        assertNotNull(root);
        // In real world scenario, you would check for specific error message
        assertEquals(IR.script(), root);
    }

    @Test
    public void testParseWithErrors() {
        String code = "function foo() {\n" +
                "  x = 5;\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js", code);
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null);
        Node root = jsAst.getAstRoot(null);
        assertNotNull(root);
        // In real world scenario, you would check for specific error message
        assertEquals(IR.script(), root);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test content");
        JsAst jsAst = new JsAst(sourceFile);
        Node root = jsAst.getAstRoot(null);
        jsAst.clearAst();
        Node newRoot = jsAst.getAstRoot(null);
        assertNull(newRoot);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test1.js", "test1 content");
        JsAst jsAst = new JsAst(sourceFile1);
        jsAst.setSourceFile(new SourceFile("test2.js", "test2 content"));
        assertEquals("test2.js", jsAst.getSourceFile().getName());
    }
}