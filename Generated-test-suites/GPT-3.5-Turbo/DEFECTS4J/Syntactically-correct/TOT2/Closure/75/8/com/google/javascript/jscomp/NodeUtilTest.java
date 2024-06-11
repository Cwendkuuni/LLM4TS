import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(Token.FOR_IN, new Node(Token.FOR, new Node(Token.FOR_IN))));
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IF, new Node(Token.FOR_IN));
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(new Node(Token.VAR, new Node(Token.VAR)));
        assertTrue(NodeUtil.isStatement(scriptNode));

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.ASSIGN, new Node(Token.ASSIGN)));
        assertFalse(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDecl = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDecl));

        Node notFunctionDecl = new Node(Token.SCRIPT);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDecl));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunction = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        parentScript.addChildToFront(hoistedFunction);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunction));

        Node nonHoistedFunction = new Node(Token.FUNCTION);
        Node parentFunction = new Node(Token.FUNCTION);
        parentFunction.addChildToFront(nonHoistedFunction);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunction));
    }
}