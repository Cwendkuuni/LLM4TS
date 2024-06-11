import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Test for FOR loop
        assertFalse(NodeUtil.isForIn(createNode(Token.FOR, 3)));
        // Test for non-FOR loop
        assertFalse(NodeUtil.isForIn(createNode(Token.WHILE, 2)));
        // Test for FOR-IN loop
        assertTrue(NodeUtil.isForIn(createNode(Token.FOR, 3, Token.IN)));
    }

    @Test
    public void testIsStatement() {
        // Test for Script node
        assertTrue(NodeUtil.isStatement(createNode(Token.SCRIPT)));
        // Test for Block node
        assertTrue(NodeUtil.isStatement(createNode(Token.BLOCK)));
        // Test for Label node
        assertTrue(NodeUtil.isStatement(createNode(Token.LABEL)));
        // Test for non-Statement node
        assertFalse(NodeUtil.isStatement(createNode(Token.IF)));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test for Function declaration
        assertTrue(NodeUtil.isFunctionDeclaration(createNode(Token.FUNCTION)));
        // Test for non-Function declaration
        assertFalse(NodeUtil.isFunctionDeclaration(createNode(Token.EXPR_RESULT)));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for hoisted Function declaration in Script
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, Token.SCRIPT)));
        // Test for hoisted Function declaration in Function
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, Token.FUNCTION, Token.SCRIPT)));
        // Test for non-hoisted Function declaration
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, Token.BLOCK)));
    }

    private Node createNode(int type, int... childTypes) {
        Node node = new Node(type);
        for (int childType : childTypes) {
            node.addChildToBack(new Node(childType));
        }
        return node;
    }
}