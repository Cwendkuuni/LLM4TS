import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.EMPTY);
        node.addChildToFront(childNode);
        childNode.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isStatement(childNode);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(parent1);
        node.setParent(parent1);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }
}