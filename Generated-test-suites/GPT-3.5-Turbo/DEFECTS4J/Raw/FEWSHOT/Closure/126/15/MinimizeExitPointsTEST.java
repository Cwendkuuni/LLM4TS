package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MinimizeExitPointsTest {
    @Test
    public void test_tryMinimizeExits_matchingExitNode() {
        Node node = new Node(Token.RETURN);
        assertTrue(MinimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
    }

    @Test
    public void test_tryMinimizeIfBlockExits() {
        Node srcBlock = new Node(Token.BLOCK);
        Node destBlock = new Node(Token.BLOCK);
        Node ifNode = new Node(Token.IF);
        int exitType = Token.BREAK;
        String labelName = null;
        MinimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
        assertEquals(destBlock, ifNode.getParent().getLastChild());
    }

    @Test
    public void test_moveAllFollowing() {
        Node start = new Node(Token.LABEL);
        Node srcParent = new Node(Token.BLOCK);
        Node destParent = new Node(Token.BLOCK);
        Node child1 = new Node(Token.EXPR_RESULT);
        Node child2 = new Node(Token.RETURN);
        srcParent.addChildToFront(child1);
        srcParent.addChildToFront(child2);
        MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        assertEquals(child1, destParent.getLastChild().getFirstChild());
        assertEquals(child2, destParent.getLastChild());
    }
}