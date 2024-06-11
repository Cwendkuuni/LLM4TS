import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test case for isForIn method
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(n));
    }

    // Test case for isStatement method
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    // Test case for isFunctionDeclaration method
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    // Test case for isHoistedFunctionDeclaration method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(parent1);

        n.setParent(parent1);
        parent1.setParent(parent2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}