package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3}, new int[] {0, 0, 0});
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2}, new int[] {0, 0});
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node nodeExpression = new Node(Token.IDENT, new int[] {1}, new int[] {0});
        assertFalse(NodeUtil.isStatement(nodeExpression));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        nodeFunction.setProperty("id", "myFunction");

        Node nodeNotFunction = new Node(Token.IF, new int[] {1, 2}, new int[] {0, 0});
        Node parentScript = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
        parentScript.setChild(0, nodeNotFunction);

        Node nodeFunctionStatement = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        nodeFunctionStatement.setProperty("id", "myFunctionStatement");
        Node parentBlock = new Node(Token.BLOCK, new int[] {1}, new int[] {0});
        parentBlock.setChild(0, nodeFunctionStatement);

        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        nodeFunctionDeclaration.setProperty("id", "myFunctionDeclaration");
        Node parentScript2 = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
        parentScript2.setChild(0, nodeFunctionDeclaration);

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionStatement));
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        nodeFunctionDeclaration.setProperty("id", "myFunctionDeclaration");
        Node parentScript = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
        parentScript.setChild(0, nodeFunctionDeclaration);

        Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        nodeFunctionDeclaration2.setProperty("id", "myFunctionDeclaration2");
        Node parentFunction = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {0, 0, 0});
        parentFunction.setProperty("id", "myFunction");
        parentFunction.setChild(0, nodeFunctionDeclaration2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));
    }
}