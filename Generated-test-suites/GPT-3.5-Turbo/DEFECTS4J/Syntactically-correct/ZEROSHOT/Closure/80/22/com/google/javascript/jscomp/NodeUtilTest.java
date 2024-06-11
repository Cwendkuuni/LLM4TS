import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.EMPTY);
        node.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));

        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));

        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        Node parentFunction = new Node(Token.FUNCTION);
        parentFunction.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(new Node(Token.EMPTY));
        script.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}