import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test with invalid types
        assertFalse(NodeUtil.isForIn(null));
        assertFalse(NodeUtil.isForIn(new Node(null)));

        // Test with valid types
        Node forInNode = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        // Test with invalid types
        assertFalse(NodeUtil.isStatement(null));

        // Test with valid types
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        
        Node statementNode1 = new Node();
        statementNode1.setParent(scriptNode);
        assertTrue(NodeUtil.isStatement(statementNode1));

        Node statementNode2 = new Node();
        statementNode2.setParent(blockNode);
        assertTrue(NodeUtil.isStatement(statementNode2));

        Node statementNode3 = new Node();
        statementNode3.setParent(labelNode);
        assertTrue(NodeUtil.isStatement(statementNode3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test with invalid types
        assertFalse(NodeUtil.isFunctionDeclaration(null));

        // Test with valid types
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);

        functionDeclarationNode.setParent(scriptNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        functionDeclarationNode.setParent(blockNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test with invalid types
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));

        // Test with valid types
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node functionNode = new Node(Token.FUNCTION);

        functionDeclarationNode.setParent(scriptNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

        functionDeclarationNode.setParent(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

        functionDeclarationNode.setParent(blockNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    }
}