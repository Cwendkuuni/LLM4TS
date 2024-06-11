package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.NAME));

        Node node2 = new Node(Token.WHILE);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));

        assertEquals(true, NodeUtil.isForIn(node1));
        assertEquals(false, NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT);
        Node node1 = new Node(Token.NAME);
        parent1.addChildToBack(node1);

        Node parent2 = new Node(Token.LABEL);
        Node node2 = new Node(Token.NAME);
        parent2.addChildToBack(node2);

        assertEquals(true, NodeUtil.isStatement(node1));
        assertEquals(true, NodeUtil.isStatement(parent1));
        assertEquals(false, NodeUtil.isStatement(node2));
        assertEquals(false, NodeUtil.isStatement(parent2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node node1 = new Node(Token.FUNCTION);
        parent1.addChildToBack(node1);

        Node parent2 = new Node(Token.NAME);
        Node node2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);

        assertEquals(true, NodeUtil.isFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandparent1 = new Node(Token.SCRIPT);
        Node parent1 = new Node(Token.FUNCTION);
        grandparent1.addChildToBack(parent1);
        parent1.addChildToBack(new Node(Token.NAME));

        Node grandparent2 = new Node(Token.NAME);
        Node parent2 = new Node(Token.FUNCTION);
        grandparent2.addChildToBack(parent2);
        parent2.addChildToBack(new Node(Token.NAME));

        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(parent1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(parent2));
    }
}