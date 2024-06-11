import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(2); // FOR node
        Node node2 = new Node(33); // Not a FOR node
        Node node3 = new Node(122); // FOR node
        node1.addChildToBack(new Node(Token.FOR));
        node1.addChildToBack(new Node());
        node1.addChildToBack(new Node());

        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertTrue(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.EXPR_RESULT);
        Node node3 = new Node(Token.BLOCK);
        node2.addChildToFront(new Node());

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EXPR_RESULT);
        Node node3 = new Node(Token.BLOCK);
        node2.addChildToFront(new Node());

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.SCRIPT);
        Node parent3 = new Node(Token.FUNCTION);
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EXPR_RESULT);
        Node node3 = new Node(Token.BLOCK);
        node2.addChildToFront(new Node());
        parent1.addChildToBack(node1);
        parent2.addChildToBack(node3);
        parent3.addChildToBack(node1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
    }

}