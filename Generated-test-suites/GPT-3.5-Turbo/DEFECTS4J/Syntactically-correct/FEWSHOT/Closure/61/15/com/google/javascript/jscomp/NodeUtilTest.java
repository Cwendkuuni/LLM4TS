
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.IN));
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.IN), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node block = new Node(Token.BLOCK);
        Node label = new Node(Token.LABEL);

        Node node1 = new Node(Token.ADD);
        node1.setParent(script);
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.ADD);
        node2.setParent(block);
        assertTrue(NodeUtil.isStatement(node2));

        Node node3 = new Node(Token.ADD);
        node3.setParent(label);
        assertTrue(NodeUtil.isStatement(node3));

        Node node4 = new Node(Token.ADD);
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(function));

        Node add = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(add));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function1 = new Node(Token.FUNCTION);
        Node function2 = new Node(Token.FUNCTION);
        function2.setParent(new Node(Token.ADD));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function2));

        Node add = new Node(Token.ADD);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(add));
    }
}
