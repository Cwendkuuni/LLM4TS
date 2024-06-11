package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node());
        node2.addChildToBack(new Node());
        
        assertFalse(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.EXPR_RESULT);
        parent2.addChildToBack(node2);
        node2.setParent(parent2);
        
        assertFalse(NodeUtil.isStatement(node2));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.EXPR_RESULT);
        parent2.addChildToBack(node2);
        node2.setParent(parent2);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        functionNode.setParent(scriptNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node functionNode2 = new Node(Token.FUNCTION);
        Node functionNode3 = new Node(Token.FUNCTION);
        Node scriptNode2 = new Node(Token.SCRIPT);
        scriptNode2.addChildToBack(functionNode2);
        functionNode2.addChildToBack(functionNode3);
        functionNode2.setParent(scriptNode2);
        functionNode3.setParent(functionNode2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode3));
        
        Node functionNode4 = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(functionNode4);
        functionNode4.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode4));
    }
}