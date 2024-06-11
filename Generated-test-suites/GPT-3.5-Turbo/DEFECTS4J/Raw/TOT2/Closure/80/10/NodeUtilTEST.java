import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));

        Node invalidNode = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node n = new Node(Token.NAME);
        scriptNode.addChildToFront(n);
        assertTrue(NodeUtil.isStatement(n));

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToFront(n);
        assertTrue(NodeUtil.isStatement(n));

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(n);
        assertTrue(NodeUtil.isStatement(n));

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(new Node(Token.NAME));
        Node nonStatementNode = new Node(Token.ADD, n);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node nonFunctionNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node functionInFunctionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(functionInFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionInFunctionNode));

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToFront(functionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node nonFunctionNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionNode));
    }
}