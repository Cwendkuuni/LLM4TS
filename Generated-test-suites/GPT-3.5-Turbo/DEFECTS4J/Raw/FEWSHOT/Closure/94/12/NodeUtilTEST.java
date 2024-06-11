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
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.EXPR_RESULT);
        Node child3 = new Node(Token.BLOCK);

        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsNotForIn() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.EXPR_RESULT);

        n.addChildToBack(child1);
        n.addChildToBack(child2);

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsNotStatement() {
        Node n = new Node(Token.CALL);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}