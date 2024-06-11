
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.NAME);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(node));

        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.NAME);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        Node notStatementNode = new Node(Token.NAME);
        notStatementNode.setParent(null);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node notFunctionDeclaration = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.BLOCK);
        notFunctionDeclaration.setParent(parent2);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        notHoistedFunctionDeclaration.setParent(parent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}
