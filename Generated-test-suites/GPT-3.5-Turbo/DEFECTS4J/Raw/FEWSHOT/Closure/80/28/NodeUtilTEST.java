package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.TRUE);
        Node child3 = new Node(Token.BLOCK);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(n));

        Node n2 = new Node(Token.FOR);
        Node child4 = new Node(Token.EXPR_RESULT);
        Node child5 = new Node(Token.BLOCK);
        n2.addChildToBack(child4);
        n2.addChildToBack(child5);

        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));

        Node n2 = new Node(Token.NAME);
        Node parent2 = new Node(Token.EXPR_RESULT);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);

        assertFalse(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));

        Node n2 = new Node(Token.NAME);
        Node parent2 = new Node(Token.EXPR_RESULT);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);

        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.SCRIPT);
        Node child = new Node(Token.VAR);
        Node grandChild = new Node(Token.FUNCTION);
        child.addChildToBack(grandChild);
        n.addChildToBack(child);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(grandChild));

        Node n2 = new Node(Token.SCRIPT);
        Node child2 = new Node(Token.FALSE);
        n2.addChildToBack(child2);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(child2));
    }
}