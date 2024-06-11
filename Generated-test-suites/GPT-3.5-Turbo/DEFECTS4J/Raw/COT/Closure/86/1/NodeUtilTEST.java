
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE), new Node(Token.EMPTY));
        assertEquals(true, NodeUtil.isForIn(n));

        n = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE));
        assertEquals(false, NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SWITCH, new Node(Token.BLOCK));
        assertEquals(true, NodeUtil.isStatement(n));

        n = new Node(Token.EXPR_RESULT, new Node(Token.VAR));
        assertEquals(true, NodeUtil.isStatement(n));

        n = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
        assertEquals(false, NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
        assertEquals(true, NodeUtil.isFunctionDeclaration(n));

        n = new Node(Token.EXPR_RESULT, new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction")));
        assertEquals(false, NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
        n.getParent().setType(Token.SCRIPT);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(n));

        n.getParent().setType(Token.BLOCK);
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n));
    }
}
