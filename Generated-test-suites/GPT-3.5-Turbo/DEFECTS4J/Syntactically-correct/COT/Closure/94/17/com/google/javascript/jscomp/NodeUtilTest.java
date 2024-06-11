import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_notForIn() {
        Node node = new Node(Token.WHILE);
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.ADD);
        
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notFunctionDeclaration() {
        Node node = new Node(Token.ADD);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}