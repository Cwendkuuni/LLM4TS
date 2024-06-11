package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node());
        node2.addChildToBack(new Node());
        node2.addChildToBack(new Node());
        
        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.FUNCTION);
        
        Node parent1 = new Node();
        Node parent2 = new Node();
        Node parent3 = new Node();
        
        node1.setParent(parent1);
        node2.setParent(parent2);
        node3.setParent(parent3);
        node4.setParent(parent4);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node());
        
        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(new Node(Token.SCRIPT));
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(new Node(Token.FUNCTION));
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}