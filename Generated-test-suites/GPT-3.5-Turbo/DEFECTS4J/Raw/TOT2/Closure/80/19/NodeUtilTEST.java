import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInScript() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInFunction() {
        Node functionNode1 = new Node(Token.FUNCTION);
        Node functionNode2 = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode1);
        parentFunctionNode.addChildToBack(functionNode2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
    }
}