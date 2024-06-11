package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME, "i");
        Node child3 = new Node(Token.NAME, "array");
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.FOR);
        Node child12 = new Node(Token.VAR);
        Node child22 = new Node(Token.NAME, "i");
        Node child32 = new Node(Token.NAME, "list");
        Node child42 = new Node(Token.NAME, "array");
        node2.addChildToBack(child12);
        node2.addChildToBack(child22);
        node2.addChildToBack(child32);
        node2.addChildToBack(child42);
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME, "i");
        Node child3 = new Node(Token.NAME, "array");
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        assertTrue(NodeUtil.isStatement(child2));
        
        Node node2 = new Node(Token.CALL);
        Node child12 = new Node(Token.NAME, "console.log");
        Node child22 = new Node(Token.STRING, "hello world");
        node2.addChildToBack(child12);
        node2.addChildToBack(child22);
        assertFalse(NodeUtil.isStatement(child22));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node child1 = new Node(Token.NAME, "myFunction");
        Node child2 = new Node(Token.PARAM_LIST);
        Node child3 = new Node(Token.SCRIPT);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        Node node2 = new Node(Token.FUNCTION);
        Node child12 = new Node(Token.NAME, "myFunction");
        Node child22 = new Node(Token.PARAM_LIST);
        Node child32 = new Node(Token.EXPR_RESULT);
        node2.addChildToBack(child12);
        node2.addChildToBack(child22);
        node2.addChildToBack(child32);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node child1 = new Node(Token.NAME, "myFunction");
        Node child2 = new Node(Token.PARAM_LIST);
        Node child3 = new Node(Token.SCRIPT);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
        Node node2 = new Node(Token.FUNCTION);
        Node child12 = new Node(Token.NAME, "myFunction");
        Node child22 = new Node(Token.PARAM_LIST);
        Node child32 = new Node(Token.EXPR_RESULT);
        node2.addChildToBack(child12);
        node2.addChildToBack(child22);
        node2.addChildToBack(child32);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(node2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}