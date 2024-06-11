package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NULL));
        node2.addChildToBack(new Node(Token.TRUE));

        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.ASSIGN);
        Node node5 = new Node(Token.CALL);

        node2.addChildToBack(new Node(Token.NAME));

        node5.addChildToBack(new Node(Token.THIS));
        node5.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertTrue(NodeUtil.isStatement(node4));
        assertFalse(NodeUtil.isStatement(node5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.EXPR_RESULT);
        Node node2 = new Node(Token.VAR);
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.BLOCK);

        node2.addChildToFront(new Node(Token.NAME));
        node3.addChildToFront(new Node(Token.NAME));

        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertTrue(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.FUNCTION);
        Node node5 = new Node(Token.EXPR_RESULT);

        node3.addChildToFront(new Node(Token.NAME));
        node4.addChildToFront(new Node(Token.NAME));

        node1.addChildToBack(node2);
        node1.addChildToBack(node3);
        node2.addChildToBack(node4);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node4));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node5));
    }
}