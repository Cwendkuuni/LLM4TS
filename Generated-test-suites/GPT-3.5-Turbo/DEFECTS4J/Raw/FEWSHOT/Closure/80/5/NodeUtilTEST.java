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
        Node n = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(n));

        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.NAME);

        n.addChildToFront(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.NAME);
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);
        parent.setParent(grandparent);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.NAME);
        parent = new Node(Token.NAME);
        parent.addChildToBack(n);
        grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);
        parent.setParent(grandparent);
        n.setParent(parent);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(n));

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToBack(parent);
        parent.setParent(grandparent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);
        parent.setParent(grandparent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}