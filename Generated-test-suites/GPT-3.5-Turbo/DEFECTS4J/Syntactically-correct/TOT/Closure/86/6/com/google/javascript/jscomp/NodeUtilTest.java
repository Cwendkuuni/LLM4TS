import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        nodeForIn.addChildToFront(child1);
        nodeForIn.addChildToFront(child2);
        nodeForIn.addChildToFront(child3);

        assertTrue(NodeUtil.isForIn(nodeForIn));
        
        Node nodeNotForIn = new Node(Token.FOR);
        Node child4 = new Node(Token.EMPTY);
        nodeNotForIn.addChildToFront(child4);
        
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }
    
    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(nodeScript));
        
        Node nodeBlock = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(nodeBlock));
        
        Node nodeLabel = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(nodeLabel));
        
        Node nodeOther = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(nodeOther));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclaration = new Node(Token.FUNCTION);
        Node parentStatement = new Node(Token.SCRIPT);
        parentStatement.addChildToFront(functionDeclaration);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
        
        Node functionExpression = new Node(Token.FUNCTION);
        Node parentExpression = new Node(Token.EMPTY);
        parentExpression.addChildToFront(functionExpression);
        
        assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDeclarationHoisted = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        parentScript.addChildToFront(functionDeclarationHoisted);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationHoisted));
        
        Node functionDeclarationNotHoisted = new Node(Token.FUNCTION);
        Node parentFunction = new Node(Token.FUNCTION);
        parentFunction.addChildToFront(functionDeclarationNotHoisted);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNotHoisted));
    }

}