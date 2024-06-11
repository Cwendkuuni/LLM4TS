import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(Token.INIT), new Node(Token.COND), new Node(Token.BODY));
        assertTrue(NodeUtil.isForIn(nodeForIn));
        
        Node nodeNotForIn = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BODY));
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node statementNode = new Node(Token.NAME);
        statementNode.setParent(parent);
        assertTrue(NodeUtil.isStatement(statementNode));
        
        Node expressionNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclaration = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
        
        Node notFunctionDeclaration = new Node(Token.COND);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node scriptParent = new Node(Token.SCRIPT);
        hoistedFunctionDeclaration.setParent(scriptParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
        
        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node functionParent = new Node(Token.FUNCTION);
        notHoistedFunctionDeclaration.setParent(functionParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}