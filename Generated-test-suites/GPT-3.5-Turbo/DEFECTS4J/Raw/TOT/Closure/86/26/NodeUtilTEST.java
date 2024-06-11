import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NUMBER);
        Node child2 = new Node(Token.VAR);
        Node child3 = new Node(Token.BOOLEAN);
        node.addChildToFront(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_missingTypeToken() {
        Node node = new Node(); // No type token set

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_missingChildNodes() {
        Node node = new Node(Token.FOR); // Only one child node instead of three
        Node child = new Node(Token.NUMBER);
        node.addChildToBack(child);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typicalCase() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node n = new Node(Token.IF);
        scriptNode.addChildToBack(blockNode);
        blockNode.addChildToBack(labelNode);
        labelNode.addChildToBack(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_nonStatementParent() {
        Node parent = new Node(Token.TRY);
        Node n = new Node(Token.EXPR_RESULT);
        n.setParent(parent);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_expression() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.FUNCTION);
        n.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_withinFunction() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode1 = new Node(Token.FUNCTION);
        Node functionNode2 = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode1);
        functionNode1.addChildToBack(functionNode2);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notInScopeRoot() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node blockNode = new Node(Token.BLOCK);
        scriptNode.addChildToBack(blockNode);
        blockNode.addChildToBack(functionNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}