import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical Use Case
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
        
        // Edge Case
        assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION, new Node(Token.EMPTY))));
    }

    @Test
    public void testIsStatement() {
        // Typical Use Case
        assertTrue(NodeUtil.isStatement(new Node(Token.SCRIPT, new Node(Token.EMPTY))));
        
        // Edge Case
        assertFalse(NodeUtil.isStatement(null));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical Use Case
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.EMPTY))));
        
        // Edge Case
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.SCRIPT, new Node(Token.EMPTY))));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical Use Case
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.EMPTY))));
        
        // Edge Case
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.SCRIPT, new Node(Token.EMPTY))));
    }

}