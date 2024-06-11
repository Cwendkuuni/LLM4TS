import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = // create a Node object for testing
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = // create a Node object for testing
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = // create a Node object for testing
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = // create a Node object for testing
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testIsForInEdgeCase() {
        Node node = // create a Node object representing an edge case for isForIn
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatementEdgeCase() {
        Node node = // create a Node object representing an edge case for isStatement
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclarationEdgeCase() {
        Node node = // create a Node object representing an edge case for isFunctionDeclaration
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclarationEdgeCase() {
        Node node = // create a Node object representing an edge case for isHoistedFunctionDeclaration
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}