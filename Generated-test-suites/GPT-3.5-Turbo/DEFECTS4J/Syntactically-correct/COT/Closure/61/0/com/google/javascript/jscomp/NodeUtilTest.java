
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test case 1: For-in loop
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        // Test case 2: Not a for-in loop
        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        // Test case 1: Node is a statement
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.EMPTY);
        node.addChildToFront(child);
        child.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(child));

        // Test case 2: Node is not a statement
        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test case 1: Node is a function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test case 2: Node is not a function declaration
        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test case 1: Node is a hoisted function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test case 2: Node is not a hoisted function declaration
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(node2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}
