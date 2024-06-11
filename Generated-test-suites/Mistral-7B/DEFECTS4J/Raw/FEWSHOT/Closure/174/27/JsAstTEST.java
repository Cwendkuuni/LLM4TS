package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new Compiler();
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        AbstractCompiler compiler = new Compiler();
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.getAstRoot(compiler); // parse the file
        jsAst.clearAst();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNull(astRoot);
    }

    @Test
    public void testParse() {
        AbstractCompiler compiler = new Compiler();
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        assertTrue(jsAst.getAstRoot(compiler) != null);
    }

    @Test
    public void testParseWithErrors() {
        AbstractCompiler compiler = new Compiler();
        SourceFile sourceFile = new SourceFile("test.js", "test code with errors");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        // Assume a hypothetical pass that relies on parse tree being null for errors
        assertTrue(astRoot == null || compiler.hasHaltingErrors());
    }
}