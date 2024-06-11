import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalInput() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_notForInLoop() {
        Node node = new Node(Token.IF);
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_edgeCase() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typicalInput() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FOR);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notUsedAsStatement() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FOR);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_edgeCase() {
        Node node = new Node(Token.FOR);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typicalInput() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_edgeCase() {

        assertFalse(NodeUtil.isFunctionDeclaration(null));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalInput() {
        Node grandParent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(grandParent);
        Node node = new Node(Token.FUNCTION);
        grandParent.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_edgeCase() {
        Node node = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}