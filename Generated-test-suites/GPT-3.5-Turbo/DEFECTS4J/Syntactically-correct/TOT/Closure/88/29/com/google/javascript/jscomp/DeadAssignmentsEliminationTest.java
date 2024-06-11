import static org.junit.Assert.*;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testProcess_WithUselessLocalAssignment_RemovesAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    // Create a function block with an assignment to a local variable that is not used
    Node fnBlock = new Node(Token.BLOCK);
    Node assignment = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, new Node(Token.AND, new Node(Token.NAME, "a"), new Node(Token.NAME, "b")));
    Node rhs = new Node(Token.NUMBER, 1.0);
    assignment.addChildToFront(lhs);
    assignment.addChildToBack(rhs);
    fnBlock.addChildToBack(assignment);

    // Create a condition expression for the IF node
    Node condition = new Node(Token.AND, new Node(Token.NAME, "c"), new Node(Token.NAME, "d"));

    // Create the IF node with the function block as its child
    Node ifNode = new Node(Token.IF, condition, fnBlock);

    root.addChildToBack(ifNode);

    // Act
    elimination.process(externs, root);

    // Assert
    assertNull(condition.getFirstChild());
    assertEquals(rhs, condition.getSecondChild());
    assertFalse(fnBlock.hasChildren());
  }

  @Test
  public void testProcess_WithNonLocalVariable_EscapeToClosure() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    // Create a scope with a non-local variable
    Scope globalScope = new Scope(null, null);
    Var nonLocalVar = new Var("x", null, true);
    globalScope.addLocal(nonLocalVar);
    globalScope.declare("x", null, null, true);
    NodeTraversal.traverse(compiler, root, elimination);

    // Act
    elimination.process(externs, root);

    // Assert
    // The non-local variable should be untouched
    assertNotNull(globalScope.getVar("x"));
    assertTrue(NodeUtil.isName(root));
  }

  @Test
  public void testProcess_WithUnreachableCode_IgnoreUnreachableCode() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    // Create an IF node with unreachable code
    Node condition = new Node(Token.FALSE);
    Node unreachableCode = new Node(Token.BLOCK);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 1.0));
    unreachableCode.addChildToBack(assignment);
    Node ifNode = new Node(Token.IF, condition, unreachableCode);

    root.addChildToBack(ifNode);

    // Act
    elimination.process(externs, root);

    // Assert
    assertNull(condition.getNext());
    assertFalse(unreachableCode.hasChildren());
  }

  // Add more test cases for other methods

}