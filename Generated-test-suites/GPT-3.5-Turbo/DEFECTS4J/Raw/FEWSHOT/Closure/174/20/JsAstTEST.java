package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;


public class JsAstTest {
    private JsAst jsAst;

    @BeforeClass
    public static void setUpBeforeClass() {
        // Set up any necessary objects
    }

    @Test
    public void testJsAstConstructor() {
        SourceFile sourceFile = new SourceFile("filename.js");
        jsAst = new JsAst(sourceFile);
        
        assertNotNull(jsAst);
        assertNotNull(jsAst.getInputId());
        assertNotNull(jsAst.getSourceFile());
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("filename.js");
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst = new JsAst(sourceFile);
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("filename.js");
        jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }
    
    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("filename.js");
        jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        
        assertNotNull(inputId);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("filename.js");
        SourceFile newSourceFile = new SourceFile("newfilename.js");
        jsAst = new JsAst(sourceFile);
        jsAst.setSourceFile(newSourceFile);
        
        assertNotNull(jsAst.getSourceFile());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetSourceFileWithInvalidName() {
        SourceFile sourceFile = new SourceFile("filename.js");
        SourceFile newSourceFile = new SourceFile("newfilename.js");
        jsAst = new JsAst(sourceFile);
        jsAst.setSourceFile(newSourceFile);
        jsAst.setSourceFile(sourceFile);
    }

    @Test
    public void testParse() {
        SourceFile sourceFile = new SourceFile("filename.js");
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);
        
        assertNotNull(jsAst.getAstRoot(compiler));
        assertNull(compiler.hasHaltingErrors());
    }
    
    @Test
    public void testParseWithRootNull() {
        SourceFile sourceFile = new SourceFile("filename.js");
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst = new JsAst(sourceFile);
        jsAst.setSourceFile(new SourceFile("emptyfile.js"));
        jsAst.parse(compiler);
        
        assertNotNull(jsAst.getAstRoot(compiler));
        assertNull(compiler.hasHaltingErrors());
    }
    
    @Test
    public void testParseWithIOException() {
        SourceFile sourceFile = new SourceFile("filename.js");
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst = new JsAst(sourceFile);
        jsAst.setSourceFile(new SourceFile("nonexistentfile.js"));
        jsAst.parse(compiler);
        
        assertNotNull(jsAst.getAstRoot(compiler));
        assertNotNull(compiler.hasHaltingErrors());
    }
}