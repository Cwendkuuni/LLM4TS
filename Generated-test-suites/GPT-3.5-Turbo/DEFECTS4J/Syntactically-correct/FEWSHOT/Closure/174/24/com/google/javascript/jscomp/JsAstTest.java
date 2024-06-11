package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        // Test case for null root
        SourceFile sourceFile1 = new SourceFile("file1.js", "var a = 1;");
        JsAst jsAst1 = new JsAst(sourceFile1);
        assertNull(jsAst1.getAstRoot(null));

        // Test case for non-null root
        SourceFile sourceFile2 = new SourceFile("file2.js", "function foo() {}");
        JsAst jsAst2 = new JsAst(sourceFile2);
        Node root = jsAst2.getAstRoot(null);
        assertEquals("FUNCTION", root.getToken().toString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("file.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("file.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertEquals("file.js", inputId.getId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("file.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("file1.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("file2.js", "function foo() {}");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}