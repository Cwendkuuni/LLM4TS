package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {
    
    private CheckSideEffects checkSideEffects;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        checkSideEffects.process(externs, root);
        
        // assertion statements
    }
    
    @Test
    public void testHotSwapScript() {
        Node scriptRoot = new Node(Token.EXPR_RESULT);
        Node originalRoot = new Node(Token.EXPR_RESULT);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        
        // assertion statements
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.EXPR_RESULT);
        checkSideEffects.visit(t, n, parent);
        
        // assertion statements
    }
    
    @Test
    public void testProtectSideEffects() {
        // set up test case
        
        // call method
        
        // assertion statements
    }
    
    @Test
    public void testAddExtern() {
        // set up test case
        
        // call method
        
        // assertion statements
    }
    
    @Test
    public void testStripProtection() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.process(externs, root);
        
        // assertion statements
    }  
}