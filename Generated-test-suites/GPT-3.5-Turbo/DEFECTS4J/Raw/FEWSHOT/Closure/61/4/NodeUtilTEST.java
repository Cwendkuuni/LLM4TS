package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.FOR));
        node2.addChildToBack(new Node(Token.FOR));
        node2.addChildToBack(new Node(Token.FOR));

        assertEquals(true, NodeUtil.isForIn(node1));
        assertEquals(false, NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.FUNCTION);
        Node parentNode1 = new Node(Token.PARENT);
        parentNode1.addChildToFront(node1);
        Node parentNode2 = new Node(Token.PARENT);
        parentNode2.addChildToFront(node2);
        Node parentNode3 = new Node(Token.PARENT);
        parentNode3.addChildToFront(node3);
        Node parentNode4 = new Node(Token.PARENT);
        parentNode4.addChildToFront(node4);

        assertEquals(true, NodeUtil.isStatement(node1));
        assertEquals(true, NodeUtil.isStatement(node2));
        assertEquals(true, NodeUtil.isStatement(node3));
        assertEquals(false, NodeUtil.isStatement(node4));

        assertEquals(false, NodeUtil.isStatement(parentNode1));
        assertEquals(false, NodeUtil.isStatement(parentNode2));
        assertEquals(false, NodeUtil.isStatement(parentNode3));
        assertEquals(false, NodeUtil.isStatement(parentNode4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.NOT_FUNCTION);
        Node parentNode1 = new Node(Token.PARENT);
        parentNode1.addChildToFront(node1);
        Node parentNode2 = new Node(Token.PARENT);
        parentNode2.addChildToFront(node2);

        assertEquals(true, NodeUtil.isFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node2));

        assertEquals(false, NodeUtil.isFunctionDeclaration(parentNode1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(parentNode2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.NOT_FUNCTION);
        Node parentNode1 = new Node(Token.PARENT);
        parentNode1.addChildToFront(node1);
        Node parentNode2 = new Node(Token.PARENT);
        parentNode2.addChildToFront(node2);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToFront(functionNode);
        Node parentNode3 = new Node(Token.PARENT);
        parentNode3.addChildToFront(scriptNode);

        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node2));
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(parentNode3));

        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(parentNode1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(parentNode2));
    }
}