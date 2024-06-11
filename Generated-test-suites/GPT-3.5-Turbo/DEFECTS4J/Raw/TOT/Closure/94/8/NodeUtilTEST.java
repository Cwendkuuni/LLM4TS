package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalUseCase() {
        // Typical use case: Node represents a FOR-IN loop
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.IN);
        Node child3 = new Node(Token.BLOCK);
        n.addChildToFront(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_notForInLoop() {
        // Node does not represent a FOR-IN loop
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.LT);
        Node child3 = new Node(Token.BLOCK);
        n.addChildToFront(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement_statement() {
        // Node is used as a statement
        Node n = new Node(Token.SCRIPT);
        Node child = new Node(Token.BLOCK);
        n.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsStatement_notStatement() {
        // Node is not used as a statement
        Node n = new Node(Token.FOR);
        Node child = new Node(Token.VAR);
        n.addChildToFront(child);

        assertFalse(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsFunctionDeclaration_functionDeclaration() {
        // Node is a function declaration
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_notFunctionDeclaration() {
        // Node is not a function declaration
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_hoistedFunctionDeclaration() {
        // Node is a hoisted function declaration
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
        // Node is not a hoisted function declaration
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(n);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    } 
}