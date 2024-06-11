package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.STRING));
        n2.addChildToBack(new Node(Token.STRING));
        n2.addChildToBack(new Node(Token.STRING));

        assertFalse(NodeUtil.isForIn(n1));
        assertTrue(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.VAR);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(new Node(Token.VAR));
        Node block = new Node(Token.BLOCK);
        block.addChildToBack(new Node(Token.VAR));
        Node label = new Node(Token.LABEL);
        label.addChildToBack(new Node(Token.VAR));

        assertFalse(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(script));
        assertTrue(NodeUtil.isStatement(block));
        assertTrue(NodeUtil.isStatement(label));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.STRING);
        Node n2 = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(new Node(Token.FUNCTION));
        Node block = new Node(Token.BLOCK);
        block.addChildToBack(new Node(Token.FUNCTION));

        assertFalse(NodeUtil.isFunctionDeclaration(n1));
        assertTrue(NodeUtil.isFunctionDeclaration(n2));
        assertFalse(NodeUtil.isFunctionDeclaration(script));
        assertFalse(NodeUtil.isFunctionDeclaration(block));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.STRING);
        Node n2 = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(new Node(Token.FUNCTION));
        Node fn = new Node(Token.FUNCTION);
        fn.addChildToBack(new Node(Token.FUNCTION));
        Node block = new Node(Token.BLOCK);
        block.addChildToBack(fn);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(script));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(fn));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(block));
    }
}