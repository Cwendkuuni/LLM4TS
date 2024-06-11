import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(), new Node(), new Node());
        Node node2 = new Node(Token.IF, new Node(), new Node(), new Node());
        assertEquals(true, NodeUtil.isForIn(node1));
        assertEquals(false, NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node parent2 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        Node node1 = new Node(Token.IF, parent1, new Node(), new Node());
        Node node2 = new Node(Token.IF, parent2, new Node(), new Node());
        assertEquals(true, NodeUtil.isStatement(node1));
        assertEquals(false, NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        Node node2 = new Node(Token.IF, new Node(), new Node(), new Node());
        assertEquals(true, NodeUtil.isFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node parent2 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        Node node1 = new Node(Token.FUNCTION, parent1, new Node(), new Node());
        Node node2 = new Node(Token.FUNCTION, parent2, new Node(), new Node());
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}