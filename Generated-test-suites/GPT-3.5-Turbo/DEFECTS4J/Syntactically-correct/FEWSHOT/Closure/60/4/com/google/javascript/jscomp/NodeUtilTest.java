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
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertFalse(NodeUtil.isForIn(node));

        node.setType(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));

        node.addChildToFront(new Node(Token.DO));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EMPTY);
        node.addChildToBack(parent);

        Node child1 = new Node(Token.BLOCK);
        Node child2 = new Node(Token.LABEL);
        Node child3 = new Node(Token.EMPTY);
        parent.addChildToBack(child1);
        parent.addChildToBack(child2);
        parent.addChildToBack(child3);

        assertTrue(NodeUtil.isStatement(child1));
        assertTrue(NodeUtil.isStatement(child2));
        assertFalse(NodeUtil.isStatement(child3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        node.addChildToBack(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(node));

        parent.addChildToBack(grandparent);
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        parent.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        parent.setType(Token.EMPTY);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        node.addChildToBack(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        parent.setType(Token.SCRIPT);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        parent.setParent(grandparent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        grandparent.setType(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}