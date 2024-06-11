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
        Node node1 = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.IF, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node2));

        Node node3 = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node otherNode = new Node(Token.IF);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node otherNode = new Node(Token.VAR);
        functionNode.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode1 = new Node(Token.FUNCTION);
        Node functionNode2 = new Node(Token.FUNCTION);
        functionNode1.addChildToBack(new Node(Token.NAME));
        functionNode2.addChildToBack(new Node(Token.NAME));
        scriptNode.addChildToFront(functionNode1);
        functionNode2.addChildToBack(functionNode1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    }
}