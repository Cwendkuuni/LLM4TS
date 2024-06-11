import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());

        boolean result = NodeUtil.isForIn(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsForIn_false() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());

        boolean result = NodeUtil.isForIn(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsStatement_false() {
        Node node = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsFunctionDeclaration_false() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.BLOCK));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.FUNCTION));
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(new Node(Token.FUNCTION));
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertEquals(false, result);
    }
}