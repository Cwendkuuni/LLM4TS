package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.Node;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
    
    private JsAst jsAst;
    
    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js");
        jsAst = new JsAst(sourceFile);
    }
    
    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        // Assert that the AST root is not null
        assertNotNull(astRoot);
        
        // Assert that the AST root input ID matches the source file
        assertEquals(jsAst.getInputId(), astRoot.getInputId());
    }
    
    @Test
    public void testClearAst() {
        jsAst.clearAst();
        
        // Assert that the AST root is null after clearing
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
        
        // Assert that the cached source is cleared
        assertNull(jsAst.getSourceFile().getCachedSource());
    }
    
    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        
        // Assert that the input ID is not null
        assertNotNull(inputId);
        
        // Assert that the input ID matches the source file name
        assertEquals(jsAst.getSourceFile().getName(), inputId.getIdName());
    }
    
    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        
        // Assert that the source file is not null
        assertNotNull(sourceFile);
        
        // Assert that the source file matches the one passed in the constructor
        assertEquals("test.js", sourceFile.getName());
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("new.js");
        jsAst.setSourceFile(newSourceFile);
        
        // Assert that the source file is updated
        assertEquals("new.js", jsAst.getSourceFile().getName());
    }
    
    @Test
    public void testParse() throws IOException {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.parse(compiler);
        
        // Assert that the AST root is not null after parsing
        assertNotNull(jsAst.getAstRoot(compiler));
        
        // Assert that the AST root input ID matches the source file
        assertEquals(jsAst.getInputId(), jsAst.getAstRoot(compiler).getInputId());
        
        // Assert that the source name is set in the AST root
        assertEquals(jsAst.getSourceFile(), jsAst.getAstRoot(compiler).getStaticSourceFile());
    }
}