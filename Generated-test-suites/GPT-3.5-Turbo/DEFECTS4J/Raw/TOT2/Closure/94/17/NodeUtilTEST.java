import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Node nodeForIn = new Node(Token.FOR);
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(nodeForIn));
        
        // Edge case
        Node nodeNotForIn = new Node(Token.FOR);
        nodeNotForIn.addChildToBack(new Node(Token.NAME));
        nodeNotForIn.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
        
        // Exception handling
        try {
            NodeUtil.isForIn(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Node nodeStatement = new Node(Token.SCRIPT);
        nodeStatement.addChildToBack(new Node(Token.FUNCTION));
        
        assertTrue(NodeUtil.isStatement(nodeStatement));
        
        // Edge case
        Node nodeNotStatement = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isStatement(nodeNotStatement));
        
        // Exception handling - parent is null
        try {
            NodeUtil.isStatement(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node nodeFuncDeclaration = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFuncDeclaration));
        
        // Edge case
        Node nodeNotFuncDeclaration = new Node(Token.NAME);
        
        assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFuncDeclaration));
        
        // Exception handling
        try {
            NodeUtil.isFunctionDeclaration(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node nodeHoistedFuncDeclaration = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        parentScript.addChildToBack(nodeHoistedFuncDeclaration);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFuncDeclaration));
        
        // Edge case
        Node nodeNotHoistedFuncDeclaration = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFuncDeclaration));
        
        // Exception handling
        try {
            NodeUtil.isHoistedFunctionDeclaration(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

}