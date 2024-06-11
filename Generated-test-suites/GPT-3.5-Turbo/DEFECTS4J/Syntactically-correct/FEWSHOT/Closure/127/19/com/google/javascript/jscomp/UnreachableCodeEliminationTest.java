package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    private UnreachableCodeElimination elimination;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN);
        Node toplevel = new Node(Token.TOP_LEVEL);
        elimination.process(externs, toplevel);
        // Assert something here
    }

    @Test
    public void testEliminationPassVisit() {
        NodeTraversal traversal = new NodeTraversal(null, null);
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        elimination.new EliminationPass(null).visit(traversal, n, parent);
        // Assert something here
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node n = new Node(Token.RETURN);
        elimination.new EliminationPass(null).tryRemoveUnconditionalBranching(n);
        // Assert something here
    }

    @Test
    public void testComputeFollowing() {
        Node n = new Node(Token.IF);
        Node result = elimination.new EliminationPass(null).computeFollowing(n);
        // Assert something here
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node n = new Node(Token.VAR);
        elimination.new EliminationPass(null).removeDeadExprStatementSafely(n);
        // Assert something here
    }

    @Test
    public void testRemoveNode() {
        Node n = new Node(Token.BLOCK);
        elimination.new EliminationPass(null).removeNode(n);
        // Assert something here
    }
}