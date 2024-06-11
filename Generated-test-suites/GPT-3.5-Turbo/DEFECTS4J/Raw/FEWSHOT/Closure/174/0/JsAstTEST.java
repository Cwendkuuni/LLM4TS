
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsAstTest {
    
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertEquals("var a = 1;", astRoot.toString());
    }
    
    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().id);
    }
    
    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals(sourceFile, jsAst.getSourceFile());
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 1;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("test2.js", "var b = 2;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}
