import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR, null);
        Node node2 = new Node(Token.VAR, null);
        Node node3 = new Node(Token.IN, null);
        node1.addChildToBack(node2);
        node1.addChildToBack(node3);
        assertTrue(nodeUtil.isForIn(node1));

        // Edge case: Empty node
        Node node4 = new Node(Token.FOR, null);
        assertFalse(nodeUtil.isForIn(node4));

        // Error scenario: Invalid child count
        Node node5 = new Node(Token.FOR, null);
        Node node6 = new Node(Token.VAR, null);
        node5.addChildToBack(node6);
        assertFalse(nodeUtil.isForIn(node5));
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Node node1 = new Node(Token.VAR, null);
        Node node2 = new Node(Token.SCRIPT, null);
        node2.addChildToBack(node1);
        assertTrue(nodeUtil.isStatement(node1));

        // Edge case: Parent is null
        Node node3 = new Node(Token.VAR, null);
        assertFalse(nodeUtil.isStatement(node3));

        // Error scenario: Invalid parent type
        Node node4 = new Node(Token.VAR, null);
        Node node5 = new Node(Token.NOT, null);
        node4.addChildToBack(node5);
        assertFalse(nodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION, null);
        Node node2 = new Node(Token.SCRIPT, null);
        node2.addChildToBack(node1);
        assertTrue(nodeUtil.isFunctionDeclaration(node1));

        // Edge case: Parent is null
        Node node3 = new Node(Token.FUNCTION, null);
        assertFalse(nodeUtil.isFunctionDeclaration(node3));

        // Error scenario: Not a statement
        Node node4 = new Node(Token.FUNCTION, null);
        Node node5 = new Node(Token.NOT, null);
        node4.addChildToBack(node5);
        assertFalse(nodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION, null);
        Node node2 = new Node(Token.SCRIPT, null);
        node2.addChildToBack(node1);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node1));

        // Edge case: Parent is not SCRIPT or FUNCTION
        Node node3 = new Node(Token.FUNCTION, null);
        Node node4 = new Node(Token.NOT, null);
        node4.addChildToBack(node3);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node3));

        // Error scenario: Not a function declaration
        Node node5 = new Node(Token.VAR, null);
        Node node6 = new Node(Token.SCRIPT, null);
        node6.addChildToBack(node5);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node5));
    }

}