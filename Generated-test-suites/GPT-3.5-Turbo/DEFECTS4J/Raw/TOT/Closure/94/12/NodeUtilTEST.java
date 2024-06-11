import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test case for isForIn method
    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node1));

        // Edge case: Node type is not FOR
        Node node2 = new Node(Token.SCRIPT);
        assertFalse(NodeUtil.isForIn(node2));

        // Edge case: Node does not have 3 children
        Node node3 = new Node(Token.FOR);
        node3.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node3));
    }

    // Test case for isStatement method
    @Test
    public void testIsStatement() {
        // Typical use case
        Node node1 = new Node(Token.LABEL);
        Node parent1 = new Node(Token.BLOCK);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isStatement(node1));

        // Edge case: Node type is not SCRIPT, BLOCK, or LABEL
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        node2.setParent(parent2);
        assertFalse(NodeUtil.isStatement(node2));

        // Edge case: Parent node is null
        Node node3 = new Node(Token.LABEL);
        assertFalse(NodeUtil.isStatement(node3));
    }

    // Test case for isFunctionDeclaration method
    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Edge case: Node type is not FUNCTION
        Node node2 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.LABEL);
        node2.setParent(parent2);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));

        // Edge case: Node is not a statement
        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.BLOCK);
        node3.setParent(parent3);
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    // Test case for isHoistedFunctionDeclaration method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Edge case: Node type is not FUNCTION
        Node node2 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        node2.setParent(parent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

        // Edge case: Node is not a statement
        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.BLOCK);
        Node grandparent3 = new Node(Token.FUNCTION);
        node3.setParent(parent3);
        parent3.setParent(grandparent3);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}