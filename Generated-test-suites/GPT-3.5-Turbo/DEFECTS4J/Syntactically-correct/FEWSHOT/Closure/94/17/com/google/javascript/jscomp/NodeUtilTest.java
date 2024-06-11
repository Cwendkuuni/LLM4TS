package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(node));

        node.removeChild(child3);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node otherNode = new Node(Token.EMPTY);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node statementNode = new Node(Token.SCRIPT);
        Node otherNode = new Node(Token.EMPTY);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        functionNode.setParent(scriptNode);
        parentFunctionNode.setParent(scriptNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        functionNode.setParent(parentFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}