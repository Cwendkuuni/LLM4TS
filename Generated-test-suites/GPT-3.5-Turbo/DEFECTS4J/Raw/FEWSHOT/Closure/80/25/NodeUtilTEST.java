package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.NAME);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.NAME);
        assertTrue(NodeUtil.isStatement(node));
        
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.IF);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        parent = new Node(Token.IF);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.IF);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
    }
}