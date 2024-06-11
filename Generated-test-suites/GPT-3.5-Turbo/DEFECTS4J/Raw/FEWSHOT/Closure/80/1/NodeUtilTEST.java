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

        assertFalse(NodeUtil.isForIn(node));

        Node left = new Node(Token.NAME);
        Node right = new Node(Token.LITERAL);
        Node block = new Node(Token.BLOCK);

        node.addChildToBack(left);
        node.addChildToBack(right);
        node.addChildToBack(block);

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.NAME);

        Node script = new Node(Token.SCRIPT);
        Node block = new Node(Token.BLOCK);
        Node label = new Node(Token.LABEL);

        script.addChildToBack(node);
        block.addChildToBack(node);
        label.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(script));
        assertTrue(NodeUtil.isStatement(block));
        assertTrue(NodeUtil.isStatement(label));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
        assertFalse(NodeUtil.isFunctionDeclaration(parent));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        grandparent.addChildToBack(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(grandparent));
    }
}