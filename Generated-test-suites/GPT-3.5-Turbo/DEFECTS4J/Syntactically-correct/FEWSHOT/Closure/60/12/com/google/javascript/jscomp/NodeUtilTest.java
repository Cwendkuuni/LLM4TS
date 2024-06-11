package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(n));
        
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.NAME));
        n2.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.NAME);
        n.setParent(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isStatement(n));
        
        Node n2 = new Node(Token.NAME);
        n2.setParent(new Node(Token.FUNCTION));
        
        assertFalse(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        parent.addChildToFront(n);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        n2.setParent(parent2);
        parent2.addChildToFront(n2);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        parent.addChildToFront(n);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        n2.setParent(parent2);
        parent2.addChildToFront(grandparent);
        grandparent.addChildToFront(n2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
    }
}