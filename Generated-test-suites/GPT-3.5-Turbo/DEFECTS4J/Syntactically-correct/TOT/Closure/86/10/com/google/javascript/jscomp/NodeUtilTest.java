import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  // Test case for isForIn method
  @Test
  public void testIsForIn() {
    // Typical use case with a FOR-IN loop
    Node forInLoop = new Node(Token.FOR);
    forInLoop.addChildToBack(new Node(Token.EMPTY).cloneTree());
    forInLoop.addChildToBack(new Node(Token.EMPTY).cloneTree());
    forInLoop.addChildToBack(new Node(Token.EMPTY).cloneTree());
    assertTrue(NodeUtil.isForIn(forInLoop));

    // Edge case with a FOR loop that is not a FOR-IN loop
    Node forLoop = new Node(Token.FOR);
    forLoop.addChildToBack(new Node(Token.EMPTY).cloneTree());
    forLoop.addChildToBack(new Node(Token.EMPTY).cloneTree());
    assertFalse(NodeUtil.isForIn(forLoop));

    // Error scenario with incorrect node type
    Node incorrectNode = new Node(Token.WHILE);
    assertFalse(NodeUtil.isForIn(incorrectNode));
  }
  
  // Test case for isStatement method
  @Test
  public void testIsStatement() {
    // Typical use case with a statement node
    Node statement = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(statement));

    // Edge case with a non-statement node
    Node nonStatement = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(nonStatement));

    // Error scenario with null parent node
    Node nullParent = new Node(Token.FUNCTION);
    nullParent.setParent(null);
    assertFalse(NodeUtil.isStatement(nullParent));
  }
  
  // Test case for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case with a function declaration node
    Node functionDeclaration = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(functionDeclaration);
    functionDeclaration.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    
    // Edge case with a non-function declaration node
    Node nonFunctionDeclaration = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclaration));
    
    // Error scenario with null parent node
    Node nullParent = new Node(Token.FUNCTION);
    nullParent.setParent(null);
    assertFalse(NodeUtil.isFunctionDeclaration(nullParent));
  }
  
  // Test case for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case with a hoisted function declaration node
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(hoistedFunctionDeclaration);
    hoistedFunctionDeclaration.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    
    // Edge case with a non-hoisted function declaration node
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
    
    // Error scenario with null parent node
    Node nullParent = new Node(Token.FUNCTION);
    nullParent.setParent(null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nullParent));
  }
}