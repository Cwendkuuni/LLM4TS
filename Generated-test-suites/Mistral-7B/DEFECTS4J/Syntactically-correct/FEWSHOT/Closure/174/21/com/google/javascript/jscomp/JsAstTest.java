package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new CompilerPassManager();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js");
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new CompilerPassManager()));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "test.js");
        JsAst jsAst = new JsAst(sourceFile);
        assertNotNull(jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test1.js", "test1.js");
        JsAst jsAst = new JsAst(null);
        jsAst.setSourceFile(sourceFile1);
        checkState(jsAst.getSourceFile().getName().equals("test1.js"));
    }

    @Test
    public void testParseWithNullSourceFile() {
        JsAst jsAst = new JsAst(null);
        AbstractCompiler compiler = new CompilerPassManager();
        jsAst.parse(compiler);
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithEmptySourceCode() {
        SourceFile sourceFile = new SourceFile("test.js", "");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new CompilerPassManager();
        jsAst.parse(compiler);
        assertNotNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithError() {
        String sourceCode = "function() { throw new Error('Test error'); }";
        SourceFile sourceFile = new SourceFile("test.js", sourceCode);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new CompilerPassManager();
        jsAst.parse(compiler);
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testPrepareAst() {
        String sourceCode = "function foo() { return 1; }";
        SourceFile sourceFile = new SourceFile("test.js", sourceCode);
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new CompilerPassManager();
        jsAst.parse(compiler);
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        // Assuming that prepareAst sets the source name to the source file name.
        SourceName sourceName = astRoot.getSourceName();
        assertNotNull(sourceName);
        assertNotNull(sourceName.getSourceNameString());
        assertNotNull(sourceName.getModuleName());
        assertEquals(sourceFile.getName(), sourceName.getSourceNameString());
        assertEquals(sourceFile.getName(), sourceName.getModuleName());
    }
}