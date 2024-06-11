package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
    
    @Test
    public void testProcess() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        pass.process(new Node(null), new Node(null));
    }
    
    @Test
    public void testEliminationPass() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = pass.new EliminationPass(new ControlFlowGraph<Node>());
        
        eliminationPass.visit(new NodeTraversal(), new Node(null), new Node(null));
    }
    
    @Test
    public void testTryRemoveUnconditionalBranching() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = pass.new EliminationPass(new ControlFlowGraph<Node>());
        
        eliminationPass.tryRemoveUnconditionalBranching(new Node(Token.BREAK));
        eliminationPass.tryRemoveUnconditionalBranching(new Node(Token.RETURN));
        eliminationPass.tryRemoveUnconditionalBranching(new Node(Token.CONTINUE));
    }
    
    @Test
    public void testComputeFollowing() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = pass.new EliminationPass(new ControlFlowGraph<Node>());
        
        Node node = new Node(null);
        assertEquals(null, eliminationPass.computeFollowing(node));
    }
    
    @Test
    public void testRemoveDeadExprStatementSafely() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = pass.new EliminationPass(new ControlFlowGraph<Node>());
        
        Node node = new Node(Token.BLOCK);
        eliminationPass.removeDeadExprStatementSafely(node);
    }
    
    @Test
    public void testRemoveNode() {
        UnreachableCodeElimination pass = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = pass.new EliminationPass(new ControlFlowGraph<Node>());
        
        Node node = new Node(Token.BLOCK);
        eliminationPass.removeNode(node);
    }
}