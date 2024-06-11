import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_true() {
    Node forInNode = new Node(Token.FOR, 
                              new Node(Token.VAR), 
                              new Node(Token.NAME), 
                              new Node(Token.FOR_IN, 
                                       new Node(Token.NAME), 
                                       new Node(Token.NAME)));
    
    assertTrue(NodeUtil.isForIn(forInNode));
  }
  
  @Test
  public void testIsForIn_false() {
    Node notForInNode = new Node(Token.FOR, 
                                 new Node(Token.VAR), 
                                 new Node(Token.NAME), 
                                 new Node(Token.IF, 
                                          new Node(Token.NAME), 
                                          new Node(Token.NAME)));
    
    assertFalse(NodeUtil.isForIn(notForInNode));
  }
  
  @Test
  public void testIsStatement_true() {
    Node statementNode = new Node(Token.SCRIPT, 
                                  new Node(Token.NAME), 
                                  new Node(Token.BLOCK), 
                                  new Node(Token.LABEL));
    
    assertTrue(NodeUtil.isStatement(statementNode));
  }
  
  @Test
  public void testIsStatement_false() {
    Node notStatementNode = new Node(Token.FOR, 
                                     new Node(Token.VAR), 
                                     new Node(Token.NAME), 
                                     new Node(Token.NAME));
    
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }
  
  @Test
  public void testIsFunctionDeclaration_true() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, 
                                            new Node(Token.NAME), 
                                            new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node notFunctionDeclarationNode = new Node(Token.NAME, 
                                               new Node(Token.NAME), 
                                               new Node(Token.NAME));
    
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, 
                                                   new Node(Token.NAME), 
                                                   new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, 
                                                      new Node(Token.NAME), 
                                                      new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}