package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node2));

        Node node3 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node());
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.BLOCK, new Node());
        assertTrue(NodeUtil.isStatement(node2));

        Node node3 = new Node(Token.LABEL, new Node());
        assertTrue(NodeUtil.isStatement(node3));

        Node node4 = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node());
        Node parent1 = new Node(Token.SCRIPT, node1);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION, new Node());
        Node parent2 = new Node(Token.BLOCK, node2);
        node2.setParent(parent2);
        assertTrue(NodeUtil.isFunctionDeclaration(node2));

        Node node3 = new Node(Token.VAR, new Node());
        Node parent3 = new Node(Token.SCRIPT, node3);
        node3.setParent(parent3);
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node());
        Node parent1 = new Node(Token.SCRIPT, node1);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION, new Node());
        Node parent2 = new Node(Token.SCRIPT, new Node(Token.FUNCTION, node2));
        node2.setParent(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION, new Node());
        Node parent3 = new Node(Token.BLOCK, new Node(Token.FUNCTION, node3));
        node3.setParent(parent3);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

        Node node4 = new Node(Token.VAR, new Node());
        Node parent4 = new Node(Token.FUNCTION, node4);
        node4.setParent(parent4);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}