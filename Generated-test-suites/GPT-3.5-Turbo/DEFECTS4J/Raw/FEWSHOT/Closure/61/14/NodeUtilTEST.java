
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn_True() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        n.addChildToFront(child1);
        n.addChildToFront(child2);
        n.addChildToFront(child3);
        
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_False_NotForLoopNodeType() {
        Node n = new Node(Token.WHILE);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        n.addChildToFront(child1);
        n.addChildToFront(child2);
        n.addChildToFront(child3);
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_False_InvalidChildCount() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        n.addChildToFront(child1);
        n.addChildToFront(child2);
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement_True() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        n.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_False() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.FUNCTION);
        n.setParent(parent);
        
        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_True() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_False_NotStatement() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        n.setParent(parent);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_False_NotFunctionType() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node n = new Node(Token.FUNCTION);
        
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        n.setParent(parent1);
        parent1.setParent(parent2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_False_NotDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        Node parent1 = new Node(Token.BLOCK);
        Node parent2 = new Node(Token.FUNCTION);
        n.setParent(parent1);
        parent1.setParent(parent2);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_False_NotInRootScope() {
        Node n = new Node(Token.FUNCTION);
        
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.WHILE);
        n.setParent(parent1);
        parent1.setParent(parent2);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}
