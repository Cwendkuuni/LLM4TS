package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
    
    private MinimizeExitPoints minimizeExitPoints;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        minimizeExitPoints = new MinimizeExitPoints(compiler);
        externs = new Node(Token.EXTERN);
        root = new Node(Token.ROOT);
    }

    @Test
    public void testProcess() {
        minimizeExitPoints.process(externs, root);
        // assert something
    }
    
    @Test
    public void testVisit_label() {
        NodeTraversal t = new NodeTraversal(compiler, minimizeExitPoints);
        Node n = new Node(Token.LABEL);
        Node parent = new Node(Token.ROOT);
        
        minimizeExitPoints.visit(t, n, parent);
        // assert something
    }
    
    @Test
    public void testVisit_for() {
        NodeTraversal t = new NodeTraversal(compiler, minimizeExitPoints);
        Node n = new Node(Token.FOR);
        Node parent = new Node(Token.ROOT);
        
        minimizeExitPoints.visit(t, n, parent);
        // assert something
    }
    
    @Test
    public void testVisit_while() {
        NodeTraversal t = new NodeTraversal(compiler, minimizeExitPoints);
        Node n = new Node(Token.WHILE);
        Node parent = new Node(Token.ROOT);
        
        minimizeExitPoints.visit(t, n, parent);
        // assert something
    }
    
    @Test
    public void testVisit_do() {
        NodeTraversal t = new NodeTraversal(compiler, minimizeExitPoints);
        Node n = new Node(Token.DO);
        Node parent = new Node(Token.ROOT);
        
        minimizeExitPoints.visit(t, n, parent);
        // assert something
    }
    
    @Test
    public void testVisit_function() {
        NodeTraversal t = new NodeTraversal(compiler, minimizeExitPoints);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ROOT);
        
        minimizeExitPoints.visit(t, n, parent);
        // assert something
    }
    
    @Test
    public void testTryMinimizeExits() {
        Node n = new Node(Token.FUNCTION);
        int exitType = Token.RETURN;
        String labelName = null;
        
        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
        // assert something
    }
    
    @Test
    public void testTryMinimizeIfBlockExits() {
        Node srcBlock = new Node(Token.BLOCK);
        Node destBlock = new Node(Token.BLOCK);
        Node ifNode = new Node(Token.IF);
        int exitType = Token.BREAK;
        String labelName = null;
        
        minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
        // assert something
    }
    
    @Test
    public void testMatchingExitNode_return() {
        Node n = new Node(Token.RETURN);
        int type = Token.RETURN;
        String labelName = null;
        
        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
        // assert something
    }
    
    @Test
    public void testMatchingExitNode_break() {
        Node n = new Node(Token.BREAK);
        int type = Token.BREAK;
        String labelName = null;
        
        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
        // assert something
    }
    
    @Test
    public void testMatchingExitNode_breakWithLabel() {
        Node n = new Node(Token.BREAK);
        int type = Token.BREAK;
        String labelName = "label";
        
        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
        // assert something
    }
    
    @Test
    public void testMatchingExitNode_continue() {
        Node n = new Node(Token.CONTINUE);
        int type = Token.CONTINUE;
        String labelName = null;
        
        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
        // assert something
    }
    
    @Test
    public void testMatchingExitNode_continueWithLabel() {
        Node n = new Node(Token.CONTINUE);
        int type = Token.CONTINUE;
        String labelName = "label";
        
        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);
        // assert something
    }
    
    @Test
    public void testMoveAllFollowing() {
        Node start = new Node(Token.FUNCTION);
        Node srcParent = new Node(Token.ROOT);
        Node destParent = new Node(Token.ROOT);
        
        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        // assert something
    }
}