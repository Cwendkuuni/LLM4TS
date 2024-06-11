import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();

        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();

        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node();

        node.addChildToBack(parent);

        assertTrue(nodeUtil.isStatement(parent));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.IF);
        Node parent = new Node();

        node.addChildToFront(parent);

        assertFalse(nodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node();

        node.addChildToBack(parent);

        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.VAR);
        Node parent = new Node();

        node.addChildToFront(parent);

        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node grandparent = new Node();
        Node parent = new Node();

        node.addChildToBack(parent);
        parent.addChildToBack(grandparent);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node();
        Node grandparent = new Node();

        node.addChildToBack(parent);
        grandparent.addChildToBack(parent);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}