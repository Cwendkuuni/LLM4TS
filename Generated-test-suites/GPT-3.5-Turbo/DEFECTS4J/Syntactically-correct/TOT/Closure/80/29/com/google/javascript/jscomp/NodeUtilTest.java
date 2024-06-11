import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_missingChildren() {
        Node node = new Node(Token.FOR);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_script() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_block() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_label() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_scopeRoot() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_deeperScope() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.SCRIPT));
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EXPR_RESULT));
        parent.addChildToBack(node);
        node.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}