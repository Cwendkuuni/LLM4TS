import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.VAR);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(functionNode);
        script.addChildToBack(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}