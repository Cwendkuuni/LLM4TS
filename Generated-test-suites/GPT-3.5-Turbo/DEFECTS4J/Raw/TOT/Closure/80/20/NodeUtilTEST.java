import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.BLOCK));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        
        //Act
        boolean result = nodeUtil.isForIn(n);
        
        //Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.IF);
        
        //Act
        boolean result = nodeUtil.isForIn(n);
        
        //Assert
        assertEquals(false, result);
    }

    @Test
    public void testIsStatement_WhenNodeIsUsedAsStatement_ReturnsTrue() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.IF);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        
        //Act
        boolean result = nodeUtil.isStatement(n);
        
        //Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsStatement_WhenNodeIsNotUsedAsStatement_ReturnsFalse() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.IF);
        
        //Act
        boolean result = nodeUtil.isStatement(n);
        
        //Assert
        assertEquals(false, result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        
        //Act
        boolean result = nodeUtil.isFunctionDeclaration(n);
        
        //Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.IF);
        
        //Act
        boolean result = nodeUtil.isFunctionDeclaration(n);
        
        //Assert
        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        
        //Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
        
        //Assert
        assertEquals(true, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        //Arrange
        NodeUtil nodeUtil = new NodeUtil();
        Node n = new Node(Token.IF);
        
        //Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
        
        //Assert
        assertEquals(false, result);
    }
}