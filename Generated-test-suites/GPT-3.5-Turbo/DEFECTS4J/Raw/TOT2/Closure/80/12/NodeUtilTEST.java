import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent);

        assertTrue(NodeUtil.isStatement(node));
        
        Node parent2 = new Node(Token.EXPR_RESULT);
        Node node2 = new Node(Token.FUNCTION, parent2);

        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.FOR);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION, scriptParent);
        Node node = new Node(Token.FUNCTION, functionParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node blockParent = new Node(Token.BLOCK);
        Node node2 = new Node(Token.FUNCTION, blockParent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}