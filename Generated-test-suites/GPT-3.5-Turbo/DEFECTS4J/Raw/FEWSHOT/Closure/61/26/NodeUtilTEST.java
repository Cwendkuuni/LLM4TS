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
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EXPR_RESULT));
        assertTrue(NodeUtil.isForIn(forInNode));

        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node notStatementNode = new Node(Token.CALL);

        assertTrue(NodeUtil.isStatement(statementNode));
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        Node notFunctionDeclarationNode = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        hoistedFunctionDeclarationNode.setParent(parentScriptNode);

        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(new Node(Token.SCRIPT));
        hoistedFunctionDeclarationNode.setParent(parentFunctionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}