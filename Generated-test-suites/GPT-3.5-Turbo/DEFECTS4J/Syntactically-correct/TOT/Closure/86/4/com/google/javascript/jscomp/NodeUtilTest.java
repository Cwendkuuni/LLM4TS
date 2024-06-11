import com.google.javascript.rhino.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeUtilTest {

    @Test
    void testIsForIn_whenValidForInNode_returnTrue() {
        // Arrange
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        
        // Act
        boolean isForIn = NodeUtil.isForIn(forInNode);
        
        // Assert
        Assertions.assertTrue(isForIn);
    }
    
    @Test
    void testIsForIn_whenInvalidForInNode_returnFalse() {
        // Arrange
        Node invalidForInNode = new Node(Token.FOR);
        invalidForInNode.addChildToFront(new Node(Token.EMPTY));
        invalidForInNode.addChildToFront(new Node(Token.EMPTY));
        
        // Act
        boolean isForIn = NodeUtil.isForIn(invalidForInNode);
        
        // Assert
        Assertions.assertFalse(isForIn);
    }
    
    @Test
    void testIsStatement_whenStatementNode_returnTrue() {
        // Arrange
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
        statementNode.addChildToFront(childNode);
        
        // Act
        boolean isStatement = NodeUtil.isStatement(childNode);
        
        // Assert
        Assertions.assertTrue(isStatement);
    }
    
    @Test
    void testIsStatement_whenNonStatementNode_returnFalse() {
        // Arrange
        Node nonStatementNode = new Node(Token.FUNCTION);
        
        // Act
        boolean isStatement = NodeUtil.isStatement(nonStatementNode);
        
        // Assert
        Assertions.assertFalse(isStatement);
    }
    
    @Test
    void testIsFunctionDeclaration_whenFunctionDeclarationNode_returnTrue() {
        // Arrange
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(functionDeclarationNode);
        functionDeclarationNode.putBooleanProp(Node.FUNCTION_EXPRESSION, false);
        
        // Act
        boolean isFunctionDeclaration = NodeUtil.isFunctionDeclaration(functionDeclarationNode);
        
        // Assert
        Assertions.assertTrue(isFunctionDeclaration);
    }
    
    @Test
    void testIsFunctionDeclaration_whenNonDeclarationNode_returnFalse() {
        // Arrange
        Node nonDeclarationNode = new Node(Token.BLOCK);
        
        // Act
        boolean isFunctionDeclaration = NodeUtil.isFunctionDeclaration(nonDeclarationNode);
        
        // Assert
        Assertions.assertFalse(isFunctionDeclaration);
    }
    
    @Test
    void testIsHoistedFunctionDeclaration_whenHoistedFunctionDeclarationNode_returnTrue() {
        // Arrange
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(hoistedFunctionDeclarationNode);
        hoistedFunctionDeclarationNode.putBooleanProp(Node.FUNCTION_EXPRESSION, false);
        
        // Act
        boolean isHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode);
        
        // Assert
        Assertions.assertTrue(isHoistedFunctionDeclaration);
    }
    
    @Test
    void testIsHoistedFunctionDeclaration_whenNonHoistedDeclarationNode_returnFalse() {
        // Arrange
        Node nonHoistedDeclarationNode = new Node(Token.FUNCTION);
        nonHoistedDeclarationNode.putBooleanProp(Node.FUNCTION_EXPRESSION, false);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(nonHoistedDeclarationNode);
        
        // Act
        boolean isHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(nonHoistedDeclarationNode);
        
        // Assert
        Assertions.assertFalse(isHoistedFunctionDeclaration);
    }
}