import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.WHILE, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT, new Node());
        Node blockNode = new Node(Token.BLOCK, new Node());
        Node labelNode = new Node(Token.LABEL, new Node());

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));

        Node functionNode = new Node(Token.FUNCTION, new Node());
        functionNode.addChildToBack(scriptNode);
        assertFalse(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node notFunctionNode = new Node(Token.SCRIPT, new Node());
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionNode = new Node(Token.FUNCTION, new Node());
        Node scriptNode = new Node(Token.SCRIPT, hoistedFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

        Node parentFunctionNode = new Node(Token.FUNCTION, hoistedFunctionNode);
        Node grandparentFunctionNode = new Node(Token.FUNCTION, parentFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    }
}