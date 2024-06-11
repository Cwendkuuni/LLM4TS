package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {
    private static final CheckSideEffects.CheckLevel level = CheckSideEffects.CheckLevel.WARNING;
    private static final boolean protectSideEffectFreeCode = true;
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = IR.empty();
        Node root = IR.root();
        
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.process(externs, root);
        
        // Assert statements here
        
        assertTrue(true);
    }
    
    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node scriptRoot = IR.script();
        Node originalRoot = IR.root();
        
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        
        // Assert statements here
        
        assertTrue(true);
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = IR.empty();
        Node parent = IR.empty();
        
        CheckSideEffects checkSideEffects = new CheckSideEffects(new AbstractCompiler(), level, protectSideEffectFreeCode);
        checkSideEffects.visit(t, n, parent);
        
        // Assert statements here
        
        assertTrue(true);
    }
    
    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.protectSideEffects();
        
        // Assert statements here
        
        assertTrue(true);
    }
    
    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.addExtern();
        
        // Assert statements here
        
        assertTrue(true);
    }
    
    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = IR.empty();
        Node root = IR.root();
        
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.process(externs, root);
        
        // Assert statements here
        
        assertTrue(true);
    }
}