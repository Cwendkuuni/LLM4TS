import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.EMPTY);
        Node node3 = new Node(Token.BLOCK);
        node1.addChildToBack(node2);
        node1.addChildToBack(node3);
        assertTrue(NodeUtil.isForIn(node1));

        // Edge case
        Node node4 = new Node(Token.FOR);
        Node node5 = new Node(Token.EMPTY);
        Node node6 = new Node(Token.EMPTY);
        node4.addChildToBack(node5);
        node4.addChildToBack(node6);
        assertFalse(NodeUtil.isForIn(node4));

        // Error scenario
        Node node7 = null;
        assertFalse(NodeUtil.isForIn(node7));
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        node1.addChildToBack(node2);
        assertTrue(NodeUtil.isStatement(node2));

        // Edge case
        Node node3 = new Node(Token.BLOCK);
        assertFalse(NodeUtil.isStatement(node3));

        // Error scenario
        Node node4 = null;
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.BLOCK);
        node1.addChildToBack(node2);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Edge case
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.BLOCK);
        node3.addChildToBack(node4);
        assertFalse(NodeUtil.isFunctionDeclaration(node3));

        // Error scenario
        Node node5 = null;
        assertFalse(NodeUtil.isFunctionDeclaration(node5));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.SCRIPT);
        node2.addChildToFront(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Edge case
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.BLOCK);
        Node node5 = new Node(Token.FUNCTION);
        node3.addChildToBack(node4);
        node4.addChildToBack(node5);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

        // Error scenario
        Node node6 = null;
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node6));
    }
}