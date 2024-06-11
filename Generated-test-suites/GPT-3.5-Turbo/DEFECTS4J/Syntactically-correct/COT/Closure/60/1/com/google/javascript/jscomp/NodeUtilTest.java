import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // 


    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.LET));
        n.addChildToFront(new Node(Token.STRING));
        n.addChildToFront(new Node(Token.SCRIPT));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.LET));
        n.addChildToFront(new Node(Token.STRING));

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
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        Node grandparent = new Node(Token.FUNCTION);
        parent.addChildToFront(n);
        n.setParent(parent);
        parent.setParent(grandparent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    // 

}