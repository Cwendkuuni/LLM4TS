import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node labelNode = new Node(Token.LABEL);
        Node functionNode = new Node(Token.FUNCTION);

        scriptNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(scriptNode));

        labelNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(labelNode));

        functionNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}