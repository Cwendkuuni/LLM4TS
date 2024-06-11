package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsNotForIn() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(n));

        n.setType(Token.FOR);
        assertFalse(NodeUtil.isForIn(n));

        n.setType(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));

        n.setType(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));

        n.setType(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatementForNullParent() {
        Node n = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatementForScriptParent() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatementForBlockParent() {
        Node parent = new Node(Token.BLOCK);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatementForLabelParent() {
        Node parent = new Node(Token.LABEL);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatementForOtherParent() {
        Node parent = new Node(Token.IF);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
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
    public void testIsFunctionExpression() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionExpression(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationForScriptParent() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationForFunctionParent() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = new Node(Token.IF);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}