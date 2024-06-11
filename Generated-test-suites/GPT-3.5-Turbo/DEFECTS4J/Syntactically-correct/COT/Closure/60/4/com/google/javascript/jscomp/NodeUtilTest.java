
// JUnit test cases for NodeUtil class
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_notForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.EMPTY));
        functionNode.addChildToBack(new Node(Token.EMPTY));
        scriptNode.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}