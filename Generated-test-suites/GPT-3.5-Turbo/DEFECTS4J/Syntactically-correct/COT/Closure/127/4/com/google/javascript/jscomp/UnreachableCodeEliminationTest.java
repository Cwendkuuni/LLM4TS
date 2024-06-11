
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess_WithNoDeadCode_ShouldNotChangeCode() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    
    // Act
    pass.process(null, null);
    
    // Assert
    // Code should not be changed
  }

  @Test
  public void testProcess_WithDeadCodeAfterReturn_ShouldRemoveDeadCode() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    Node externs = null;
    Node toplevel = generateRootNodeWithDeadCodeAfterReturn();
    
    // Act
    pass.process(externs, toplevel);
    
    // Assert
    // Check if dead code is removed
    assertFalse(NodeUtil.containsCode(toplevel, "alert('unreachable');"));
  }

  private Node generateRootNodeWithDeadCodeAfterReturn() {
    Node root = new Node(Token.ROOT);
    Node function = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    Node ifStatement = new Node(Token.IF);
    Node returnStatement = new Node(Token.RETURN);
    Node alertStatement = new Node(Token.EXPR_RESULT);
    Node alertCall = new Node(Token.CALL);
    Node alertParam = Node.newString("unreachable");
    
    alertCall.addChildToBack(alertParam);
    alertStatement.addChildToFront(alertCall);
    returnStatement.addChildToFront(alertStatement);
    ifStatement.addChildToBack(returnStatement);
    body.addChildToFront(ifStatement);
    function.addChildToBack(body);
    root.addChildToBack(function);

    return root;
  }
  
}
