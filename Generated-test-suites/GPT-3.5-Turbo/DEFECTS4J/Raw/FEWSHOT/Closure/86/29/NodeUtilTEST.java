package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n = createMockNode(Token.FOR, 3);
        assertTrue(NodeUtil.isForIn(n));

        n = createMockNode(Token.FOR, 2);
        assertFalse(NodeUtil.isForIn(n));

        n = createMockNode(Token.WHILE, 3);
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = createMockNode(Token.SCRIPT, 0);
        assertTrue(NodeUtil.isStatement(n));

        n = createMockNode(Token.BLOCK, 0);
        assertTrue(NodeUtil.isStatement(n));

        n = createMockNode(Token.LABEL, 0);
        assertTrue(NodeUtil.isStatement(n));

        n = createMockNode(Token.WHILE, 0);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = createMockNode(Token.FUNCTION, 0);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        n = createMockNode(Token.FUNCTION, 1);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        n = createMockNode(Token.FUNCTION, 2);
        assertFalse(NodeUtil.isFunctionDeclaration(n));

        n = createMockNode(Token.LABEL, 0);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = createMockFunctionDeclarationNode(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        n = createMockFunctionDeclarationNode(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        n = createMockFunctionDeclarationNode(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        n = createMockFunctionDeclarationNode(Token.FUNCTION);
        n.getParent().getType() = Token.IF;
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    private Node createMockNode(int type, int childCount) {
        Node n = new Node(type);
        for (int i = 0; i < childCount; i++) {
            n.addChildToFront(new Node(i));
        }
        return n;
    }

    private Node createMockFunctionDeclarationNode(int parentType) {
        Node n = new Node(Token.FUNCTION);
        n.getParent() = new Node(parentType);
        return n;
    }
}