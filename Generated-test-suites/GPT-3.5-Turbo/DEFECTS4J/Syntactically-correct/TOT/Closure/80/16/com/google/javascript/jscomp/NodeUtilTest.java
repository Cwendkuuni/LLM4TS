import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsForIn_missingChild() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsForIn_wrongTokenType() {
        Node node = new Node(Token.IF);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsStatement_scriptParent() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        
        boolean result = NodeUtil.isStatement(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_blockParent() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        
        boolean result = NodeUtil.isStatement(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_labelParent() {
        Node parent = new Node(Token.LABEL);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        
        boolean result = NodeUtil.isStatement(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_wrongParent() {
        Node parent = new Node(Token.IF);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        
        boolean result = NodeUtil.isStatement(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_functionStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_functionExpression() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_wrongNodeType() {
        Node parent = new Node(Token.IF);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_inScript() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FCNTION);
        functionParent.addChildToBack(new Node(Token.EMPTY));
        Node node = new Node(Token.FUNCTION);
        node.setParent(functionParent);
        functionParent.setParent(scriptParent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_inFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FCNTION);
        functionParent.addChildToBack(new Node(Token.EMPTY));
        Node node = new Node(Token.FUNCTION);
        node.setParent(functionParent);
        functionParent.setParent(scriptParent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_noParent() {
        Node node = new Node(Token.FUNCTION);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertFalse(result);
    }
}