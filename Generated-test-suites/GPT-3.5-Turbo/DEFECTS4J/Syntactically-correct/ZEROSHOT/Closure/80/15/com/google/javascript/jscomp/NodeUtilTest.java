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
        node.addChildToBack(new Node(Token.LABEL));
        node.addChildToBack(new Node(Token.LABEL));
        node.addChildToBack(new Node(Token.LABEL));
        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForInFalse() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.LABEL));
        node.addChildToBack(new Node(Token.LABEL));
        node.addChildToBack(new Node(Token.LABEL));
        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.LOOP);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatementFalse() {
        Node node = new Node(Token.LOOP);
        node.setParent(new Node(Token.CALL));
        assertFalse(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclarationFalse() {
        Node node = new Node(Token.CALL);
        node.setParent(new Node(Token.SCRIPT));
        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclarationFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.CALL);
        parent.addChildToBack(node);
        node.setParent(parent);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}