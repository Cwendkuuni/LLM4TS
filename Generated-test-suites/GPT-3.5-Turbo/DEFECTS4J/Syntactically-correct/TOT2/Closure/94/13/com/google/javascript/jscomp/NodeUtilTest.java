import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.INIT));
        node.addChildToBack(new Node(Token.COND));
        node.addChildToBack(new Node(Token.BLOCK));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node node = new Node(Token.VAR);
        script.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(node));
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
        script.addChildToBack(function);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}