import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_TypicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.BLOCK));
    node.addChildToFront(new Node(Token.ASSIGN));
    node.addChildToFront(new Node(Token.FOR_IN));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_IncorrectNodeType() {
    Node node = new Node(Token.WHILE);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_InvalidChildCount() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_StatementParent() {
    Node node = new Node(Token.STRING);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(new Node(Token.EXPR_RESULT, node));
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_NotStatementParent() {
    Node node = new Node(Token.STRING);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_FunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_Expression() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_ScriptParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.BLOCK);
    grandParent.addChildToFront(node);
    parent.addChildToFront(grandParent);
    node.setParent(parent);
    grandParent.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_FunctionParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToFront(parent);
    parent.addChildToFront(node);
    node.setParent(parent);
    parent.setParent(grandParent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}