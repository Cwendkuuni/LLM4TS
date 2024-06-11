import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.MUL);
        Node child2 = new Node(Token.PLUS);
        Node child3 = new Node(Token.MINUS);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.FUNCTION);
        Node child2 = new Node(Token.VAR);
        Node child3 = new Node(Token.LET);
        node.addChildToFront(child1);
        node.addChildToFront(child2);
        node.addChildToFront(child3);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToBack(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }
}