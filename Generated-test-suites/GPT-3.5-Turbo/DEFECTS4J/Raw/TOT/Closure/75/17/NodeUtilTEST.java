import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        NodeUtil nodeUtil = new NodeUtil();
        
        // Typical Use Case
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.VAR));
        forInNode.addChildToBack(new Node(Token.TRUE));
        forInNode.addChildToBack(new Node(Token.FALSE));
        assertTrue(nodeUtil.isForIn(forInNode));
        
        // Edge Case
        Node nonForInNode = new Node(Token.IF);
        assertFalse(nodeUtil.isForIn(nonForInNode));
    }

    @Test
    public void testIsStatement() {
        NodeUtil nodeUtil = new NodeUtil();
        
        // Typical Use Case
        Node statementNode = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(statementNode);
        assertTrue(nodeUtil.isStatement(statementNode));
        
        // Edge Case
        Node nonStatementNode = new Node(Token.IF);
        assertFalse(nodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        NodeUtil nodeUtil = new NodeUtil();
        
        // Typical Use Case
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        assertTrue(nodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Edge Cases
        Node nonFunctionDeclarationNode = new Node(Token.IF);
        assertFalse(nodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));

        Node functionExpression = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionExpression);
        assertFalse(nodeUtil.isFunctionDeclaration(functionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        NodeUtil nodeUtil = new NodeUtil();
        
        // Typical Use Case
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(hoistedFunctionDeclarationNode);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // Edge Cases
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));

        Node functionExpression = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionExpression);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionExpression));
    }
}