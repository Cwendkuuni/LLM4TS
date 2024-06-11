package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    private UnreachableCodeElimination eliminator;

    @Before
    public void setUp() {
        eliminator = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }

    @Test
    public void testEnterScope() {
        // TODO: Write test cases for enterScope method
    }

    @Test
    public void testExitScope() {
        // TODO: Write test cases for exitScope method
    }

    @Test
    public void testProcess() {
        // TODO: Write test cases for process method
    }

    @Test
    public void testVisit() {
        // TODO: Write test cases for visit method
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node node = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.addChildToFront(new Node(Token.RETURN));
        assertTrue(eliminator.tryRemoveUnconditionalBranching(node) == null);
    }

    @Test
    public void testComputeFollowing() {
        Node node = new Node(Token.BLOCK);
        assertTrue(eliminator.computeFollowing(node) == null);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // TODO: Write test cases for removeDeadExprStatementSafely method
    }
}