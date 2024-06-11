
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_False() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node());

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_False() {
        Node node = new Node(Token.EXPR_RESULT);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_False() {
        Node node = new Node(Token.EXPR_RESULT);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node);
        parent1.addChildToBack(parent2);
        node.setParent(parent2);
        parent2.setParent(parent1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
