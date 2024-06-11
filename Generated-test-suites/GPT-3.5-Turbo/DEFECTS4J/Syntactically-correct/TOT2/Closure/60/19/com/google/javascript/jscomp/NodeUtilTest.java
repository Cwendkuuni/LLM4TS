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
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        functionParent.addChildToBack(n);
        scriptParent.addChildToBack(functionParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}