package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToBack(new Node(Token.NAME));
        notForInNode.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(notForInNode));

        Node nonForNode = new Node(Token.WHILE);
        nonForNode.addChildToBack(new Node(Token.NAME));
        nonForNode.addChildToBack(new Node(Token.NAME));
        nonForNode.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(nonForNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        statementNode.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(statementNode));

        Node nonStatementNode = new Node(Token.IF);
        nonStatementNode.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        Node nonFunctionDeclarationNode = new Node(Token.IF);
        nonFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedDeclarationNodeInsideScript = new Node(Token.FUNCTION);
        hoistedDeclarationNodeInsideScript.addChildToBack(new Node(Token.NAME));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(hoistedDeclarationNodeInsideScript);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedDeclarationNodeInsideScript));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));

        Node hoistedDeclarationNodeInsideFunction = new Node(Token.FUNCTION);
        hoistedDeclarationNodeInsideFunction.addChildToBack(new Node(Token.NAME));
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(hoistedDeclarationNodeInsideFunction);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedDeclarationNodeInsideFunction));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}