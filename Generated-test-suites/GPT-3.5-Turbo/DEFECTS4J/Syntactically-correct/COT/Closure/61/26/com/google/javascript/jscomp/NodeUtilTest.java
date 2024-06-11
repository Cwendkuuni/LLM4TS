
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.ASSIGN));
        node1.addChildToBack(new Node(Token.OBJECT));
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.ASSIGN));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.EXPR_RESULT);
        node1.addChildToBack(child1);
        assertTrue(NodeUtil.isStatement(child1));

        Node node2 = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node1);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        parent2.addChildToBack(node2);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        parent2.addChildToBack(node2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.FUNCTION);
        parent3.addChildToBack(node3);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}
