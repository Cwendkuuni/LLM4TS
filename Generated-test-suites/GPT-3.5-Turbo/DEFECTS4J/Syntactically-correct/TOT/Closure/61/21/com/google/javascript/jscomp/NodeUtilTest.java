import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test typical use case
        Node node1 = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node1));

        // Test edge case with different node type
        Node node2 = new Node(Token.IF, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node2));

        // Test edge case with different child count
        Node node3 = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        // Test typical use case
        Node node1 = new Node(Token.IF, new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node1));

        // Test edge case with parent type SCRIPT
        Node node2 = new Node(Token.NAME);
        node2.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node2));

        // Test edge case with different parent type
        Node node3 = new Node(Token.NAME);
        node3.getParent().setType(Token.ARRAYLIT);
        assertFalse(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test typical use case
        Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        node1.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Test edge case with different node type
        Node node2 = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));

        // Test edge case with parent type SCRIPT
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));

        // Test edge case with parent type SCRIPT and grandparent type FUNCTION
        Node node4 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        node4.getParent().setType(Token.SCRIPT);
        node4.getParent().getParent().setType(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test typical use case
        Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        node1.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Test edge case with different parent type
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        node2.getParent().setType(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

        // Test edge case with parent type SCRIPT and grandparent type FUNCTION
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        node3.getParent().setType(Token.SCRIPT);
        node3.getParent().getParent().setType(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}