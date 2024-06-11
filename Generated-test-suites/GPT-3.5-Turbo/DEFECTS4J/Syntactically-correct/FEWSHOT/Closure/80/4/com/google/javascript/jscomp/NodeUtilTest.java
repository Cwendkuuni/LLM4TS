package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        Node parent3 = new Node(Token.LABEL);
        Node parent4 = new Node(Token.NAME);

        Node child1 = new Node(Token.NAME);
        child1.setParent(parent1);
        Node child2 = new Node(Token.NAME);
        child2.setParent(parent2);
        Node child3 = new Node(Token.NAME);
        child3.setParent(parent3);
        Node child4 = new Node(Token.NAME);
        child4.setParent(parent4);

        assertTrue(NodeUtil.isStatement(child1));
        assertTrue(NodeUtil.isStatement(child2));
        assertTrue(NodeUtil.isStatement(child3));
        assertFalse(NodeUtil.isStatement(child4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);

        Node child = new Node(Token.FUNCTION);
        child.setParent(parent1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(child));

        child.setParent(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(child));
    }
}