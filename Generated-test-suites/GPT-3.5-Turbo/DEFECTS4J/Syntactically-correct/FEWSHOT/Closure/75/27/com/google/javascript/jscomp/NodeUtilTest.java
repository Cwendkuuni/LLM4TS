package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node forIn = new Node(Token.FOR);
        forIn.addChildToBack(new Node(Token.EXPR_RESULT));
        forIn.addChildToBack(new Node(Token.EXPR_RESULT));
        forIn.addChildToBack(new Node(Token.EXPR_RESULT));
        assertTrue(NodeUtil.isForIn(forIn));

        Node notForIn = new Node(Token.FOR);
        notForIn.addChildToBack(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isForIn(notForIn));
    }

    @Test
    public void testIsStatement() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node blockParent = new Node(Token.BLOCK);
        Node labelParent = new Node(Token.LABEL);
        Node nonStatementParent = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isStatement(scriptParent));
        assertTrue(NodeUtil.isStatement(blockParent));
        assertTrue(NodeUtil.isStatement(labelParent));
        assertFalse(NodeUtil.isStatement(nonStatementParent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclaration = new Node(Token.FUNCTION);
        functionDeclaration.addChildToBack(new Node(Token.EXPR_RESULT));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

        Node notFunctionDeclaration = new Node(Token.ERR);
        notFunctionDeclaration.addChildToBack(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
        hoistedFunctionDeclaration.addChildToBack(new Node(Token.EXPR_RESULT));
        hoistedFunctionDeclaration.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        notHoistedFunctionDeclaration.addChildToBack(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}