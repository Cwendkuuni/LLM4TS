import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node n = new Node(Token.VAR, new Node(Token.NAME));
        n.setParent(script);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION, new Node(Token.NAME));
        function.setParent(script);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

}