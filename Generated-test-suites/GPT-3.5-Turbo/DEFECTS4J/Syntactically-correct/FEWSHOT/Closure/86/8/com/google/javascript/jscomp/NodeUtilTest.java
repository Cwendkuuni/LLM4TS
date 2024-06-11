
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EXPR_RESULT));
        node.addChildToFront(new Node(Token.FOR));
        node.addChildToFront(new Node(Token.NUMBER));
        node.addChildToFront(new Node(Token.NAME, new Node(Token.STRING)));
        
        assertEquals(true, NodeUtil.isForIn(node));
        
        node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.STRING));
        
        assertEquals(false, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.NAME, new Node(Token.STRING)));
        
        assertEquals(true, NodeUtil.isStatement(node));
        
        node = new Node(Token.IF);
        node.addChildToBack(new Node(Token.STRING));
        
        assertEquals(false, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME, new Node(Token.STRING)));
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.FUNCTION));
        
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME, new Node(Token.STRING)));
        node.getParent().addChildToFront(new Node(Token.SCRIPT));
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.FUNCTION));
        
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
