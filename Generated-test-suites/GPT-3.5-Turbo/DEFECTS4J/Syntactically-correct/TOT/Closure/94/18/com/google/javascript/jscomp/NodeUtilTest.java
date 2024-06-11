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
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        // Typical use case
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR_IN));
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        
        assertTrue(nodeUtil.isForIn(node));

        // Edge case
        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR_IN));
        
        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Typical use case
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());

        assertFalse(nodeUtil.isForIn(node));
        
        // Edge case
        node = new Node(Token.FOR);

        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
        // Typical use case
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node());

        assertTrue(nodeUtil.isStatement(node));

        // Edge case
        node = new Node(Token.BLOCK);
        
        assertTrue(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
        // Typical use case
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node());

        assertFalse(nodeUtil.isStatement(node));

        // Edge case
        node = new Node(Token.LABEL);
        
        assertFalse(nodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        // Typical use case
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node());
        nodeUtil.isStatement(node);

        assertTrue(nodeUtil.isFunctionDeclaration(node));

        // Edge case
        node = new Node(Token.FUNCTION);

        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Typical use case
        Node node = new Node(Token.VAR);
        node.addChildToBack(new Node());

        assertFalse(nodeUtil.isFunctionDeclaration(node));

        // Edge case
        node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        nodeUtil.isStatement(node);

        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        // Typical use case
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        parent1.addChildToFront(new Node());
        parent2.addChildToFront(node);
        nodeUtil.isStatement(node);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));

        // Edge case
        node = new Node(Token.FUNCTION);
        parent1 = new Node(Token.SCRIPT);
        parent1.addChildToFront(node);
        nodeUtil.isStatement(node);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Typical use case
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));

        // Edge case
        node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        nodeUtil.isStatement(node);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
    }
}