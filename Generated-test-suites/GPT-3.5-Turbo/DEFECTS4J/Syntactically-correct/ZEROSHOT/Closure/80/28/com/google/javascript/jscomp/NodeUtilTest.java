import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToFront(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);
        functionNode.setParent(scriptNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(functionNode);
        functionNode.setParent(blockNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}