package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

    private MinimizeExitPoints minimizeExitPoints;

    @Before
    public void setUp() {
        minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
    }

    @Test
    public void testTryMinimizeExits_matchingExitNode() {
        Node node = new Node(Token.RETURN);
        assertEquals(true, minimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
    }
    
    @Test
    public void testTryMinimizeExits_notMatchingExitNode() {
        Node node = new Node(Token.BREAK);
        assertEquals(false, minimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
    }
    
    @Test
    public void testMatchingExitNode_returnWithoutValue() {
        Node node = new Node(Token.RETURN);
        assertEquals(true, minimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
    }
    
    @Test
    public void testMatchingExitNode_notReturnWithoutValue() {
        Node node = new Node(Token.RETURN);
        node.addChildToBack(new Node(Token.TRUE));
        assertEquals(false, minimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
    }
    
    @Test
    public void testMatchingExitNode_continueWithoutLabel() {
        Node node = new Node(Token.CONTINUE);
        assertEquals(true, minimizeExitPoints.matchingExitNode(node, Token.CONTINUE, null));
    }
    
    @Test
    public void testMatchingExitNode_notContinueWithoutLabel() {
        Node node = new Node(Token.CONTINUE);
        node.addChildToBack(new Node(Token.TRUE));
        assertEquals(false, minimizeExitPoints.matchingExitNode(node, Token.CONTINUE, null));
    }
    
    @Test
    public void testMatchingExitNode_breakWithoutLabel() {
        Node node = new Node(Token.BREAK);
        assertEquals(true, minimizeExitPoints.matchingExitNode(node, Token.BREAK, null));
    }
    
    @Test
    public void testMatchingExitNode_notBreakWithoutLabel() {
        Node node = new Node(Token.BREAK);
        node.addChildToBack(new Node(Token.TRUE));
        assertEquals(false, minimizeExitPoints.matchingExitNode(node, Token.BREAK, null));
    }
    
    @Test
    public void testMatchingExitNode_breakWithMatchingLabel() {
        Node node = new Node(Token.BREAK);
        Node label = new Node(Token.LABEL);
        node.addChildToBack(label);
        label.addChildToBack(new Node(Token.STRING, "test"));
        assertEquals(true, minimizeExitPoints.matchingExitNode(node, Token.BREAK, "test"));
    }
    
    @Test
    public void testMatchingExitNode_breakWithNonMatchingLabel() {
        Node node = new Node(Token.BREAK);
        Node label = new Node(Token.LABEL);
        node.addChildToBack(label);
        label.addChildToBack(new Node(Token.STRING, "test"));
        assertEquals(false, minimizeExitPoints.matchingExitNode(node, Token.BREAK, "other"));
    }
    
    @Test
    public void testTryMinimizeIfBlockExits() {
        Node srcBlock = new Node(Token.BLOCK);
        Node destBlock = new Node(Token.BLOCK);
        Node ifNode = new Node(Token.IF);
        int exitType = Token.RETURN;
        String labelName = null;
        minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    }
    
    @Test
    public void testMoveAllFollowing() {
        Node start = new Node(Token.BLOCK);
        Node srcParent = new Node(Token.BLOCK);
        Node destParent = new Node(Token.BLOCK);
        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    }
}