
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node());
        n.addChildToFront(new Node());
        n.addChildToFront(new Node());

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsNotForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node());
        n.addChildToFront(new Node());

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.IF);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsNotStatement() {
        Node n = new Node(Token.IF);
        Node parent = new Node(Token.EXPR_RESULT);
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
    public void testIsNotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.FUNCTION);

        scriptNode.addChildToFront(functionNode);
        functionNode.setParent(scriptNode);
        functionNode.getParent().addChildToFront(parentNode);
        parentNode.setParent(functionNode.getParent());

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.EXPR_RESULT);

        scriptNode.addChildToFront(functionNode);
        functionNode.setParent(scriptNode);
        functionNode.getParent().addChildToFront(parentNode);
        parentNode.setParent(functionNode.getParent());

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}

