
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.BLOCK);
        node.addChildToFront(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.NAME);
        node.addChildToFront(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.NAME);
        node.addChildToFront(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.NAME);
        node.addChildToFront(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}
