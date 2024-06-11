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
        Node n = new Node(Token.FOR);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();
        
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(n));
        
        child3.detachFromParent();
        
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node();
        
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        Node parent3 = new Node(Token.LABEL);
        Node parent4 = new Node();
        
        n.addChildToBack(parent1);
        assertTrue(NodeUtil.isStatement(parent1));

        n.addChildToBack(parent2);
        assertTrue(NodeUtil.isStatement(parent2));

        n.addChildToBack(parent3);
        assertTrue(NodeUtil.isStatement(parent3));

        n.addChildToBack(parent4);
        assertFalse(NodeUtil.isStatement(parent4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        Node parent1 = new Node();
        Node parent2 = new Node(Token.SCRIPT);
        Node parent3 = new Node(Token.FUNCTION);
        
        parent1.addChildToBack(n);
        assertFalse(NodeUtil.isFunctionDeclaration(n));

        parent2.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent3.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node();
        
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node grandparent1 = new Node();
        Node grandparent2 = new Node();
        
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(n);
        
        parent1.addChildToBack(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        grandparent1.addChildToBack(functionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        parent2.addChildToBack(functionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        grandparent2.addChildToBack(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}