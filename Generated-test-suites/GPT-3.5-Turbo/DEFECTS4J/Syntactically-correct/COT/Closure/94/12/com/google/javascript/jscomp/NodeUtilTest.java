
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when node is a FOR-IN loop
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        // Test when node is not a FOR-IN loop
        node = new Node(Token.IF);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));

        // Test when node is not used as a statement
        node = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test when node is not a function declaration
        node = new Node(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.EMPTY));
        scriptNode.addChildToBack(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        // Test when node is not a hoisted function declaration
        scriptNode = new Node(Token.SCRIPT);
        Node ifNode = new Node(Token.IF);
        scriptNode.addChildToBack(ifNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(ifNode));
    }
}
