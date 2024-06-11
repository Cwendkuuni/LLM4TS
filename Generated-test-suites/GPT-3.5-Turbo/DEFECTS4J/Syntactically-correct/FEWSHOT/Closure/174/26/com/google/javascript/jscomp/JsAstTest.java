package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Get the AstRoot using getAstRoot method
        Node astRoot = jsAst.getAstRoot(compiler);
        
        // Assert that the astRoot is not null
        assertNotNull(astRoot);
    }
    
    @Test
    public void testClearAst() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Call the clearAst method
        jsAst.clearAst();
        
        // Assert that the root is null
        assertNull(jsAst.root);
        
        // Assert that the source file's cached source is cleared
        verify(sourceFile).clearCachedSource();
    }
    
    @Test
    public void testGetInputId() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Get the InputId using getInputId method
        InputId inputId = jsAst.getInputId();
        
        // Assert that the inputId is not null
        assertNotNull(inputId);
    }
    
    @Test
    public void testGetSourceFile() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Get the SourceFile using getSourceFile method
        SourceFile result = jsAst.getSourceFile();
        
        // Assert that the result is the same as the original sourceFile
        assertEquals(sourceFile, result);
    }
    
    @Test
    public void testSetSourceFile() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Create a new SourceFile
        SourceFile newSourceFile = mock(SourceFile.class);
        
        // Set the new SourceFile using setSourceFile method
        jsAst.setSourceFile(newSourceFile);
        
        // Assert that the sourceFile variable in JsAst is updated to the new SourceFile
        assertEquals(newSourceFile, jsAst.sourceFile);
    }
    
    @Test
    public void testParse() {
        // Create a mock SourceFile
        SourceFile sourceFile = mock(SourceFile.class);
        
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        
        // Create a JsAst object
        JsAst jsAst = new JsAst(sourceFile);
        
        // Call the parse method
        jsAst.parse(compiler);
        
        // Assert that the root is not null
        assertNotNull(jsAst.root);
        
        // Assert that the prepareAst method is called on the compiler with the root as the argument
        verify(compiler).prepareAst(jsAst.root);
        
        // Assert that the staticSourceFile of the root is set to the sourceFile
        assertEquals(sourceFile, jsAst.root.getStaticSourceFile());
    }
}