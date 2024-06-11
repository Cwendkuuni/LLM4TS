package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = createNode(Token.FOR, createNode(Token.VAR), createNode(Token.TRUE), createNode(Token.FALSE));
        assertFalse(NodeUtil.isForIn(node));

        node = createNode(Token.FOR, createNode(Token.VAR), createNode(Token.TRUE), createNode(Token.FALSE), createNode(Token.FALSE));
        assertTrue(NodeUtil.isForIn(node));

        node = createNode(Token.IF, createNode(Token.VAR), createNode(Token.TRUE), createNode(Token.FALSE));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = createNode(Token.TRUE);
        assertTrue(NodeUtil.isStatement(node));

        node = createNode(Token.FOR, createNode(Token.VAR), createNode(Token.TRUE), createNode(Token.FALSE));
        assertFalse(NodeUtil.isStatement(node));

        node = createNode(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));

        node = createNode(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));

        node = createNode(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = createNode(Token.FUNCTION, createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = createNode(Token.EXPR_RESULT, createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK)));
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        node = createNode(Token.FUNCTION, createNode(Token.EXPR_RESULT), createNode(Token.BLOCK));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node.getParent().getParent()));

        node = createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        node.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = createNode(Token.FUNCTION, createNode(Token.NAME), createNode(Token.PARAM_LIST), createNode(Token.BLOCK));
        node.getParent().getParent().setType(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    private Node createNode(int type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
}