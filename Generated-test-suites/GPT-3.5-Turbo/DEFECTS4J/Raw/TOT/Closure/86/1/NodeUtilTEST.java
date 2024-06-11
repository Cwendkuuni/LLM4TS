package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    private NodeUtil nodeUtil = new NodeUtil();

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR));
        node.addChildToBack(new Node(Token.BLOCK));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_NotForInLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR));
        node.addChildToBack(new Node(Token.BLOCK));

        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node statement = new Node(Token.NAME);
        node.addChildToBack(statement);

        assertTrue(nodeUtil.isStatement(statement));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node node = new Node(Token.NAME);

        assertFalse(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node node = new Node(Token.SCRIPT);

        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}