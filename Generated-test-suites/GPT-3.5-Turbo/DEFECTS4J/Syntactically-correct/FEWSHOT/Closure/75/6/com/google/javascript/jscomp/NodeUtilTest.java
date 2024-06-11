package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR, null);
        n1.addChildToFront(new Node(Token.NAME, null));
        n1.addChildToFront(new Node(Token.NAME, null));
        n1.addChildToFront(new Node(Token.NAME, null));
        assertTrue(NodeUtil.isForIn(n1));

        Node n2 = new Node(Token.FOR, null);
        n2.addChildToFront(new Node(Token.NAME, null));
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT, null);
        Node n1 = new Node(Token.NAME, parent1);
        assertTrue(NodeUtil.isStatement(n1));

        Node parent2 = new Node(Token.BLOCK, null);
        Node n2 = new Node(Token.NAME, parent2);
        assertTrue(NodeUtil.isStatement(n2));

        Node parent3 = new Node(Token.LABEL, null);
        Node n3 = new Node(Token.NAME, parent3);
        assertTrue(NodeUtil.isStatement(n3));

        Node parent4 = new Node(Token.FUNCTION, null);
        Node n4 = new Node(Token.NAME, parent4);
        assertFalse(NodeUtil.isStatement(n4));

        Node parent5 = null;
        Node n5 = new Node(Token.NAME, parent5);
        assertFalse(NodeUtil.isStatement(n5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT, null);
        Node n1 = new Node(Token.FUNCTION, parent1);
        n1.addChildToFront(new Node(Token.NAME, n1));
        assertTrue(NodeUtil.isFunctionDeclaration(n1));

        Node parent2 = new Node(Token.NAME, null);
        Node n2 = new Node(Token.FUNCTION, parent2);
        n2.addChildToFront(new Node(Token.NAME, n2));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT, null);
        Node n1 = new Node(Token.FUNCTION, parent1);
        n1.addChildToFront(new Node(Token.NAME, n1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

        Node parent2 = new Node(Token.FUNCTION, null);
        Node n2 = new Node(Token.FUNCTION, parent2);
        n2.addChildToFront(new Node(Token.NAME, n2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));

        Node parent3 = new Node(Token.NAME, null);
        Node n3 = new Node(Token.FUNCTION, parent3);
        n3.addChildToFront(new Node(Token.NAME, n3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}