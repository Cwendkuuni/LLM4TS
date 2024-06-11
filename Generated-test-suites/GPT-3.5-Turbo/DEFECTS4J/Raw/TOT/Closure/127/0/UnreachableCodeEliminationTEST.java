import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess_ReturnStatement() {
    Compiler compiler = new Compiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    
    Node externs = new Node(Token.ROOT);
    Node root = createTestAST_ReturnStatement();
    
    pass.process(externs, root);
    
    // Verify that the dead code after the return statement is removed
    Node result = pass.getResult();
    assertNull(result.getFirstChild().getNext());
  }
  
  @Test
  public void testProcess_NoOpStatements() {
    Compiler compiler = new Compiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    
    Node externs = new Node(Token.ROOT);
    Node root = createTestAST_NoOpStatements();
    
    pass.process(externs, root);
    
    // Verify that the no-op statements are removed
    Node result = pass.getResult();
    assertNull(result.getFirstChild().getNext());
  }
  
  @Test
  public void testProcess_SideEffectStatements() {
    Compiler compiler = new Compiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    
    Node externs = new Node(Token.ROOT);
    Node root = createTestAST_SideEffectStatements();
    
    pass.process(externs, root);
    
    // Verify that the side-effect statements are not removed
    Node result = pass.getResult();
    assertEquals(Token.IF, result.getFirstChild().getNext().getType());
  }

  private Node createTestAST_ReturnStatement() {
    // Create AST for a function with dead code after return statement
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(new Node(Token.NAME));
    Node block = new Node(Token.BLOCK);
    function.addChildToBack(block);
    block.addChildToFront(new Node(Token.RETURN));
    Node alert = new Node(Token.CALL);
    block.getFirstChild().addChildToBack(alert);
    alert.addChildToFront(new Node(Token.NAME, new Node("alert")));
    return function;
  }
  
  private Node createTestAST_NoOpStatements() {
    // Create AST for a function with no-op statements
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(new Node(Token.NAME));
    Node block = new Node(Token.BLOCK);
    function.addChildToBack(block);
    block.addChildToFront(new Node(Token.EXPR_RESULT));
    block.addChildToBack(new Node(Token.TRUE));
    return function;
  }
  
  private Node createTestAST_SideEffectStatements() {
    // Create AST for a function with side-effect statements
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(new Node(Token.NAME));
    Node block = new Node(Token.BLOCK);
    function.addChildToBack(block);
    Node ifStatement = new Node(Token.IF);
    block.addChildToFront(ifStatement);
    ifStatement.addChildToBack(new Node(Token.TRUE));
    ifStatement.addChildToBack(new Node(Token.EMPTY));
    return function;
  }
}