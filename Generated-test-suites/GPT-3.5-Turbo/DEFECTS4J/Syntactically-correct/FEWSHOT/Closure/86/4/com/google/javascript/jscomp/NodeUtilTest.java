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
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.WHILE);
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(node));

        node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node);
        parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.SCRIPT);
        parent3.addChildToBack(new Node(Token.VAR));
        parent3.addChildToBack(node);
        parent3.addChildToBack(new Node(Token.EXPR_RESULT));
        parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(parent3);
        parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(parent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}