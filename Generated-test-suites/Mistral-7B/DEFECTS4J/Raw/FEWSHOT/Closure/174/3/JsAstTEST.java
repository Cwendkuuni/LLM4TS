package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;

public class JsAstTest {
    private SourceFile sourceFile;
    private Compiler compiler;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "test");
        compiler = new Compiler();
    }

    @Test
    public void testGetAstRoot() {
        JsAst jsAst = new JsAst(sourceFile);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.getAstRoot(compiler);
        jsAst.clearAst();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNull(astRoot);
    }

    @Test
    public void testParse() {
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertTrue(astRoot instanceof com.google.javascript.rhino.Script);
    }

    @Test
    public void testParseError() {
        sourceFile.setCode("function invalidSyntax() { // invalid syntax here }");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertTrue(astRoot instanceof com.google.javascript.rhino.Script);

        // Check that there is an error reported
        assertTrue(compiler.getErrors().size() > 0);
    }
}