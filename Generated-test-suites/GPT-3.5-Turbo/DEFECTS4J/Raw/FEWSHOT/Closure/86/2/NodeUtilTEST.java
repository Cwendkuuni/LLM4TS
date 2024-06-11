import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Test when node represents a FOR-IN loop
        Node forInLoop = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInLoop));
        
        // Test when node does not represent a FOR-IN loop
        Node notForInLoop = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(notForInLoop));
    }
    
    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(scriptNode));
        
        Node blockNode = new Node(Token.BLOCK, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(blockNode));
        
        Node labelNode = new Node(Token.LABEL, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(labelNode));
        
        // Test when node is not used as a statement
        Node notStatementNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node functionDeclaration = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
        
        // Test when node is not a function declaration
        Node notFunctionDeclaration = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        Node parentScriptNode = new Node(Token.SCRIPT, hoistedFunctionDeclaration);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
        
        Node parentFunctionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        parentScriptNode = new Node(Token.FUNCTION, parentFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
        
        // Test when node is not a hoisted function declaration
        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        Node notParentScriptNode = new Node(Token.EMPTY, notHoistedFunctionDeclaration);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}