package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToFront(new Node(Token.EMPTY));
        n1.addChildToFront(new Node(Token.EMPTY));
        n1.addChildToFront(new Node(Token.EMPTY));

        Node n2 = new Node(Token.FOR);
        n2.addChildToFront(new Node(Token.EMPTY));
        n2.addChildToFront(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(n1));
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT);
        Node n1 = new Node(Token.EMPTY);
        parent1.addChildToFront(n1);
        
        Node parent2 = new Node(Token.BLOCK);
        Node n2 = new Node(Token.EMPTY);
        parent2.addChildToFront(n2);
        
        Node parent3 = new Node(Token.LABEL);
        Node n3 = new Node(Token.EMPTY);
        parent3.addChildToFront(n3);
        
        Node parent4 = new Node(Token.EMPTY);
        Node n4 = new Node(Token.EMPTY);
        parent4.addChildToFront(n4);
        
        assertTrue(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
        assertTrue(NodeUtil.isStatement(n3));
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent1 = new Node(Token.FUNCTION);
        parent1.addChildToFront(new Node(Token.EMPTY));
        Node n1 = new Node(Token.EMPTY);
        parent1.addChildToFront(n1);
        
        Node parent2 = new Node(Token.EMPTY);
        Node n2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(n2);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.FUNCTION);
        parent1.addChildToFront(new Node(Token.EMPTY));
        Node n1 = new Node(Token.EMPTY);
        Node script = new Node(Token.SCRIPT);
        script.addChildToFront(parent1);
        script.addChildToFront(n1);
        
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(new Node(Token.EMPTY));
        Node n2 = new Node(Token.EMPTY);
        parent2.addChildToFront(n2);
        
        Node parent3 = new Node(Token.FUNCTION);
        parent3.addChildToFront(new Node(Token.EMPTY));
        Node n3 = new Node(Token.EMPTY);
        Node parent31 = new Node(Token.SCRIPT);
        parent31.addChildToFront(parent3);
        parent31.addChildToFront(n3);
        Node parent32 = new Node(Token.FUNCTION);
        parent32.addChildToFront(new Node(Token.EMPTY));
        Node n32 = new Node(Token.EMPTY);
        parent32.addChildToFront(n32);
        Node script2 = new Node(Token.SCRIPT);
        script2.addChildToFront(parent32);
        script2.addChildToFront(n32);
        script2.addChildToFront(parent31);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}