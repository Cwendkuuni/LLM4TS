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
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NUMBER));
        n.addChildToBack(new Node(Token.IN));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NUMBER));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement_Script() {
        Node n = new Node(Token.NAME);
        n.getParent().setType(Token.SCRIPT);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_Block() {
        Node n = new Node(Token.NAME);
        n.getParent().setType(Token.BLOCK);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_Label() {
        Node n = new Node(Token.NAME);
        n.getParent().setType(Token.LABEL);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node n = new Node(Token.NAME);
        n.getParent().setType(Token.FUNCTION);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_FunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().setType(Token.SCRIPT);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().setType(Token.BLOCK);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_InsideScript() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().setType(Token.SCRIPT);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_InsideFunction() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().getParent().setType(Token.FUNCTION);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoisted() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().setType(Token.BLOCK);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}