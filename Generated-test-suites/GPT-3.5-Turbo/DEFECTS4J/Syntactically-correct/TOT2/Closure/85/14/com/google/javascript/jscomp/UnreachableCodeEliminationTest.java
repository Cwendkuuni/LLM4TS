import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

    @Test
    public void testRemoveDeadCodeAfterReturn() {
        // Create test input node with dead code after return
        Node testNode = createTestNodeWithDeadCodeAfterReturn();
        
        // Apply UnreachableCodeElimination pass
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.process(null, testNode);
        
        // Test that dead code after return is removed
        assertTrue(testNode.getChildAtIndex(1).isEmpty());
    }
    
    private Node createTestNodeWithDeadCodeAfterReturn() {
        // Create test node with dead code after return
        Node testNode = new Node(Token.FUNCTION);
        Node returnNode = new Node(Token.RETURN);
        Node alertNode = new Node(Token.ASSERT);
        
        alertNode.setString("unreachable");
        
        testNode.addChildToBack(returnNode);
        testNode.addChildToBack(alertNode);
        
        return testNode;
    }

    @Test
    public void testRemoveDeadCodeNoOpStatements() {
        // Create test input node with no-op statements
        Node testNode = createTestNodeWithNoOpStatements();
        
        // Apply UnreachableCodeElimination pass
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.process(null, testNode);
        
        // Test that no-op statements are removed
        assertEquals(0, testNode.getChildAtIndex(1).getString().length());
    }
    
    private Node createTestNodeWithNoOpStatements() {
        // Create test node with no-op statements
        Node testNode = new Node(Token.FUNCTION);
        Node noOpNode1 = new Node(Token.CALL);
        Node noOpNode2 = new Node(Token.TRUE);
        
        testNode.addChildToBack(noOpNode1);
        testNode.addChildToBack(noOpNode2);
        
        return testNode;
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // Create test input node with unconditional branching
        Node testNode = createTestNodeWithUnconditionalBranching();
        
        // Apply UnreachableCodeElimination pass
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.process(null, testNode);
        
        // Test that unconditional branching is removed
        assertEquals(0, testNode.getChildAtIndex(1).numberOfChildren());
    }
    
    private Node createTestNodeWithUnconditionalBranching() {
        // Create test node with unconditional branching
        Node testNode = new Node(Token.FUNCTION);
        Node returnNode1 = new Node(Token.RETURN);
        Node returnNode2 = new Node(Token.RETURN);
        
        returnNode2.addChildToBack(returnNode1);
        testNode.addChildToBack(returnNode2);
        
        return testNode;
    }
}