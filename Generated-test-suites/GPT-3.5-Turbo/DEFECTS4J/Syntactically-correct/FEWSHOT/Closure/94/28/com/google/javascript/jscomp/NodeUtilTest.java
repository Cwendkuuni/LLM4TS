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
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(forInNode));
        
        Node otherNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(otherNode));
    }
    
    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.FUNCTION);
        statementNode.addChildToBack(childNode);
        
        assertTrue(NodeUtil.isStatement(childNode));
        
        Node otherNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isStatement(otherNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionDecNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(functionDecNode);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDecNode));
        
        Node otherNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDecNode = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        parent1.addChildToBack(functionDecNode);
        parent2.addChildToBack(parent1);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDecNode));
        
        Node otherNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(otherNode));
    }
}