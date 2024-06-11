import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));
        assertTrue(nodeUtil.isForIn(n1));

        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));
        assertFalse(nodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.EMPTY);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertTrue(nodeUtil.isStatement(n1));

        Node n2 = new Node(Token.EMPTY);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        assertFalse(nodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertTrue(nodeUtil.isFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        assertFalse(nodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.BLOCK);
        Node grandParent2 = new Node(Token.FUNCTION);
        grandParent2.addChildToBack(parent2);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        parent2.setParent(grandParent2);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(n2));
    }

}