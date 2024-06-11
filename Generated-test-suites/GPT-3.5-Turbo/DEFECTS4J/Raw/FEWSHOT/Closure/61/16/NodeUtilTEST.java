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
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));

        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n1));
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.BLOCK);
        n1.addChildToBack(new Node(Token.EMPTY));

        Node n2 = new Node(Token.EXPR_RESULT);
        n2.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToBack(new Node(Token.NAME));

        Node n2 = new Node(Token.EXPR_RESULT);
        n2.addChildToBack(new Node(Token.FUNCTION));
        n2.getLastChild().addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToBack(new Node(Token.NAME));

        Node n2 = new Node(Token.SCRIPT);
        n2.addChildToBack(new Node(Token.FUNCTION));
        n2.getLastChild().addChildToBack(new Node(Token.NAME));

        Node n3 = new Node(Token.FUNCTION);
        n3.addChildToBack(new Node(Token.NAME));

        Node n4 = new Node(Token.FUNCTION);
        n4.addChildToBack(new Node(Token.NAME));

        Node n5 = new Node(Token.FUNCTION);
        n5.addChildToBack(new Node(Token.NAME));

        n4.addChildToBack(n5);
        n3.addChildToBack(n4);
        n2.addChildToBack(n3);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
    }
}