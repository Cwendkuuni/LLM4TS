
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToFront(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        // Test case 1: n.getType() == Token.FOR and n.getChildCount() == 3
        node.setType(Token.FOR);
        child1.setType(Token.EMPTY);
        child2.setType(Token.EMPTY);
        child3.setType(Token.EMPTY);
        
        assertEquals(true, NodeUtil.isForIn(node));
        
        // Test case 2: n.getType() != Token.FOR
        node.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isForIn(node));
        
        // Test case 3: n.getChildCount() != 3
        node.setType(Token.FOR);
        child1.setType(Token.EMPTY);
        child2.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        // Test case 1: parent.getType() == Token.SCRIPT
        parent.setType(Token.SCRIPT);
        
        assertEquals(true, NodeUtil.isStatement(node));
        
        // Test case 2: parent.getType() != Token.SCRIPT
        parent.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isStatement(node));
        
        // Test case 3: parent == null
        node.setParent(null);
        
        assertEquals(false, NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        // Test case 1: node.getType() == Token.FUNCTION and isStatement(n) == true
        node.setType(Token.FUNCTION);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
        
        // Test case 2: node.getType() != Token.FUNCTION
        node.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
        
        // Test case 3: isStatement(n) == false
        node.setType(Token.FUNCTION);
        parent.setType(Token.SCRIPT);
        
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        node.setParent(parent1);
        parent1.setParent(parent2);
        
        // Test case 1: isFunctionDeclaration(n) == true and (n.getParent().getType() == Token.SCRIPT or n.getParent().getParent().getType() == Token.FUNCTION)
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
        
        // Test case 2: isFunctionDeclaration(n) == false
        node.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
        
        // Test case 3: (n.getParent().getType() != Token.SCRIPT and n.getParent().getParent().getType() != Token.FUNCTION)
        node.setType(Token.FUNCTION);
        parent1.setType(Token.EMPTY);
        parent2.setType(Token.EMPTY);
        
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
