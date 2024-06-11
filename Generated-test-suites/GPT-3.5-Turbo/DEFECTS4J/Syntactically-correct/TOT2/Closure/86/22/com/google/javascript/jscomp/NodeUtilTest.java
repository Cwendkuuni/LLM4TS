import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        Node invalidNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(Token.FUNCTION));

        assertTrue(NodeUtil.isStatement(blockNode.getFirstChild()));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        parentBlockNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(parentBlockNode.getFirstChild()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node grandparentFunctionNode = new Node(Token.FUNCTION);
        parentScriptNode.addChildToBack(functionNode);
        grandparentFunctionNode.addChildToBack(parentScriptNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(grandparentFunctionNode.getFirstChild()));
    }
}