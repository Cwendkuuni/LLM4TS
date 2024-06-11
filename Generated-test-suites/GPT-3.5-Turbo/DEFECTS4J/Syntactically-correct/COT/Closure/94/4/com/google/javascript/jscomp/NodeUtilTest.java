
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node testNode = new Node(Token.FOR);
        testNode.addChildToBack(new Node(Token.NAME));
        testNode.addChildToBack(new Node(Token.NUMBER));
        testNode.addChildToBack(new Node(Token.STRING));

        assertTrue(NodeUtil.isForIn(testNode));
    }

    @Test
    public void testIsNotForIn() {
        Node testNode = new Node(Token.WHILE);
        testNode.addChildToBack(new Node(Token.NAME));
        testNode.addChildToBack(new Node(Token.NUMBER));

        assertFalse(NodeUtil.isForIn(testNode));
    }

    @Test
    public void testIsStatement() {
        Node testNode = new Node(Token.VAR);
        testNode.addChildToBack(new Node(Token.NAME));
        testNode.addChildToBack(new Node(Token.NUMBER));

        assertTrue(NodeUtil.isStatement(testNode));
    }

    @Test
    public void testIsNotStatement() {
        Node testNode = new Node(Token.NAME);

        assertFalse(NodeUtil.isStatement(testNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node testNode = new Node(Token.FUNCTION);
        testNode.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(testNode));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node testNode = new Node(Token.NAME);

        assertFalse(NodeUtil.isFunctionDeclaration(testNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node testNode = new Node(Token.FUNCTION);
        testNode.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(testNode);
        testNode.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node testNode = new Node(Token.FUNCTION);
        testNode.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.WHILE);
        parent.addChildToBack(testNode);
        testNode.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(testNode));
    }
}
