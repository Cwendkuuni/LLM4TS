package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.VAR);

        node.addChildToFront(childNode);

        assertTrue(NodeUtil.isStatement(childNode));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.VAR);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);

        parent.addChildToBack(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);

        script.addChildToBack(node);
        functionNode.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.VAR);

        script.addChildToBack(node);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}