import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);

        scriptNode.addChildToBack(new Node(Token.EMPTY));
        blockNode.addChildToBack(new Node(Token.EMPTY));
        labelNode.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node expressionNode = new Node(Token.LET);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(expressionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionParentNode = new Node(Token.FUNCTION);

        scriptNode.addChildToBack(functionNode);
        functionParentNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionParentNode));
    }
}