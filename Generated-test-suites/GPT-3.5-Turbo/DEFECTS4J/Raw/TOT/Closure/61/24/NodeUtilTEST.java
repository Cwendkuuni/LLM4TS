import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn_WithForInLoop_ReturnsTrue() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR_IN));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        boolean result = NodeUtil.isForIn(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsForIn_WithForLoop_ReturnsFalse() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        boolean result = NodeUtil.isForIn(node);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsStatement_WhenParentIsScript_ReturnsTrue() {
        Node node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.SCRIPT));
        
        boolean result = NodeUtil.isStatement(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenParentIsBlock_ReturnsTrue() {
        Node node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.BLOCK));
        
        boolean result = NodeUtil.isStatement(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenParentIsLabel_ReturnsTrue() {
        Node node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.LABEL));
        
        boolean result = NodeUtil.isStatement(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenParentHasOtherType_ReturnsFalse() {
        Node node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.SCRIPT));
        node.getParent().setType(Token.FUNCTION);
        
        boolean result = NodeUtil.isStatement(node);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenTypeIsFunctionAndIsStatement_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenTypeIsFunctionAndIsNotStatement_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.getParent().setType(Token.NAME);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenIsFunctionDeclarationAndParentIsScript_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.getParent().setType(Token.SCRIPT);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenIsFunctionDeclarationAndParentIsFunction_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.getParent().setType(Token.FUNCTION);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenIsFunctionDeclarationAndParentHasOtherType_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.getParent().setType(Token.NAME);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        Assert.assertFalse(result);
    }
}