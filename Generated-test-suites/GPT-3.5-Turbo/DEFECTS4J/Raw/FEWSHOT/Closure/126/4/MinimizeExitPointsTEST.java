package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MinimizeExitPointsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node externs = new Node(Token.EXTERNRESULT);
        Node root = new Node(Token.ROOT);
        minimizeExitPoints.process(externs, root);
        // Add assertions here
        assertEquals(null, minimizeExitPoints.compiler);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        NodeTraversal.Callback callback = minimizeExitPoints;
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = new Node(Token.NUMBER);
        Node parent = new Node(Token.PARENT);
        minimizeExitPoints.visit(t, n, parent);
        // Add assertions here
    }

    @Test
    public void testTryMinimizeExits() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.NUMBER);
        int exitType = Token.RETURN;
        String labelName = "label";
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
        // Add assertions here
    }

    @Test
    public void testTryMinimizeIfBlockExits() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node srcBlock = new Node(Token.SOURCEBLOCK);
        Node destBlock = new Node(Token.DESTBLOCK);
        Node ifNode = new Node(Token.IF);
        int exitType = Token.RETURN;
        String labelName = "label";
        minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
        // Add assertions here
    }

    @Test
    public void testMatchingExitNode() {
        Node n = new Node(Token.NUMBER);
        int type = Token.RETURN;
        String labelName = "label";
        boolean result = MinimizeExitPoints.matchingExitNode(n, type, labelName);
        // Add assertions here
        assertEquals(false, result);
    }

    @Test
    public void testMoveAllFollowing() {
        Node start = new Node(Token.START);
        Node srcParent = new Node(Token.SOURCEPARENT);
        Node destParent = new Node(Token.DESTPARENT);
        MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        // Add assertions here
    }
}