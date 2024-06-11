import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    // Typical use case: FOR loop with 3 children
    Node forLoop = new Node(Token.FOR);
    forLoop.addChildToBack(new Node(Token.NAME));
    forLoop.addChildToBack(new Node(Token.EMPTY));
    forLoop.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(forLoop));
    
    // Edge case: FOR loop with less than 3 children
    Node forLoop2 = new Node(Token.FOR);
    forLoop2.addChildToBack(new Node(Token.NAME));
    forLoop2.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(forLoop2));
    
    // Edge case: FOR loop with more than 3 children
    Node forLoop3 = new Node(Token.FOR);
    forLoop3.addChildToBack(new Node(Token.NAME));
    forLoop3.addChildToBack(new Node(Token.EMPTY));
    forLoop3.addChildToBack(new Node(Token.NAME));
    forLoop3.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(forLoop3));
    
    // Edge case: Non-FOR loop
    Node whileLoop = new Node(Token.WHILE);
    whileLoop.addChildToBack(new Node(Token.NAME));
    whileLoop.addChildToBack(new Node(Token.EMPTY));
    whileLoop.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(whileLoop));
  }
  
  @Test
  public void testIsStatement() {
    // Typical use case: Node is part of a statement block
    Node block = new Node(Token.BLOCK);
    Node statement = new Node(Token.EXPR_RESULT);
    block.addChildToBack(statement);
    assertTrue(NodeUtil.isStatement(statement));
    
    // Edge case: Node is part of the script
    Node script = new Node(Token.SCRIPT);
    Node statement2 = new Node(Token.EXPR_RESULT);
    script.addChildToBack(statement2);
    assertTrue(NodeUtil.isStatement(statement2));
    
    // Edge case: Node is part of a label
    Node label = new Node(Token.LABEL);
    Node statement3 = new Node(Token.EXPR_RESULT);
    label.addChildToBack(statement3);
    assertTrue(NodeUtil.isStatement(statement3));
    
    // Edge case: Node is not part of a statement
    Node expression = new Node(Token.ADD);
    assertFalse(NodeUtil.isStatement(expression));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: Function declaration is a statement
    Node declaration = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(declaration));
    
    // Edge case: Function declaration is not a statement
    Node expression = new Node(Token.NEW);
    assertFalse(NodeUtil.isFunctionDeclaration(expression));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: Hoisted function declaration in script
    Node hoistedDeclaration = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(hoistedDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedDeclaration));
    
    // Edge case: Hoisted function declaration in function
    Node hoistedDeclaration2 = new Node(Token.FUNCTION);
    Node function = new Node(Token.FUNCTION);
    function.addChildToBack(hoistedDeclaration2);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedDeclaration2));
    
    // Edge case: Hoisted function declaration is not with root scope
    Node hoistedDeclaration3 = new Node(Token.FUNCTION);
    Node expression = new Node(Token.NEW);
    expression.addChildToBack(hoistedDeclaration3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedDeclaration3));
    
    // Edge case: Function declaration is not a hoisted declaration
    Node declaration = new Node(Token.VAR);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(declaration));
  }
}