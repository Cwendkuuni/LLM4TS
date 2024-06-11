import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination unreachableCodeElimination;

  @Before
  public void setUp() {
    unreachableCodeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }

  @Test
  public void testProcess_WithNullNodes_ShouldNotThrowException() {
    // Arrange
    Node externs = null;
    Node toplevel = null;

    // Act & Assert
    unreachableCodeElimination.process(externs, toplevel);
  }

  @Test
  public void testProcess_WithSingleNode_ShouldRemoveNode() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.TOP);

    // Act
    unreachableCodeElimination.process(externs, toplevel);

    // Assert
    assertNull(toplevel.getFirstChild());
  }

  @Test
  public void testProcess_WithCodeAfterReturnStatement_ShouldRemoveCodeAfterReturn() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.TOP);
    Node returnNode = new Node(Token.RETURN);
    Node codeAfterReturn = new Node(Token.EXPR_RESULT);

    toplevel.addChildToFront(returnNode);
    toplevel.addChildToBack(codeAfterReturn);

    // Act
    unreachableCodeElimination.process(externs, toplevel);

    // Assert
    assertNull(returnNode.getNext());
    assertNull(codeAfterReturn.getNext());
  }

  @Test
  public void testProcess_WithNoOpStatements_ShouldRemoveNoOpStatements() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.TOP);
    Node noOpStatement = new Node(Token.BLOCK);

    toplevel.addChildToFront(noOpStatement);

    // Act
    unreachableCodeElimination.process(externs, toplevel);

    // Assert
    assertNull(toplevel.getFirstChild());
  }

  @Test
  public void testProcess_WithUselessLiveCode_ShouldRemoveUselessLiveCode() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.TOP);
    Node uselessLiveCode = new Node(Token.BLOCK);

    toplevel.addChildToFront(uselessLiveCode);

    // Act
    unreachableCodeElimination.process(externs, toplevel);

    // Assert
    assertNull(toplevel.getFirstChild());
  }

}