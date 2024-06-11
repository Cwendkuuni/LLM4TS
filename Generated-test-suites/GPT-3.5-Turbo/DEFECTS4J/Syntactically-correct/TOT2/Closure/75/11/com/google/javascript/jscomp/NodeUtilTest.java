import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));

        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));

        parent.setParent(grandParent);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}