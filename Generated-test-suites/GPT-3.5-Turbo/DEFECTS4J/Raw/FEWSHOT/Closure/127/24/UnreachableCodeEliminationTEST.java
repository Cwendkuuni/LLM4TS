package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnreachableCodeEliminationTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination codeElimination = new UnreachableCodeElimination(compiler, true);

        Node externs = null;
        Node toplevel = null;

        codeElimination.process(externs, toplevel);

        // Assert code changes
        assertTrue(codeElimination.codeChanged);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler, true).new EliminationPass();

        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        // Test case when parent is null
        eliminationPass.visit(t, n, parent);

        // Test case when n is a function
        parent = new Node.Builder().setType(Token.FUNCTION).build();
        eliminationPass.visit(t, n, parent);
        parent = null;

        // Test case when n is a script
        parent = new Node.Builder().setType(Token.SCRIPT).build();
        eliminationPass.visit(t, n, parent);
        parent = null;

        // Test case when gNode is null
        eliminationPass.visit(t, n, parent);

        // Test case when gNode is not null and removes dead expression statement
        DiGraphNode<Node, Branch> gNode = new DiGraphNode<>();
        eliminationPass.visit(t, n, parent);
        
        // Assert code changes
        assertTrue(eliminationPass.codeChanged);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler, true).new EliminationPass();

        Node n = null;

        // Test case when n is null
        eliminationPass.tryRemoveUnconditionalBranching(n);

        // Test case when gNode is null
        eliminationPass.tryRemoveUnconditionalBranching(n);

        // Test case when n is Token.RETURN and has children
        n = new Node.Builder().setType(Token.RETURN).setFirstChild(new Node.Builder().build()).setNext(new Node.Builder().build()).build();
        eliminationPass.tryRemoveUnconditionalBranching(n);
        n = null;

        // Test case when n is Token.BREAK and has only one out edge which should be removed
        n = new Node.Builder().setType(Token.BREAK).build();
        DiGraphNode<Node, Branch> gNodeBreak = new DiGraphNode<>();
        eliminationPass.tryRemoveUnconditionalBranching(n);
        n = null;

        // Assert code changes
        assertTrue(eliminationPass.codeChanged);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler, true).new EliminationPass();

        Node n = null;

        // Test case when n is null
        assertNull(eliminationPass.computeFollowing(n));

        // Test case when next is not null and is not a block
        Block block = new Block();
        Node next = new Node();
        next.putProp(Node.BLOCK_NODE, block);
        n = new Node.Builder().build();
        Node result = eliminationPass.computeFollowing(n);
        assertEquals(next, result);

        // Test case when next is null
        next = null;
        result = eliminationPass.computeFollowing(n);
        assertNull(result);

        // Test case when next is block and has children
        Node nextChild = new Node.Builder().build();
        next = new Node.Builder().setFirstChild(nextChild).build();
        result = eliminationPass.computeFollowing(next);
        assertEquals(nextChild, result);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler, true).new EliminationPass();

        Node n = null;

        // Test case when n is empty
        eliminationPass.removeDeadExprStatementSafely(n);

        // Test case when n is block and has no children
        n = new Node.Builder().setType(Token.BLOCK).build();
        eliminationPass.removeDeadExprStatementSafely(n);
        n = null;

        // Test case when n is block and parent is try/catch
        n = new Node.Builder().setType(Token.BLOCK).build();
        Node parent = new Node.Builder().setType(Token.TRY).setFirstChild(new Node.Builder().setType(Token.CATCH).build()).build();
        n.putProp(Node.TRY_NODE_PROP, parent);
        eliminationPass.removeDeadExprStatementSafely(n);
        n = null;

        // Test case when n is catch and parent is try
        Node tryNode = new Node.Builder().setType(Token.TRY).build();
        parent = new Node.Builder().setType(Token.CATCH).setParent(tryNode).build();
        n.putProp(Node.TRY_NODE_PROP, parent);
        eliminationPass.removeDeadExprStatementSafely(n);
        n = null;

        // Test case when n is var and has no children
        n = new Node.Builder().setType(Token.VAR).build();
        eliminationPass.removeDeadExprStatementSafely(n);
        n = null;

        // Assert code changes
        assertTrue(eliminationPass.codeChanged);
    }

    @Test
    public void testRemoveNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler, true).new EliminationPass();

        Node n = new Node.Builder().build();
        Node parent = new Node.Builder().build();

        eliminationPass.removeNode(n);

        // Assert code changes
        assertTrue(eliminationPass.codeChanged);
    }
}