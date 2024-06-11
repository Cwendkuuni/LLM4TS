import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test cases for isForIn method

    @Test
    public void testIsForIn_shouldReturnTrueForForInLoop() {
        Node node = createNode(Token.FOR, createNode(3));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_shouldReturnFalseForNonForInLoop() {
        Node node = createNode(Token.FOR, createNode(2));
        assertFalse(NodeUtil.isForIn(node));
    }

    // Test cases for isStatement method

    @Test
    public void testIsStatement_shouldReturnTrueForScriptNode() {
        Node node = createNode(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_shouldReturnTrueForBlockNode() {
        Node node = createNode(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_shouldReturnTrueForLabelNode() {
        Node node = createNode(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_shouldReturnFalseForOtherParentNodes() {
        Node node = createNode(Token.IF);
        assertFalse(NodeUtil.isStatement(node));
    }

    // Test cases for isFunctionDeclaration method

    @Test
    public void testIsFunctionDeclaration_shouldReturnTrueForFunctionDeclaration() {
        Node node = createNode(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnFalseForFunctionExpression() {
        Node node = createNode(Token.FUNCTION);
        node.getParent().setType(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnFalseForNonFunctionNodes() {
        Node node = createNode(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    // Test cases for isHoistedFunctionDeclaration method

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnTrueForHoistedFunctionDeclaration() {
        Node parent = createNode(Token.SCRIPT);
        Node node = createNode(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnTrueForHoistedFunctionDeclarationInFunction() {
        Node grandParent = createNode(Token.FUNCTION);
        Node parent = createNode(Token.SCRIPT, grandParent);
        Node node = createNode(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnFalseForNormalFunctionDeclaration() {
        Node parent = createNode(Token.SCRIPT);
        Node node = createNode(Token.FUNCTION, parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnFalseForNonFunctionDeclaration() {
        Node node = createNode(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    // Helper method to create Node objects
    private Node createNode(int type) {
        return new Node(type);
    }

    private Node createNode(int type, Node parent) {
        Node node = new Node(type);
        node.setParent(parent);
        return node;
    }

    private Node createNode(int type, Node parent, Node grandParent) {
        Node node = new Node(type);
        node.setParent(parent);
        parent.setParent(grandParent);
        return node;
    }
}