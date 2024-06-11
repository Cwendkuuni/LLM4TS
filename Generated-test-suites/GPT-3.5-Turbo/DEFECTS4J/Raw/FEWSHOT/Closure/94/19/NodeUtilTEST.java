
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
        
        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToBack(new Node(Token.EMPTY));
        notForInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(notForInNode));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node expressionNode = new Node(Token.EXPR_RESULT);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertTrue(NodeUtil.isStatement(expressionNode));
    }
    
    @Test
    public void testIsNotStatement() {
        Node otherNode = new Node(Token.ADD);
        
        assertFalse(NodeUtil.isStatement(otherNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        parentBlockNode.addChildToBack(functionNode);
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node notFunctionNode = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node otherNode = new Node(Token.ADD);
        
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node notFunctionDeclarationNode = new Node(Token.NOT);
        notFunctionDeclarationNode.addChildToBack(functionNode);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node otherNode = new Node(Token.ADD);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(otherNode));
    }
}
