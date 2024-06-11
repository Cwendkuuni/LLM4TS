package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new int[0]);
        nodeForIn.setChild(0, new Node(Token.IDENT, new int[0], "for"));
        nodeForIn.setChild(1, new Node(Token.ASSIGN, new int[0], null, new Node(Token.IDENT, new int[0], "i"), new Node(Token.INT_LITERAL, new int[0], 0)));
        nodeForIn.setChild(2, new Node(Token.BLOCK, new int[0]));

        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IDENT, new int[0], "notForIn");
        assertFalse(NodeUtil.isForIn(nodeNotForIn));

        Node nodeStatement = new Node(Token.PRINT, new int[0], null, new Node(Token.STRING_LITERAL, new int[0], "Hello World!"));
        assertFalse(NodeUtil.isForIn(nodeStatement));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, new int[0]);
        Node nodeStatement1 = new Node(Token.PRINT, new int[0], null, new Node(Token.STRING_LITERAL, new int[0], "Hello World!"));
        Node nodeStatement2 = new Node(Token.VAR, new int[0], null, new Node(Token.IDENT, new int[0], "x"), new Node(Token.ASSIGN, new int[0], null, new Node(Token.INT_LITERAL, new int[0], 5)));

        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeStatement1));
        assertTrue(NodeUtil.isStatement(nodeStatement2));

        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[0], null, new Node(Token.IDENT, new int[0], "foo"), new Node(Token.BLOCK, new int[0]), new Node(Token.IDENT, new int[0], "bar"));
        assertFalse(NodeUtil.isStatement(nodeFunctionExpression));

        Node nodeNull = null;
        assertNull(NodeUtil.isStatement(nodeNull));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[0], null, new Node(Token.IDENT, new int[0], "foo"), new Node(Token.BLOCK, new int[0]), new Node(Token.IDENT, new int[0], "bar"));

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[0], null, null, new Node(Token.BLOCK, new int[0]), new Node(Token.IDENT, new int[0], "bar"));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

        Node nodeStatement = new Node(Token.PRINT, new int[0], null, new Node(Token.STRING_LITERAL, new int[0], "Hello World!"));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeStatement));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[0], null, new Node(Token.IDENT, new int[0], "foo"), new Node(Token.BLOCK, new int[0]), new Node(Token.IDENT, new int[0], "bar"));
        Node nodeFunctionDeclarationInScript = new Node(Token.SCRIPT, new int[0], null, nodeFunctionDeclarationInFunction);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

        Node nodeFunctionDeclarationOutsideFunction = new Node(Token.SCRIPT, new int[0], null, new Node(Token.FUNCTION, new int[0], null, new Node(Token.IDENT, new int[0], "foo"), new Node(Token.BLOCK, new int[0]), new Node(Token.IDENT, new int[0], "bar")));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideFunction));

        Node nodeStatement = new Node(Token.PRINT, new int[0], null, new Node(Token.STRING_LITERAL, new int[0], "Hello World!"));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeStatement));
    }
}