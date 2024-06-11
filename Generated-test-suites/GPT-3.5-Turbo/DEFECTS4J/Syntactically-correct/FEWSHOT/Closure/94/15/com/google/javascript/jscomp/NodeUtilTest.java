package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.IN);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.FOR);
        Node child4 = new Node(Token.NAME);
        node2.addChildToBack(child1);
        node2.addChildToBack(child2);
        node2.addChildToBack(child4);

        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        Node node2 = new Node(Token.FOR);
        Node parent2 = new Node(Token.NAME);
        parent2.addChildToBack(node2);
        node2.setParent(parent2);

        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.VAR);
        Node parent2 = new Node(Token.NAME);
        parent2.addChildToBack(node2);
        node2.setParent(parent2);

        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        node2.setParent(parent2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.NAME);
        parent3.addChildToBack(node3);
        node3.setParent(parent3);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}