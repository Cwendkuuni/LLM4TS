import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.VAR));
        node.addChildToBack(new Node(Token.IN));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);
        parent.setParent(grandParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}