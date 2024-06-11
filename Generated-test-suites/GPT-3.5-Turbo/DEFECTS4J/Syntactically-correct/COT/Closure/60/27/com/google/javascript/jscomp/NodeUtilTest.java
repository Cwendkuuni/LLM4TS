
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test isForIn method

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_ReturnsFalseForNonForInNode() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(n));
    }

    // Test isStatement method

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_ReturnsFalseForNonStatementNode() {
        Node n = new Node(Token.STRING);
        assertFalse(NodeUtil.isStatement(n));
    }

    // Test isFunctionDeclaration method

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_ReturnsFalseForNonFunctionDeclarationNode() {
        Node n = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    // Test isHoistedFunctionDeclaration method

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ReturnsFalseForNonHoistedFunctionDeclarationNode() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ReturnsFalseForNonFunctionDeclarationNode() {
        Node n = new Node(Token.VAR);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}

