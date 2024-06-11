import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME, new Node("i")));
        node.addChildToFront(new Node(Token.STRING, new Node("index")));
        node.addChildToFront(new Node(Token.NAME, new Node("arr")));
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_emptyForInLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME, new Node("i")));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.NAME, new Node("arr")));
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typical() {
        Node node = new Node(Token.NAME, new Node("test"));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.NAME, new Node("test"));
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK, new Node(Token.SCRIPT));
        parent.addChildToFront(node);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}