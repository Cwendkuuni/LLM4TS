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
        Node nodeForIn = new Node(Token.FOR);
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.FOR);
        nodeNotForIn.addChildToBack(new Node(Token.NAME));
        nodeNotForIn.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(nodeNotForIn));

        Node nodeIsForIn = new Node(Token.FOR);
        nodeIsForIn.addChildToBack(new Node(Token.NAME));
        nodeIsForIn.addChildToBack(new Node(Token.NAME));
        nodeIsForIn.addChildToBack(new Node(Token.NAME));
        nodeIsForIn.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(nodeIsForIn));
    }

    @Test
    public void testIsStatement() {
        Node pNode = new Node(Token.SCRIPT);
        Node bNode = new Node(Token.BLOCK);
        Node lNode = new Node(Token.LABEL);
        Node tNode = new Node(Token.NAME);

        bNode.addChildToBack(pNode);
        lNode.addChildToBack(bNode);
        tNode.addChildToBack(lNode);

        assertTrue(NodeUtil.isStatement(bNode));
        assertTrue(NodeUtil.isStatement(lNode));
        assertTrue(NodeUtil.isStatement(tNode));
    }

    @Test
    public void testIsNotStatement() {
        Node pNode = new Node(Token.SCRIPT);
        Node bNode = new Node(Token.BLOCK);
        Node lNode = new Node(Token.LABEL);
        Node tNode = new Node(Token.NAME);

        bNode.addChildToBack(pNode);
        tNode.addChildToBack(bNode);

        assertFalse(NodeUtil.isStatement(pNode));
        assertFalse(NodeUtil.isStatement(tNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node pNode = new Node(Token.SCRIPT);
        Node fNode = new Node(Token.FUNCTION);
        Node sNode = new Node(Token.NAME);

        fNode.addChildToBack(sNode);
        pNode.addChildToBack(fNode);

        assertTrue(NodeUtil.isFunctionDeclaration(fNode));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node pNode = new Node(Token.SCRIPT);
        Node fNode = new Node(Token.FUNCTION);
        Node sNode = new Node(Token.NAME);

        fNode.addChildToBack(sNode);

        assertFalse(NodeUtil.isFunctionDeclaration(fNode));
        assertFalse(NodeUtil.isFunctionDeclaration(pNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node p1Node = new Node(Token.SCRIPT);
        Node p2Node = new Node(Token.SCRIPT);
        Node fNode = new Node(Token.FUNCTION);
        Node p3Node = new Node(Token.NAME);

        fNode.addChildToBack(p3Node);
        p2Node.addChildToBack(fNode);
        p1Node.addChildToBack(p2Node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(fNode));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node pNode = new Node(Token.SCRIPT);
        Node fNode = new Node(Token.FUNCTION);
        Node sNode = new Node(Token.NAME);

        fNode.addChildToBack(sNode);
        pNode.addChildToBack(fNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(fNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(pNode));
    }
}