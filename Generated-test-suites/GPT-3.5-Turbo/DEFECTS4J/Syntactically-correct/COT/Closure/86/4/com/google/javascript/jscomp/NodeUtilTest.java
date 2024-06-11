
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(forInNode));
        
        Node notForInNode = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isForIn(notForInNode));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node expressionNode = new Node(Token.EMPTY);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        
        assertFalse(NodeUtil.isStatement(expressionNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.setParent(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        
        Node notFunctionDeclarationNode = new Node(Token.FUNCTION);
        notFunctionDeclarationNode.setParent(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionNode = new Node(Token.FUNCTION);
        hoistedFunctionNode.setParent(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
        
        Node notHoistedFunctionNode = new Node(Token.FUNCTION);
        notHoistedFunctionNode.setParent(new Node(Token.BLOCK));
        notHoistedFunctionNode.getParent().setParent(new Node(Token.FUNCTION));
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
    }
}
