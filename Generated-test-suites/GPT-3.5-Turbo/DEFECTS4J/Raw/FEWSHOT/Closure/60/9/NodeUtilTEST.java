package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.NAME);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        node.setParent(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        node.setParent(new Node(Token.LABEL));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        node.setParent(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        assertFalse(NodeUtil.isFunctionDeclaration(node.getParent()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.setParent(new Node(Token.NAME));
        node.setParent(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        Node parent2 = new Node(Token.FUNCTION);
        parent2.setParent(new Node(Token.SCRIPT));
        node.setParent(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node.setParent(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}