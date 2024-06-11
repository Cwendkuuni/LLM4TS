package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
    
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        
        Node root = jsAst.getAstRoot(compiler);
        
        assertNotNull(root);
        assertEquals(IR.script(IR.var(IR.name("a"), IR.number(5))), root);
    }
    
    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        
        jsAst.clearAst();
        
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
        assertNull(sourceFile.getCachedSource());
    }
    
    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        
        InputId inputId = jsAst.getInputId();
        
        assertEquals(new InputId("test.js"), inputId);
    }
    
    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        
        SourceFile result = jsAst.getSourceFile();
        
        assertEquals(sourceFile, result);
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newFile = new SourceFile("new.js", "var b = 10;");
        
        jsAst.setSourceFile(newFile);
        
        assertEquals(newFile, jsAst.getSourceFile());
    }
    
    @Test
    public void testParse() {
        SourceFile sourceFile = new SourceFile("test.js", "var a = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        
        jsAst.parse(compiler);
        
        assertNotNull(jsAst.getAstRoot(compiler));
        assertFalse(compiler.hasHaltingErrors());
    }
}