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
        Node forInNode = new Node(Token.FOR, new Node(Token.LP), new Node(Token.VAR), new Node(Token.IN));
        assertTrue(NodeUtil.isForIn(forInNode));

        Node notForInNode = new Node(Token.FOR, new Node(Token.LP));
        assertFalse(NodeUtil.isForIn(notForInNode));

        Node invalidNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));

        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));

        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));

        Node notStatementNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclaration = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

        Node notFunctionDeclaration = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
        hoistedFunctionDeclaration.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        notHoistedFunctionDeclaration.setParent(new Node(Token.SCRIPT, new Node(Token.EXPR_RESULT)));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}