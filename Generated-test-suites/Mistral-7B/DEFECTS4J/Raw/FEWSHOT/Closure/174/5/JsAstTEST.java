package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler(); // replace with a valid AbstractCompiler instance
        Node root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertEquals(sourceFile.getName(), inputId.getName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile oldSourceFile = new SourceFile("test.js", "test code");
        SourceFile newSourceFile = new SourceFile("newTest.js", "new test code");
        JsAst jsAst = new JsAst(oldSourceFile);
        jsAst.setSourceFile(newSourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(newSourceFile, result);
    }

    @Test
    public void testParseWithNullCompiler() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(null); // compiler should not be null
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testParseWithNullSourceFile() {
        JsAst jsAst = new JsAst(null); // sourceFile should not be null
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testParseWithIOException() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler(); // replace with a valid AbstractCompiler instance
        compiler.report = (message, line, column) -> {}; // replace with a stub reporter
        compiler.setOldParseTree("test.js", null); // replace with a stub old parse tree
        try {
            jsAst.parse(compiler);
        } catch (IOException e) {
            // expected
        }
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithNullAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler(); // replace with a valid AbstractCompiler instance
        ParserRunner.ParseResult result = new ParserRunner.ParseResult(IR.script(), null); // replace with a valid result
        try {
            jsAst.parse(compiler);
            compiler.setOldParseTree(sourceFile.getName(), null);
        } catch (IOException e) {
            // expected
        }
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithValidAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler(); // replace with a valid AbstractCompiler instance
        ParserRunner.ParseResult result = new ParserRunner.ParseResult(IR.script(), null); // replace with a valid result
        try {
            jsAst.parse(compiler);
            compiler.setOldParseTree(sourceFile.getName(), null);
            Node root = jsAst.getAstRoot(compiler);
            assertNotNull(root);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSetStaticSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test code");
        Node root = IR.script();
        JsAst jsAst = new JsAst(sourceFile) {
            @Override
            public void setStaticSourceFile(SourceFile file) {
                super.setStaticSourceFile(file);
                this.root = root;
            }
        };
        jsAst.setStaticSourceFile(sourceFile);
        assertEquals(sourceFile, jsAst.getSourceFile());
        assertEquals(root, jsAst.getAstRoot(new AbstractCompiler()).getFirstChild());
    }
}