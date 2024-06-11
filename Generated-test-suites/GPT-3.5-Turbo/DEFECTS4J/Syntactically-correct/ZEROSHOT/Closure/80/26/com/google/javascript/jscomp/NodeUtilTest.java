import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.WHILE);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(node);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    script.addChildToBack(functionNode);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(script);
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    script.addChildToBack(functionNode);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(script);
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}