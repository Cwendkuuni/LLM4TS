import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR).addChildToBack(new Node(Token.NAME));
        Node node3 = new Node(Token.WHILE);

        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        // Error scenario
        Node node1 = new Node(Token.SCRIPT);
        node1.addChildToFront(new Node(Token.FUNCTION));

        // Typical use case
        Node node2 = new Node(Token.BLOCK);
        node2.addChildToBack(new Node(Token.NAME));
        Node node3 = new Node(Token.LABEL);
        node3.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.IF);

        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Error scenario
        Node node1 = new Node(Token.FUNCTION).addChildToBack(new Node(Token.SCRIPT));

        // Typical use case
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.SCRIPT).addChildToBack(new Node(Token.FUNCTION));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
    }

}