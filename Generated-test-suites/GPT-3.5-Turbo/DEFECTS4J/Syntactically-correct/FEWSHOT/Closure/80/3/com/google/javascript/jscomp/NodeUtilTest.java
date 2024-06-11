package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        assertFalse(NodeUtil.isForIn(null));
        
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
        
        Node forNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        forNode.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(forNode));
        
        Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        assertFalse(NodeUtil.isStatement(null));
        
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
        
        parent = new Node(Token.BLOCK);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
        
        parent = new Node(Token.LABEL);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));
        
        parent = new Node(Token.EMPTY);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        
        Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
        
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        functionNode.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        functionNode.setParent(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        functionNode.setParent(new Node(Token.LABEL));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        
        Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        functionNode.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node parent = new Node(Token.FUNCTION);
        parent.setParent(new Node(Token.SCRIPT));
        functionNode.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.setParent(new Node(Token.FUNCTION));
        parent = new Node(Token.FUNCTION);
        parent.setParent(grandparent);
        functionNode.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}