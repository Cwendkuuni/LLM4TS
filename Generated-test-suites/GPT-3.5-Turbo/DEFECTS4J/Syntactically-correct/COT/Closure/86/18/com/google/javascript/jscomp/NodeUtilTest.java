import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EXPR_RESULT));
        node.addChildToBack(new Node(Token.STRING));
        node.addChildToBack(new Node(Token.BLOCK));
        
        boolean result = NodeUtil.isForIn(node);
        
        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.EXPR_RESULT));
        
        boolean result = NodeUtil.isStatement(node);
        
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.BLOCK));
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(new Node(Token.BLOCK));
        
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(parent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertTrue(result);
    }
}

