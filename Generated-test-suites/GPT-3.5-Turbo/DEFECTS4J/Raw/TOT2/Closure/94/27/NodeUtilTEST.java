import com.google.javascript.rhino.Node;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandparent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT, grandparent);
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}