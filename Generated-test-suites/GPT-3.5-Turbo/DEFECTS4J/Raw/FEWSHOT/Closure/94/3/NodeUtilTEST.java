import static org.junit.Assert.*;
import org.junit.Test;
public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(n1));

        Node n2 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(n2));

        Node n3 = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(n3));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT, new Node());
        assertTrue(NodeUtil.isStatement(n1));

        Node n2 = new Node(Token.BLOCK, new Node());
        assertTrue(NodeUtil.isStatement(n2));

        Node n3 = new Node(Token.LABEL, new Node());
        assertTrue(NodeUtil.isStatement(n3));

        Node n4 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(n1));

        Node n2 = new Node(Token.VAR, new Node());
        assertFalse(NodeUtil.isFunctionDeclaration(n2));

        Node n3 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(n3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        script.addChildToFront(function);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));

        Node block = new Node(Token.BLOCK);
        block.addChildToFront(function);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));

        Node parentFunction = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        parentFunction.addChildToFront(function);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}