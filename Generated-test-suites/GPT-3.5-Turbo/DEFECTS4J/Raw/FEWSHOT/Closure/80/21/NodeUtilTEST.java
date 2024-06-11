package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        assertFalse(NodeUtil.isForIn(null));
        assertFalse(NodeUtil.isForIn(new Node()));
        Node forInNode = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        assertFalse(NodeUtil.isStatement(null));
        assertFalse(NodeUtil.isStatement(new Node()));
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node parent1 = new Node();
        Node parent2 = new Node();
        Node parent3 = new Node();
        parent1.addChildToFront(scriptNode);
        parent2.addChildToFront(blockNode);
        parent3.addChildToFront(labelNode);
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(parent1));
        assertFalse(NodeUtil.isStatement(parent2));
        assertFalse(NodeUtil.isStatement(parent3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        assertFalse(NodeUtil.isFunctionDeclaration(new Node()));
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node();
        parent.addChildToFront(functionNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(parent));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node()));
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node parent1 = new Node();
        Node parent2 = new Node();
        parent1.addChildToFront(scriptNode);
        parent2.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(parent1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent2));
    }
}