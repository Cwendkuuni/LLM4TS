import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));
        
        Node notForIn = new Node(Token.FUNCTION, new Node(Token.THIS), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(notForIn));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node statementNode = new Node(Token.BLOCK);
        Node notStatementNode = new Node(Token.IF);
        
        scriptNode.addChildToFront(statementNode);
        statementNode.addChildToFront(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isStatement(statementNode));
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDecl = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.THIS));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDecl));
        
        Node notFunctionDecl = new Node(Token.IF, new Node(Token.NAME));
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDecl));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDecl = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.THIS));
        Node parentScript = new Node(Token.SCRIPT, new Node(Token.VAR), functionDecl);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDecl));
        
        Node parentFunction = new Node(Token.FUNCTION, new Node(Token.NAME), functionDecl);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDecl));

    }
}