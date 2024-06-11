package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

    @Test
    public void testProcess() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
        assertNotNull(minimizeExitPoints);
    }

    @Test
    public void testVisit() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
        assertNotNull(minimizeExitPoints);

        NodeTraversal t = new NodeTraversal(minimizeExitPoints.getCompiler());
        Node n = new Node(Token.LABEL);
        Node parent = new Node();
        minimizeExitPoints.visit(t, n, parent);

        n = new Node(Token.FOR);
        parent = new Node();
        minimizeExitPoints.visit(t, n, parent);

        n = new Node(Token.WHILE);
        parent = new Node();
        minimizeExitPoints.visit(t, n, parent);

        n = new Node(Token.DO);
        parent = new Node();
        minimizeExitPoints.visit(t, n, parent);

        n = new Node(Token.FUNCTION);
        parent = new Node();
        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testTryMinimizeExits() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
        assertNotNull(minimizeExitPoints);

        Node n = new Node(Token.FUNCTION);
        int exitType = 1;
        String labelName = "label";
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

        n = new Node(Token.FOR);
        exitType = 2;
        labelName = null;
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

        n = new Node(Token.FUNCTION);
        exitType = 3;
        labelName = null;
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

        n = new Node(Token.TRY);
        exitType = 4;
        labelName = null;
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);

        n = new Node(Token.LABEL);
        exitType = 5;
        labelName = null;
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    }

    @Test
    public void testMatchingExitNode() {
        Node n = new Node(Token.RETURN);
        int type = 1;
        String labelName = "label";
        boolean result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
        assertFalse(result);

        n = new Node(Token.BREAK);
        type = 2;
        labelName = null;
        result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
        assertTrue(result);

        n = new Node(Token.BREAK);
        type = 3;
        labelName = "label";
        result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
        assertFalse(result);
    }

    @Test
    public void testMoveAllFollowing() {
        Node start = new Node();
        Node srcParent = new Node();
        Node destParent = new Node();
        MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        assertTrue(true); // Just checking if the method runs without error
    }
}