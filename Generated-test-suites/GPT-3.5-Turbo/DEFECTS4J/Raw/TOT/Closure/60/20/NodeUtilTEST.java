import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
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
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        Node node = new Node(Token.FOR, new Node(Token.FOR, new Node(Token.FOR), new Node(Token.IN)), new Node(Token.IN));
        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        Node node = new Node(Token.FOR);
        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
        Node node = new Node(Token.VAR);
        assertFalse(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        Node node = new Node(Token.STRING);
        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        Node parentNode = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parentNode.addChildToFront(node);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        Node parentNode = new Node(Token.IF);
        Node node = new Node(Token.FUNCTION);
        parentNode.addChildToFront(node);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}