import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR, new Node(Token.FOR).addChildren(new Node(Token.IF), new Node(Token.BLOCK)), new Node("test"));

        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node(Token.LABEL));
        Node node2 = new Node(Token.IF);
        Node node3 = new Node(Token.SCRIPT, new Node(Token.LABEL), new Node(Token.FUNCTION));

        assertTrue(NodeUtil.isStatement(node1));
        assertFalse(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION, new Node(Token.SCRIPT), new Node(Token.CALL));

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.SCRIPT), new Node(Token.CALL));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.SCRIPT));
        Node node3 = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
        Node node4 = new Node(Token.SCRIPT);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}