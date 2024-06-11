import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        boolean result = NodeUtil.isForIn(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsForIn_False() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isForIn(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsStatement_False() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsFunctionDeclaration_False() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node);
        node.setParent(parent1);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(parent1);
        parent1.setParent(parent2);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node node = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertEquals(false, result);
    }
}