package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        assertFalse(NodeUtil.isForIn(null));
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR, null)));
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR, new Node(null), new Node(null), new Node(null))));
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, null, null, null)));
    }

    @Test
    public void testIsStatement() {
        assertFalse(NodeUtil.isStatement(null));
        assertFalse(NodeUtil.isStatement(new Node(null)));
        assertTrue(NodeUtil.isStatement(new Node(Token.LABEL, null)));
        assertTrue(NodeUtil.isStatement(new Node(Token.SCRIPT, null)));
        assertTrue(NodeUtil.isStatement(new Node(Token.BLOCK, null)));
    }

    @Test
    public void testIsFunctionDeclaration() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(null)));
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, null)));
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, new Node(null))));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, null)));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(null))));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(null), new Node(null))));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(null), new Node(null), new Node(null))));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(null), new Node(null), new Node(null), new Node(null, new Node(null)), new Node(null, new Node(null, new Node(null, new Node(null)))))));
    }
}