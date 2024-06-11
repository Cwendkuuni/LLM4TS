

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Edge cases
        assertFalse(NodeUtil.isForIn(null)); // edge case: null input
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR))); // edge case: non-FOR node
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR, new Node()))); // edge case: FOR node with less than 3 children
        
        // Additional test cases
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(), new Node(), new Node())));
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(), new Node(), new Node(), new Node()))); 
    }

    @Test
    public void testIsStatement() {
        // Edge case
        assertFalse(NodeUtil.isStatement(null)); // edge case: null input
        
        // Additional test cases
        assertTrue(NodeUtil.isStatement(new Node(Token.SCRIPT)));
        assertTrue(NodeUtil.isStatement(new Node(Token.BLOCK)));
        assertTrue(NodeUtil.isStatement(new Node(Token.LABEL)));
        assertFalse(NodeUtil.isStatement(new Node(Token.NAME)));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Edge cases
        assertFalse(NodeUtil.isFunctionDeclaration(null)); // edge case: null input
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION_EXPRESSION))); // edge case: non-FUNCTION node
        
        // Additional test cases
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION)));
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME))));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Edge cases
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null)); // edge case: null input
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION))); // edge case: non-FUNCTION node
        
        // Additional test cases
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME))));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME), new Node())));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME), new Node(), new Node())));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME), new Node(), new Node(), new Node()))); 
    }
}

