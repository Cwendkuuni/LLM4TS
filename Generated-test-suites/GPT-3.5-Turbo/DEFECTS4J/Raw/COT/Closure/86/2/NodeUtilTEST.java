
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Extracted public methods
    // isForIn(Node n)
    // isStatement(Node n)
    // isFunctionDeclaration(Node n)
    // isHoistedFunctionDeclaration(Node n)

    // Test for isForIn(Node n)
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    // Test for isStatement(Node n)
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isStatement(n));
    }

    // Test for isFunctionDeclaration(Node n)
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    // Test for isHoistedFunctionDeclaration(Node n)
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    // Edge case: Testing negative scenario for isForIn(Node n)
    @Test
    public void testIsForIn_Negative() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(n));
    }

    // Edge case: Testing negative scenario for isStatement(Node n)
    @Test
    public void testIsStatement_Negative() {
        Node n = new Node(Token.LABEL);

        assertFalse(NodeUtil.isStatement(n));
    }

    // Edge case: Testing negative scenario for isFunctionDeclaration(Node n)
    @Test
    public void testIsFunctionDeclaration_Negative() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(n);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    // Edge case: Testing negative scenario for isHoistedFunctionDeclaration(Node n)
    @Test
    public void testIsHoistedFunctionDeclaration_Negative() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(n);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}
