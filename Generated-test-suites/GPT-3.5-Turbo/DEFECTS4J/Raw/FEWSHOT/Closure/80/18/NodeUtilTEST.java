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
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(forInNode));
        
        Node notForInNode = new Node(Token.WHILE);
        notForInNode.addChildToBack(new Node(Token.EMPTY));
        notForInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(notForInNode));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node notStatementNode = new Node(Token.NAME);
        notStatementNode.getParent().addChildToFront(new Node(Token.SCRIPT));
        
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        
        Node notFunctionDeclarationNode = new Node(Token.MUL);
        
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node notHoistedFunctionNode = new Node(Token.FUNCTION);
        Node notHoistedScriptNode = new Node(Token.SCRIPT);
        notHoistedScriptNode.addChildToBack(notHoistedFunctionNode);
        notHoistedFunctionNode.getParent().addChildToFront(new Node(Token.FUNCTION));
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
    }
}