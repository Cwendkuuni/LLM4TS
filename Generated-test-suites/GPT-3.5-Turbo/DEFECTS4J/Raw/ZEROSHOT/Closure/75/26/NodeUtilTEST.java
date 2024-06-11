import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EXPR_RESULT);
        Node child2 = new Node(Token.VAR);
        Node child3 = new Node(Token.STRING);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EXPR_RESULT);
        Node child2 = new Node(Token.VAR);
        Node child3 = new Node(Token.STRING);
        Node child4 = new Node(Token.STRING);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        node.addChildToBack(child4);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_Script() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.EXPR_RESULT);
        node.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsStatement_Block() {
        Node node = new Node(Token.BLOCK);
        Node child = new Node(Token.EXPR_RESULT);
        node.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsStatement_Label() {
        Node node = new Node(Token.LABEL);
        Node child = new Node(Token.EXPR_RESULT);
        node.addChildToBack(child);

        assertTrue(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node node = new Node(Token.FUNCTION);
        Node child = new Node(Token.EXPR_RESULT);
        node.addChildToBack(child);

        assertFalse(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsFunctionDeclaration_FunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_HoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(functionNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

}