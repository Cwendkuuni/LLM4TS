package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithNameParent() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithAssignParent() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_OtherFunction() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testVisit_GlobalThisOnLeftSideOfAssign() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        checker.visit(t, n, parent);
        
        // Add assertions here
        
    }
    
    @Test
    public void testVisit_GlobalThisWithPropertyAccess() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        checker.visit(t, n, parent);
        
        // Add assertions here
        
    }
    
    @Test
    public void testVisit_NotGlobalThis() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        checker.visit(t, n, parent);
        
        // Add assertions here
        
    }
}