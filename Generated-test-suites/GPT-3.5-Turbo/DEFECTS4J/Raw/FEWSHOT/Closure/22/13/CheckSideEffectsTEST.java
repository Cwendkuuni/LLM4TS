package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckSideEffectsTest {
   
    @Test
    public void testProcess() {
        Compiler compiler = new Compiler();
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.process(externs, root);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testHotSwapScript() {
        Compiler compiler = new Compiler();
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.ROOT);
        
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.hotSwapScript(scriptRoot, originalRoot);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testVisit() {
        Compiler compiler = new Compiler();
        NodeTraversal traversal = new NodeTraversal(compiler, null);
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        Node grandParent = new Node(Token.EMPTY);
        parent.addChildToFront(n);
        grandParent.addChildToFront(parent);
        
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.visit(traversal, n, parent);
        
        // TODO: Add assertions
    }

    @Test
    public void testProtectSideEffects() {
        Compiler compiler = new Compiler();
        Node n = new Node(Token.EMPTY);
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.problemNodes.add(n);
        
        checker.protectSideEffects();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testAddExtern() {
        Compiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.addExtern();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testStripProtection() {
        Compiler compiler = new Compiler();
        NodeTraversal traversal = new NodeTraversal(compiler, null);
        Node callNode = new Node(Token.EMPTY);
        Node targetNode = new Node(Token.NAME, new Node(Token.EMPTY));
        
        callNode.addChildToFront(targetNode);
        
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.visit(traversal, callNode, null);
        
        // TODO: Add assertions
    }
}