import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        script.addChildToBack(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(function);

        assertTrue(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(new Node(Token.EMPTY));
        script.addChildToBack(function);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}