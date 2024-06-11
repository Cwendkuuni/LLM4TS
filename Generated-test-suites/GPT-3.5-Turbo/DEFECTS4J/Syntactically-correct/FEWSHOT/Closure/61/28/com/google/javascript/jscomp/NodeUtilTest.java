package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        
        Node node2 = new Node(Token.EMPTY);
        
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.EMPTY);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));

        Node node2 = new Node(Token.EMPTY);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parentNode1 = new Node(Token.SCRIPT);
        Node parentNode2 = new Node(Token.FUNCTION);
        Node parentNode3 = new Node(Token.EMPTY);
        
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.setParent(parentNode1);

        Node node2 = new Node(Token.EMPTY);
        node2.setParent(parentNode2);
        
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.setParent(parentNode3);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}