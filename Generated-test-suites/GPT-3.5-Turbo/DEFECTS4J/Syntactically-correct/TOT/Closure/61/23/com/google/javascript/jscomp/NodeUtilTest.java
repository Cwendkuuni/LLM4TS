import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalUseCase() {
        assertFalse(NodeUtil.isForIn(null));
    
        // Typical use cases
        // Test FOR node with 3 children - should return true
        Node forNode = new Node(Token.FOR);
        forNode.addChildToBack(new Node(Token.VAR));
        forNode.addChildToBack(new Node(Token.VAR));
        forNode.addChildToBack(new Node(Token.VAR));
        assertTrue(NodeUtil.isForIn(forNode));
    
        // Test FOR node with more than 3 children - should return false
        Node forNode2 = new Node(Token.FOR);
        forNode2.addChildToBack(new Node(Token.VAR));
        forNode2.addChildToBack(new Node(Token.VAR));
        forNode2.addChildToBack(new Node(Token.VAR));
        forNode2.addChildToBack(new Node(Token.VAR));
        assertFalse(NodeUtil.isForIn(forNode2));
    
        // Test non-FOR node with 3 children - should return false
        Node ifNode = new Node(Token.IF);
        ifNode.addChildToBack(new Node(Token.VAR));
        ifNode.addChildToBack(new Node(Token.VAR));
        ifNode.addChildToBack(new Node(Token.VAR));
        assertFalse(NodeUtil.isForIn(ifNode));
    }

    @Test
    public void testIsStatement_typicalUseCase() {
        assertFalse(NodeUtil.isStatement(null));
    
        // Typical use cases
        // Test SCRIPT parent - should return true
        Node scriptNode = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR);
        scriptNode.addChildToBack(varNode);
        assertTrue(NodeUtil.isStatement(varNode));
    
        // Test BLOCK parent - should return true
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(Token.VAR));
        assertTrue(NodeUtil.isStatement(blockNode.getFirstChild()));
    
      // Test LABEL parent - should return true 
      Node labelNode = new Node(Token.LABEL);
      labelNode.addChildToBack(new Node(Token.VAR));
      assertTrue(NodeUtil.isStatement(labelNode.getFirstChild()));
    
        // Test non-SCRIPT, non-BLOCK, non-LABEL parent - should return false
        Node ifNode = new Node(Token.IF);
        ifNode.addChildToBack(new Node(Token.VAR));
        assertFalse(NodeUtil.isStatement(ifNode.getFirstChild()));
    }

    @Test
    public void testIsFunctionDeclaration_typicalUseCase() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
    
        // Typical use cases
        // Test FUNCTION node with SCRIPT parent - should return true
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    
        // Test FUNCTION node with BLOCK parent - should return true
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isFunctionDeclaration(blockNode.getFirstChild()));
    
        // Test non-FUNCTION node with SCRIPT parent - should return false
        Node varNode = new Node(Token.VAR);
        Node scriptNode2 = new Node(Token.SCRIPT);
        scriptNode2.addChildToBack(varNode);
        assertFalse(NodeUtil.isFunctionDeclaration(varNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalUseCase() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    
        // Typical use cases
        // Test FUNCTION node with SCRIPT parent - should return false
        Node functionNode1 = new Node(Token.FUNCTION);
        Node scriptNode1 = new Node(Token.SCRIPT);
        scriptNode1.addChildToBack(functionNode1);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
    
        // Test FUNCTION node with FUNCTION parent - should return true
        Node functionNode2 = new Node(Token.FUNCTION);
        Node functionNode3 = new Node(Token.FUNCTION);
        functionNode3.addChildToBack(functionNode2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    
        // Test FUNCTION node with non-SCRIPT, non-FUNCTION parent - should return false 
        Node varNode = new Node(Token.VAR);
        Node scriptNode2 = new Node(Token.SCRIPT);
        scriptNode2.addChildToBack(varNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(varNode));
    }

}