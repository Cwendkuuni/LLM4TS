import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Before
    public void setUp() {
        // Set up any necessary objects or dependencies
    }

    @Test
    public void testIsForIn_shouldReturnTrue_whenNodeIsForInLoop() {
        Node node = new Node(Token.FOR, new Node(Token.GETELEM), new Node(Token.TRUE));

        boolean result = NodeUtil.isForIn(node);

        assertTrue(result);
    }

    @Test
    public void testIsForIn_shouldReturnFalse_whenNodeIsNotForInLoop() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.RETURN));

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement_shouldReturnTrue_whenNodeIsPartOfStatement() {
        Node node = new Node(Token.RETURN);
        Node parent = new Node(Token.SCRIPT, node);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_shouldReturnFalse_whenNodeIsNotPartOfStatement() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.FUNCTION, node);

        boolean result = NodeUtil.isStatement(node);

        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnTrue_whenNodeIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnFalse_whenNodeIsNotFunctionDeclaration() {
        Node node = new Node(Token.VAR, new Node(Token.NAME));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnTrue_whenNodeIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.BLOCK));
        Node parent1 = new Node(Token.SCRIPT, node);
        Node parent2 = new Node(Token.FUNCTION, parent1);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnFalse_whenNodeIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.RETURN));
        Node parent1 = new Node(Token.VAR, node);
        Node parent2 = new Node(Token.SCRIPT, parent1);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertFalse(result);
    }

}