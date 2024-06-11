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
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.AND);
        Node child3 = new Node(Token.SHEQ);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertFalse(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.FOR);
        Node child21 = new Node(Token.NAME);
        Node child22 = new Node(Token.IN);
        Node child23 = new Node(Token.SHEQ);
        node2.addChildToBack(child21);
        node2.addChildToBack(child22);
        node2.addChildToBack(child23);

        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT);
        Node node1 = new Node(Token.VAR);
        parent1.addChildToBack(node1);

        assertTrue(NodeUtil.isStatement(node1));

        Node parent2 = new Node(Token.BLOCK);
        Node node2 = new Node(Token.IF);
        parent2.addChildToBack(node2);

        assertTrue(NodeUtil.isStatement(node2));

        Node parent3 = new Node(Token.LABEL);
        Node node3 = new Node(Token.FOR);
        parent3.addChildToBack(node3);

        assertTrue(NodeUtil.isStatement(node3));

        Node parent4 = new Node(Token.EXPR_RESULT);
        Node node4 = new Node(Token.FUNCTION);
        parent4.addChildToBack(node4);

        assertTrue(NodeUtil.isStatement(node4));

        Node parent5 = new Node(Token.TYPE);
        Node node5 = new Node(Token.MUL);
        parent5.addChildToBack(node5);

        assertFalse(NodeUtil.isStatement(node5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node node1 = new Node(Token.FUNCTION);
        parent1.addChildToBack(node1);

        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node parent2 = new Node(Token.NAME);
        Node grandParent2 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        grandParent2.addChildToBack(parent2);

        assertTrue(NodeUtil.isFunctionDeclaration(node2));

        Node parent3 = new Node(Token.TYPEOF);
        Node node3 = new Node(Token.FUNCTION);
        parent3.addChildToBack(node3);

        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node node1 = new Node(Token.FUNCTION);
        parent1.addChildToBack(node1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node parent2 = new Node(Token.FUNCTION);
        Node grandParent2 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        grandParent2.addChildToBack(parent2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node parent3 = new Node(Token.TYPEOF);
        Node node3 = new Node(Token.FUNCTION);
        parent3.addChildToBack(node3);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}