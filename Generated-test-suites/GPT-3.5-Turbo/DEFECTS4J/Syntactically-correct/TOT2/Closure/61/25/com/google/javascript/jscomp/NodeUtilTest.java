import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        Node parent2 = new Node(Token.IF);
        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(parent2);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION, new Node(Token.FUNCTION));
        grandParent.addChildToBack(parent);

        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.ADD);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}