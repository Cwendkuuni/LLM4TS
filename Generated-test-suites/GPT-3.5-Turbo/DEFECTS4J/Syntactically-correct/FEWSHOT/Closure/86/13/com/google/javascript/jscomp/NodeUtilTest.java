package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn_true() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.IN);
        Node child3 = new Node(Token.EMPTY);
        
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_false() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.IN);
        
        n.addChildToBack(child1);
        n.addChildToBack(child2);

        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement_true() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        
        n.setParent(parent);

        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_true() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.BLOCK);
        
        n.setParent(parent);
        parent.addChildToBack(grandParent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.IF);
        Node grandParent = new Node(Token.SCRIPT);
        
        n.setParent(parent);
        parent.addChildToBack(grandParent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_true() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.BLOCK);
        
        n.setParent(parent);
        parent.addChildToBack(grandParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        
        n.setParent(parent);
        parent.setParent(grandParent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}