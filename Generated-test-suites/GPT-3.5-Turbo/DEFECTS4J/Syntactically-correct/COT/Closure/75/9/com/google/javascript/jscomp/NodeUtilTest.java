
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        NodeUtil nodeUtil = new NodeUtil();
        
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        assertTrue(nodeUtil.isForIn(node1));
        
        Node node2 = new Node(Token.WHILE);
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        assertFalse(nodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        NodeUtil nodeUtil = new NodeUtil();
        
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(nodeUtil.isStatement(node1));
        
        Node node2 = new Node(Token.EXPR_RESULT);
        assertTrue(nodeUtil.isStatement(node2));
        
        Node node3 = new Node(Token.FUNCTION);
        assertFalse(nodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        NodeUtil nodeUtil = new NodeUtil();
        
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node1);
        assertTrue(nodeUtil.isFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.EXPR_RESULT);
        parent2.addChildToBack(node2);
        assertFalse(nodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        NodeUtil nodeUtil = new NodeUtil();
        
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node1);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.BLOCK);
        parent2.addChildToBack(node2);
        Node grandparent2 = new Node(Token.FUNCTION);
        grandparent2.addChildToBack(parent2);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node2));
    }
}
