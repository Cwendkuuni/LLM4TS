
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToFront(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_NotForInLoop() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToFront(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node parent = new Node(Token.EMPTY);
        Node n = new Node(Token.EMPTY);
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToFront(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EMPTY);
        Node n = new Node(Token.FUNCTION);
        grandParent.addChildToFront(parent);
        parent.addChildToFront(n);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}
