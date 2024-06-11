
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node statementNode = new Node(Token.EMPTY);
        scriptNode.addChildToBack(statementNode);
        
        assertTrue(NodeUtil.isStatement(statementNode));
    }
    
    @Test
    public void testIsNotStatement() {
        Node exprNode = new Node(Token.EXPR_RESULT);
        
        assertFalse(NodeUtil.isStatement(exprNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(parentBlockNode);
        parentBlockNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(parentFunctionNode);
        parentFunctionNode.addChildToBack(functionNode);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}
