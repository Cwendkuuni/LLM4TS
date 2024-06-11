import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_notForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.FUNCTION, new Node());
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.FUNCTION, new Node());
        node.setParent(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node());
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node());
        node.setParent(new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION, new Node());
        Node parent = new Node(Token.FUNCTION, function);
        parent.setParent(script);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION, new Node());
        function.setParent(script);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }

}