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
        Node forNode = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.FALSE), new Node(Token.TRUE));
        assertFalse(NodeUtil.isForIn(forNode));

        Node notForNode = new Node(Token.VAR, new Node(Token.NAME, "i"), new Node(Token.FALSE), new Node(Token.TRUE));
        assertFalse(NodeUtil.isForIn(notForNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));

        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));

        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));

        Node otherNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node notFunctionNode = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        parentScriptNode.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node grandParentScriptNode = new Node(Token.SCRIPT);
        grandParentScriptNode.addChildToFront(parentFunctionNode);
        parentFunctionNode.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node notHoistedFunctionNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));

        Node notFunctionNode = new Node(Token.VAR);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notFunctionNode));
    }
}