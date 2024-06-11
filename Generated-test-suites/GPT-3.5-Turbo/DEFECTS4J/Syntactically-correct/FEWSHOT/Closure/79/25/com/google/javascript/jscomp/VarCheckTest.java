package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
    
    private VarCheck varCheck;
    private AbstractCompiler compiler;
    private CompilerInput synthesizedExternsInput;
    private Node synthesizedExternsRoot;
    
    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        varCheck = new VarCheck(compiler);
        synthesizedExternsInput = null;
        synthesizedExternsRoot = null;
    }
    
    @Test
    public void testProcess() {
        // Test your code here
    }
    
    @Test
    public void testVisit() {
        // Test your code here
    }
    
    @Test
    public void testNameRefInExternsCheck() {
        // Test your code here
    }
    
    @Test
    public void testCreateSynthesizedExternVar() {
        // Test your code here
    }
    
    @Test
    public void testGetSynthesizedExternsInput() {
        // Test your code here
    }
    
    @Test
    public void testGetSynthesizedExternsRoot() {
        // Test your code here
    }
}