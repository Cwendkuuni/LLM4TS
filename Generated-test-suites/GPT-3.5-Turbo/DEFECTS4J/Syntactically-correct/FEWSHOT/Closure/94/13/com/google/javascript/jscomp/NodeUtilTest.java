package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        assertFalse(NodeUtil.isForIn(null));
        
        Node node1 = new Node(Token.FOR, null, null);
        assertFalse(NodeUtil.isForIn(node1));
        
        Node node2 = new Node(Token.FOR, null, new Node());
        assertFalse(NodeUtil.isForIn(node2));
        
        Node node3 = new Node(Token.FOR, null, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node3));
    }
    
    @Test
    public void testIsStatement() {
        assertFalse(NodeUtil.isStatement(null));
        
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));
        
        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));
        
        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node nonStatementParent = new Node(Token.ASSIGN, new Node(), new Node());
        assertFalse(NodeUtil.isStatement(nonStatementParent));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        
        Node functionNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node statementFunctionNode = new Node(Token.FUNCTION);
        statementFunctionNode.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(statementFunctionNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        
        Node functionNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node nonHoistedStatementFunctionNode = new Node(Token.FUNCTION);
        nonHoistedStatementFunctionNode.setParent(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedStatementFunctionNode));
        
        Node hoistedStatementFunctionNode = new Node(Token.FUNCTION);
        hoistedStatementFunctionNode.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedStatementFunctionNode));
        
        Node hoistedFunctionNode = new Node(Token.FUNCTION);
        hoistedFunctionNode.setParent(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    }
}