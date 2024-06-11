import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EXPR_RESULT));
        node.addChildToBack(new Node(Token.FALSE));
        node.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsForIn_emptyNode() {
        Node node = new Node();

        boolean result = NodeUtil.isForIn(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsStatement_typical() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node();
        node.addChildToBack(child);

        boolean result = NodeUtil.isStatement(child);

        assertEquals(true, result);
    }

    @Test
    public void testIsStatement_noParent() {
        Node node = new Node();

        boolean result = NodeUtil.isStatement(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsFunctionDeclaration_noParent() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(functionNode);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToFront(parent1);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        assertEquals(true, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_noParent() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertEquals(false, result);
    }
}