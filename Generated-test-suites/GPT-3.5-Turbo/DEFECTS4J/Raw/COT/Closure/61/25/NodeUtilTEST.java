import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
        
        Node forInNode = new Node(Token.FOR, new Node(), new Node(), null);
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));
        
        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));
        
        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node expressionNode = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        functionDeclarationNode.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        
        Node functionExpressionNode = new Node(Token.FUNCTION);
        Node parentExpr = new Node(Token.EXPR_RESULT);
        functionExpressionNode.setParent(parentExpr);
        assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        functionDeclarationNode.setParent(parentScript);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
        
        Node functionDeclarationNode2 = new Node(Token.FUNCTION);
        Node parentFunc = new Node(Token.FUNCTION);
        Node parentScript2 = new Node(Token.SCRIPT);
        parentFunc.setParent(parentScript2);
        functionDeclarationNode2.setParent(parentFunc);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode2));
        
        Node functionExpressionNode = new Node(Token.FUNCTION);
        Node parentExpr = new Node(Token.EXPR_RESULT);
        functionExpressionNode.setParent(parentExpr);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    }
}