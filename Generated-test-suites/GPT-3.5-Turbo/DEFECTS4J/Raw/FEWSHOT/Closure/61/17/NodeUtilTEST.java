package com.google.javascript.jscomp.jsonml;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.SEMI));
        forInNode.addChildToBack(new Node(Token.SEMI));
        forInNode.addChildToBack(new Node(Token.SEMI));
        assertTrue(NodeUtil.isForIn(forInNode));

        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToBack(new Node(Token.SEMI));
        notForInNode.addChildToBack(new Node(Token.SEMI));
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node parent = new Node(Token.IF);
        parent.addChildToBack(scriptNode);
        parent.addChildToBack(blockNode);
        parent.addChildToBack(labelNode);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        Node parent = new Node(Token.IF);
        parent.addChildToBack(functionDeclarationNode);
        assertFalse(NodeUtil.isFunctionDeclaration(parent));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToFront(new Node(Token.NAME));

        Node scriptParent = new Node(Token.SCRIPT);
        scriptParent.addChildToBack(functionDeclarationNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

        Node functionParent = new Node(Token.FUNCTION);
        functionParent.addChildToBack(functionDeclarationNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

        Node ifParent = new Node(Token.IF);
        ifParent.addChildToBack(scriptParent);
        ifParent.addChildToBack(functionParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    }
}