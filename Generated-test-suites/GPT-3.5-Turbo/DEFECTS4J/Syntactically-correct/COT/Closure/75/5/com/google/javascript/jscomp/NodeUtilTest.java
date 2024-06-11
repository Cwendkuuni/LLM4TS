
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EXPR_RESULT));
        node.addChildToBack(new Node(Token.TRUE));
        node.addChildToBack(new Node(Token.BLOCK));
        
        boolean result = NodeUtil.isForIn(node);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.IF);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        functionNode.setParent(scriptNode);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(scriptNode);
        scriptNode.setParent(parent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);
        
        assertEquals(true, result);
    }
}
