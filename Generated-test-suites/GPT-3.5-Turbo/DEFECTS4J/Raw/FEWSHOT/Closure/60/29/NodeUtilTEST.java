package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeA = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(nodeA));

        Node nodeB = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(nodeB));
    }

    @Test
    public void testIsStatement() {
        Node nodeA = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(nodeA));

        Node nodeB = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(nodeB));

        Node nodeC = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(nodeC));

        Node nodeD = new Node(Token.NAME);
        nodeD.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(nodeD));

        Node nodeE = new Node(Token.NAME);
        nodeE.setParent(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isStatement(nodeE));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeA = new Node(Token.FUNCTION);
        nodeA.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(nodeA));

        Node nodeB = new Node(Token.FUNCTION);
        nodeB.setParent(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeB));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeA = new Node(Token.FUNCTION);
        nodeA.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeA));

        Node nodeB = new Node(Token.FUNCTION);
        nodeB.setParent(new Node(Token.NAME));
        nodeB.getParent().setParent(new Node(Token.SCRIPT));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeB));

        Node nodeC = new Node(Token.FUNCTION);
        nodeC.setParent(new Node(Token.FUNCTION));
        nodeC.getParent().setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeC));
    }
}