import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME, "i"));
        n.addChildToBack(new Node(Token.STRING, "in"));
        n.addChildToBack(new Node(Token.NAME, "array"));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_ReturnsFalseForNonForLoop() {
        Node n = new Node(Token.WHILE);

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_ReturnsFalseForNonThreeChildren() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME, "i"));
        n.addChildToBack(new Node(Token.STRING, "in"));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_ReturnsFalseForNonStatementParent() {
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_ReturnsFalseForNonFunction() {
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ReturnsFalseForNonHoistedFunction() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.IF, n));
        n.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}

