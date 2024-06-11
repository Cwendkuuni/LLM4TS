package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));
        
        Node node2 = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node2));
        
        Node node3 = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node3));
        
        Node node4 = new Node(Token.NUMBER);
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node1);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        Node grandParent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(grandParent2);
        grandParent2.addChildToFront(node2);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node1);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        parent2.addChildToFront(node2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}