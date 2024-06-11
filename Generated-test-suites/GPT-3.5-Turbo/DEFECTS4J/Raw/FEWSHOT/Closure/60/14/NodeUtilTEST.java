
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forNode = new Node(Token.FOR);
        forNode.addChildToBack(new Node(Token.NAME));
        forNode.addChildToBack(new Node(Token.NAME));
        forNode.addChildToBack(new Node(Token.NAME));
        
        assertEquals(true, NodeUtil.isForIn(forNode));
        
        Node notForNode = new Node(Token.NAME);
        assertEquals(false, NodeUtil.isForIn(notForNode));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        
        Node parentNode = new Node(Token.NAME);
        
        scriptNode.addChildToBack(parentNode);
        blockNode.addChildToBack(parentNode);
        labelNode.addChildToBack(parentNode);
        
        assertEquals(true, NodeUtil.isStatement(scriptNode));
        assertEquals(true, NodeUtil.isStatement(blockNode));
        assertEquals(true, NodeUtil.isStatement(labelNode));
        
        Node notStatementNode = new Node(Token.NAME);
        assertEquals(false, NodeUtil.isStatement(notStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        
        scriptNode.addChildToBack(functionNode);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(functionNode));
        
        Node notFunctionDeclarationNode = new Node(Token.NAME);
        assertEquals(false, NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionParentNode = new Node(Token.FUNCTION);
        
        scriptNode.addChildToBack(functionNode);
        functionNode.addChildToBack(functionParentNode);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node notHoistedFunctionDeclarationNode = new Node(Token.NAME);
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}
