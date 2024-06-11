package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;


public class JsAstTest {
    @Mock
    private AbstractCompiler compiler;
    
    private SourceFile sourceFile;
    private JsAst jsAst;
    
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        
        sourceFile = new SourceFile("test.js", "console.log('Hello, world!');");
        jsAst = new JsAst(sourceFile);
    }
    
    @Test
    public void testConstruction() {
        assertNotNull(jsAst.getInputId());
        assertNotNull(jsAst.getSourceFile());
        assertEquals("test.js", jsAst.getInputId().getIdName());
        assertEquals(sourceFile, jsAst.getSourceFile());
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("new_test.js", "console.log('New test!');");
        jsAst.setSourceFile(newSourceFile);
        
        assertNotNull(jsAst.getSourceFile());
        assertEquals("new_test.js", jsAst.getInputId().getIdName());
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
    
    @Test
    public void testClearAst() {
        jsAst.clearAst();
        
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testGetAstRoot() {
        Node root = jsAst.getAstRoot(compiler);
        
        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken().toString());
        assertEquals(1, root.getChildCount());
        assertEquals("console.log('Hello, world!');", root.getFirstChild().toStringTree());
    }

    @Test
    public void testParse_withParseError() throws IOException {
        AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
        
        SourceFile parseErrorSourceFile = new SourceFile("parse_error.js", "console.");
        JsAst parseErrorAst = new JsAst(parseErrorSourceFile);
        
        parseErrorAst.parse(mockCompiler);
        
        Node root = parseErrorAst.getAstRoot(mockCompiler);
        
        assertNotNull(root);
        assertEquals("SCRIPT", root.getToken().toString());
        assertEquals(1, root.getChildCount());
        assertEquals("", root.getFirstChild().toStringTree());
    }
}