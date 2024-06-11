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
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));

        node.setType(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));

        node.setType(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.BLOCK);
        node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.LABEL);
        node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.SWITCH);
        node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.FUNCTION);
        node.addChildToBack(child);

        assertFalse(NodeUtil.isFunctionDeclaration(child));

        child.setType(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(child);
        child.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(child));

        child.setType(Token.FUNCTION);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(child);
        child.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(child));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.FUNCTION);
        node.addChildToBack(child);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(child));

        child.setType(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(child);
        child.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(child));

        child.setType(Token.FUNCTION);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(child);
        child.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(child));
    }
}