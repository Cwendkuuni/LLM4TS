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
    public void testIsForIn_RegularCase() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.FOR_IN));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_NotForInLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_ScriptParent() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_BlockParent() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_LabelParent() {
        Node parent = new Node(Token.LABEL);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_OtherParent() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertFalse(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_FunctionTypeAndStatementParent() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionType() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotStatementParent() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_FunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        scriptParent.addChildToFront(functionParent);
        functionParent.addChildToFront(node);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        Node node = new Node(Token.EMPTY);
        scriptParent.addChildToFront(functionParent);
        functionParent.addChildToFront(node);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ScriptParent() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        scriptParent.addChildToFront(node);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_OtherParent() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}