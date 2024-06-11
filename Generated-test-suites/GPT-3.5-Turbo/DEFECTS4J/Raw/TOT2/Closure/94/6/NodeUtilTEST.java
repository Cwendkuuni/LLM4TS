import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY))));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isStatement(n));
        n = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        n = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        n.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        n.getParent().getParent().setType(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        n.getParent().setType(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}