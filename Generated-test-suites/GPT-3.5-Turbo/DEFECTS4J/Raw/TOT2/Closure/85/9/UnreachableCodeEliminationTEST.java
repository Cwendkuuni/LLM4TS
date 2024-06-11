import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

    @Test
    public void testRemoveDeadCodeAfterReturn() {
        // Test case for removing dead code after a return statement
        // Setup
        UnreachableCodeElimination uce = new UnreachableCodeElimination();
        
        // Given
        Node rootNode = new Node(Token.SCRIPT);
        Node ifNode = new Node(Token.IF);
        Node returnNode = new Node(Token.RETURN);
        Node alertNode = new Node(Token.CALL, new Node(Token.NAME, "alert"));
        alertNode.addChildToBack(new Node(Token.STRING, "unreachable"));
        ifNode.addChildToBack(returnNode);
        ifNode.addChildToBack(alertNode);
        rootNode.addChildToBack(ifNode);
        
        // When
        uce.process(null, rootNode);
        
        // Then
        // Check that the "alert" call is removed
        assertEquals(1, rootNode.getChildCount());
        assertEquals(Token.IF, rootNode.getFirstChild().getType());
        assertEquals(Token.RETURN, rootNode.getFirstChild().getFirstChild().getType());
    }

    @Test
    public void testRemoveDeadCodeWithNoSideEffects() {
        // Test case for removing dead code with no side effects
        // Setup
        UnreachableCodeElimination uce = new UnreachableCodeElimination();
        
        // Given
        Node rootNode = new Node(Token.SCRIPT);
        Node statement1 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "Statement 1"));
        Node statement2 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "Statement 2"));
        rootNode.addChildToBack(statement1);
        rootNode.addChildToBack(statement2);
        
        // When
        uce.process(null, rootNode);
        
        // Then
        // Check that the second statement is removed
        assertEquals(1, rootNode.getChildCount());
        assertEquals(Token.STRING, rootNode.getFirstChild().getType());
        assertEquals("Statement 1", rootNode.getFirstChild().getString());
    }

    // Add more test cases for edge cases and exceptions

}