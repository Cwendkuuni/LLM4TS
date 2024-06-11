
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when n is a FOR-IN loop
        Node n1 = new Node(Token.FOR);
        n1.addChildToFront(new Node(Token.EMPTY));
        n1.addChildToFront(new Node(Token.EMPTY));
        n1.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n1));

        // Test when n is not a FOR-IN loop
        Node n2 = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        // Test when n is part of a statement
        Node n1 = new Node(Token.NOT);
        Node parent1 = new Node(Token.BLOCK);
        parent1.addChildToFront(n1);
        assertTrue(NodeUtil.isStatement(n1));

        // Test when n is not part of a statement
        Node n2 = new Node(Token.NOT);
        assertFalse(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when n is a function declaration
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(n1);
        assertTrue(NodeUtil.isFunctionDeclaration(n1));

        // Test when n is not a function declaration
        Node n2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when n is a hoisted function declaration
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(n1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

        // Test when n is not a hoisted function declaration
        Node n2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
    }
}
