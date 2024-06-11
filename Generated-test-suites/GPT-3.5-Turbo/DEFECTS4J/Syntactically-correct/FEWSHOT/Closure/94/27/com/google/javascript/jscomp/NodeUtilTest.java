package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Test for node representing a for-in loop
        Node forInNode = new Node(Token.FOR, 
            new Node(Token.NAME),
            new Node(Token.NUMBER),
            new Node(Token.BLOCK));
        assertTrue(NodeUtil.isForIn(forInNode));
        
        // Test for node not representing a for-in loop
        Node notForInNode = new Node(Token.FOR, 
            new Node(Token.NAME),
            new Node(Token.NUMBER));
        assertFalse(NodeUtil.isForIn(notForInNode));
    }
    
    @Test
    public void testIsStatement() {
        // Test for node being used as a statement
        Node statementNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(statementNode));
        
        // Test for node not being used as a statement
        Node notStatementNode = new Node(Token.ADD);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        // Test for node representing a function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        
        // Test for node not representing a function declaration
        Node notFunctionDeclarationNode = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for hoisted function declaration
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToFront(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
        
        // Test for non-hoisted function declaration
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}