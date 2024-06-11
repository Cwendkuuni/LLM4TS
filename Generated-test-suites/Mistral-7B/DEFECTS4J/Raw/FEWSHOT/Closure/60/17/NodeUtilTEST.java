package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3, 4, 5}, null);
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2, 3}, null);
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3}, null);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        Node nodeExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
        assertFalse(NodeUtil.isStatement(nodeExpression));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {new Node("name", new int[] {1, 2}, null), new Node(Token.BLOCK, new int[] {1, 2, 3}, null)});
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {new Node("name", new int[] {1, 2}, null), new Node(Token.BLOCK, new int[] {1, 2, 3}, null)});
        Node parentScript = new Node(Token.SCRIPT, new int[] {}, null);
        Node parentFunction = new Node(Token.FUNCTION, new int[] {1, 2}, null);
        parentFunction.setChild(0, parentScript);
        parentFunction.setChild(1, nodeFunctionDeclaration);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionDeclarationWithoutParent = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {new Node("name", new int[] {1, 2}, null), new Node(Token.BLOCK, new int[] {1, 2, 3}, null)});
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationWithoutParent));
    }

    @Test
    public void testIsNull() {
        assertNull(NodeUtil.stripLeadingHyphens(null));
    }

    private void assertFalse(boolean condition) {
        org.junit.Assert.assertFalse(condition);
    }

    @Test
    public void testIsFunctionExpression() {
        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
        assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));

        Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3}, null);
        assertFalse(NodeUtil.isFunctionExpression(nodeStatement));
    }

    private static boolean assertNotNull(Object obj) {
        checkNotNull(obj);
        return true;
    }
}