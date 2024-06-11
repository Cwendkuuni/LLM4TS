package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_true(){
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.FOR));
        n.addChildToFront(new Node(Token.FOR));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_false(){
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.FOR));
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement_true(){
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_false(){
        Node n = new Node(Token.STRING);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_true(){
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_false(){
        Node n = new Node(Token.FALSE);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_true(){
        Node n = new Node(Token.FUNCTION);
        n.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_false(){
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}