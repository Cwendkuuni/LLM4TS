
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test case: Valid FOR-IN loop
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.IN);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToFront(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(node));

        // Test case: Invalid FOR-IN loop (missing child node)
        Node invalidNode = new Node(Token.FOR);
        Node invalidChild1 = new Node(Token.NAME);
        Node invalidChild2 = new Node(Token.IN);
        invalidNode.addChildToFront(invalidChild1);
        invalidNode.addChildToBack(invalidChild2);

        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        // Test case: Node is used as a statement
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.EXPR_RESULT);
        node.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));

        // Test case: Node is not used as a statement
        Node invalidNode = new Node(Token.SCRIPT);
        Node invalidChild = new Node(Token.NAME);
        invalidNode.addChildToBack(invalidChild);

        assertFalse(NodeUtil.isStatement(invalidChild));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test case: Node is a function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        node.attachParent(parent1);
        node.attachParent(parent2);

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test case: Node is not a function declaration
        Node invalidNode = new Node(Token.FUNCTION);
        Node invalidParent = new Node(Token.NAME);
        invalidNode.attachParent(invalidParent);

        assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test case: Node is a hoisted function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        node.attachParent(parent1);
        node.attachParent(parent2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test case: Node is not a hoisted function declaration
        Node invalidNode = new Node(Token.FUNCTION);
        Node invalidParent = new Node(Token.NAME);
        invalidNode.attachParent(invalidParent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
    }
}
