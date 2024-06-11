package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeFor = new Node(Token.FOR, 1, 2, new Node[3]);
        assertTrue(NodeUtil.isForIn(nodeFor));

        Node nodeIf = new Node(Token.IF, 1, 2, new Node[3]);
        assertFalse(NodeUtil.isForIn(nodeIf));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, 1, 2, new Node[0]);
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node nodeVar = new Node(Token.VAR, 1, 2, new Node[0]);
        assertTrue(NodeUtil.isStatement(nodeVar));

        Node nodeIf = new Node(Token.IF, 1, 2, new Node[3]);
        assertFalse(NodeUtil.isStatement(nodeIf));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, 2, new Node[1]);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, 2, new Node[1]);
        nodeFunctionExpression.setParent(new Node(Token.ASSIGN, 1, 2, new Node[]{nodeFunctionExpression, new Node(Token.IDENT, 1, 2, new String[]{"foo"})}));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeScript = new Node(Token.SCRIPT, 1, 2, new Node[0]);
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, 2, new Node[1]);
        nodeFunctionDeclaration.setLine(1);
        nodeFunctionDeclaration.setColumn(1);
        nodeFunctionDeclaration.setParent(nodeScript);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, 1, 2, new Node[1]);
        nodeFunctionDeclaration2.setLine(1);
        nodeFunctionDeclaration2.setColumn(1);
        nodeFunctionDeclaration2.setParent(new Node(Token.FUNCTION, 1, 2, new Node[0]));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));

        Node nodeFunctionDeclaration3 = new Node(Token.FUNCTION, 1, 2, new Node[1]);
        nodeFunctionDeclaration3.setLine(1);
        nodeFunctionDeclaration3.setColumn(1);
        nodeFunctionDeclaration3.setParent(new Node(Token.BLOCK, 1, 2, new Node[0]));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration3));
    }

    @Test
    public void testThrowExceptionIfInvalidParent() {
        Node nodeFor = new Node(Token.FOR, 1, 2, new Node[3]);
        checkArgument(nodeFor.getParent() != null, "Node must have non-null parent");
    }

    @Test
    public void testThrowExceptionIfInvalidParentType() {
        Node nodeFor = new Node(Token.FOR, 1, 2, new Node[3]);
        checkState(nodeFor.getParent().getType() == Token.SCRIPT || nodeFor.getParent().getType() == Token.BLOCK
                || nodeFor.getParent().getType() == Token.LABEL, "Invalid parent type for node for");
    }
}