
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
    private JsAst jsAst;
    private SourceFile sourceFile;
    
    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var a = 5;");
        jsAst = new JsAst(sourceFile);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(jsAst.getInputId());
        assertEquals(sourceFile, jsAst.getSourceFile());
        assertEquals("test.js", jsAst.getSourceFile().getName());
    }
    
    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new DefaultCompiler();
        Node root = jsAst.getAstRoot(compiler);
        
        assertNotNull(root);
        assertEquals(IR.script().toStringTree(), root.toStringTree());
    }
    
    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new DefaultCompiler()));
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("newTest.js", "var b = 10;");
        jsAst.setSourceFile(newFile);
        
        assertEquals(newFile, jsAst.getSourceFile());
        assertEquals("newTest.js", jsAst.getSourceFile().getName());
    }
    
    @Test
    public void testParse() {
        AbstractCompiler compiler = new DefaultCompiler();
        jsAst.getAstRoot(compiler);
        
        assertNotNull(jsAst.getAstRoot(compiler));
        assertEquals("SCRIPT\n  VAR\n    NAME a\n    NUMBER 5\n", jsAst.getAstRoot(compiler).toStringTree());
    }
}
