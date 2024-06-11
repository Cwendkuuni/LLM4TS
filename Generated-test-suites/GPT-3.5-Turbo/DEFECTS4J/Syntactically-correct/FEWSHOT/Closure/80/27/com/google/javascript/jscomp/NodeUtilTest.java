package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(1));
        n2.addChildToBack(new Node(2));
        n2.addChildToBack(new Node(3));

        assertFalse(NodeUtil.isForIn(n1));
        assertTrue(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.VAR);

        assertTrue(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
        assertTrue(NodeUtil.isStatement(n3));
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.SCRIPT);
        Node n3 = new Node(Token.BLOCK);
        Node n4 = new Node(Token.VAR);

        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
        assertFalse(NodeUtil.isFunctionDeclaration(n3));
        assertFalse(NodeUtil.isFunctionDeclaration(n4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.SCRIPT);
        Node n3 = new Node(Token.FUNCTION);

        n1.addChildToFront(new Node(Token.NAME));
        n2.addChildToFront(new Node(Token.FUNCTION));
        n2.getParent().addChildToFront(new Node(Token.SCRIPT));
        n3.addChildToFront(new Node(Token.NAME));
        n3.getParent().addChildToFront(new Node(Token.FUNCTION));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}