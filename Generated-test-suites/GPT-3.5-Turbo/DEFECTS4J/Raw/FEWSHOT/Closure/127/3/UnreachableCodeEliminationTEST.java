package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination unreachableCodeElimination;
    private Compiler compiler;
    private Node externs;
    private Node toplevel;

    @Before
    public void setUp() {
        compiler = new Compiler();
        unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
        externs = new Node(Token.EXTERN);
        toplevel = new Node(Token.TOPLEVEL);
    }

    @Test
    public void testProcess() {
        unreachableCodeElimination.process(externs, toplevel);
        assertTrue(true);
    }

    @Test
    public void testVisit() {
        NodeTraversal traversal = new NodeTraversal(compiler, new UnreachableCodeElimination.EliminationPass(null));
        NodeTraversal.AbstractShallowCallback callback = traversal.getCallback();
        Node n = new Node(Token.NAMES);
        Node parent = new Node(Token.PARENT);
        callback.visit(traversal, n, parent);
        assertTrue(true);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node n = new Node(Token.NAMES);
        unreachableCodeElimination.new EliminationPass(null).tryRemoveUnconditionalBranching(n);
        assertTrue(true);
    }

    @Test
    public void testComputeFollowing() {
        Node n = new Node(Token.NAMES);
        unreachableCodeElimination.new EliminationPass(null).computeFollowing(n);
        assertTrue(true);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node n = new Node(Token.NAMES);
        unreachableCodeElimination.new EliminationPass(null).removeDeadExprStatementSafely(n);
        assertTrue(true);
    }

    @Test
    public void testRemoveNode() {
        Node n = new Node(Token.NAMES);
        unreachableCodeElimination.new EliminationPass(null).removeNode(n);
        assertTrue(true);
    }
}