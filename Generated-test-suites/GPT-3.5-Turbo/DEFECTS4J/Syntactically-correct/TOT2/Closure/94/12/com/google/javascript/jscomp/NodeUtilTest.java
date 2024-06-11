import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.FOR_INIT), new Node(Token.FOR_COND), new Node(Token.FOR_ITER), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT, new Node(Token.VAR), new Node(Token.FOR), new Node(Token.IF));
        Node node = new Node(Token.FOR, new Node(Token.FOR_INIT), new Node(Token.FOR_COND), new Node(Token.FOR_ITER), new Node(Token.EMPTY));
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK)));
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}