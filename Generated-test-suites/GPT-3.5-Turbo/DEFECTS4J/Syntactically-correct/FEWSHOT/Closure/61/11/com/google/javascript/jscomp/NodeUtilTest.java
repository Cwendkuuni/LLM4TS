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
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.addChildToBack(node);
        parent.addChildToBack(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}