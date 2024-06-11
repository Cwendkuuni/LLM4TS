import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination elimination;

    @Before
    public void setUp() {
        elimination = new DeadAssignmentsElimination(new AbstractCompiler());
    }

    @Test
    public void testTryRemoveAssignment_simpleAssignment() {
        // Arrange
        Node node = new Node(Token.EXPR_RESULT);
        Node assignment = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.EXPR_RESULT));
        Node rhs = new Node(Token.NUMBER);
        node.addChildToFront(assignment);
        assignment.addChildToFront(lhs);
        assignment.addChildToBack(rhs);

        // Act
        elimination.tryRemoveAssignment(node, assignment, null);

        // Assert
        assertNull(assignment.getFirstChild());
        assertNull(assignment.getLastChild());  
    }

    @Test
    public void testTryRemoveAssignment_identityAssignment() {
        // Arrange
        Node node = new Node(Token.EXPR_RESULT);
        Node assignment = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.EXPR_RESULT));
        Node rhs = new Node(Token.NAME, lhs.cloneNode());
        node.addChildToFront(assignment);
        assignment.addChildToFront(lhs);
        assignment.addChildToBack(rhs);

        // Act
        elimination.tryRemoveAssignment(node, assignment, null);

        // Assert
        assertNull(assignment.getFirstChild());
        assertNull(assignment.getLastChild());  
    }

    @Test
    public void testTryRemoveAssignment_assignmentWithLiveVariableBefore() {
        // Arrange
        Node node = new Node(Token.EXPR_RESULT);
        Node assignment = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.EXPR_RESULT));
        Node rhs = new Node(Token.NAME);
        Node liveVariable = new Node(Token.EXPR_RESULT, rhs.cloneNode());
        node.addChildToFront(assignment);
        assignment.addChildToFront(lhs);
        assignment.addChildToBack(rhs);
        node.addChildAfter(liveVariable, assignment);

        // Act
        elimination.tryRemoveAssignment(node, assignment, null);

        // Assert
        assertNotNull(assignment.getFirstChild());
        assertNotNull(assignment.getLastChild());
    }

    // Edge Cases

    @Test
    public void testTryRemoveAssignment_variableEscapedByClosure() {
        // Arrange
        Node node = new Node(Token.EXPR_RESULT);
        Node assignment = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.EXPR_RESULT));
        Node rhs = new Node(Token.NAME);
        Node closure = new Node(Token.CLOSURE);
        node.addChildToFront(assignment);
        assignment.addChildToFront(lhs);
        assignment.addChildToBack(rhs);
        closure.addChildToBack(assignment.getParent());

        // Act
        elimination.tryRemoveAssignment(node, assignment, null);

        // Assert
        assertNotNull(assignment.getFirstChild());
        assertNotNull(assignment.getLastChild());
    }

    @Test
    public void testTryRemoveAssignment_emptyNode() {
        // Arrange
        Node node = new Node(Token.EMPTY);

        // Act
        elimination.tryRemoveAssignment(node, node, null);

        // Assert
        assertTrue(true); // No exception thrown
    }

    // Error Scenarios

    @Test
    public void testTryRemoveAssignment_nonLocalVariableAssignment() {
        // Arrange
        Node node = new Node(Token.EXPR_RESULT);
        Node assignment = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.THIS);
        Node rhs = new Node(Token.NAME);
        node.addChildToFront(assignment);
        assignment.addChildToFront(lhs);
        assignment.addChildToBack(rhs);

        // Act
        elimination.tryRemoveAssignment(node, assignment, null);

        // Assert
        assertNotNull(assignment.getFirstChild());
        assertNotNull(assignment.getLastChild());
    }

}