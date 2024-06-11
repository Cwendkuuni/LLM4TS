
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.FOR);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_NotForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.WHILE);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node block = new Node(Token.BLOCK);
    Node label = new Node(Token.LABEL);
    Node child = new Node(Token.NAME);
    label.addChildToBack(child);
    block.addChildToBack(label);
    script.addChildToBack(block);
    
    assertTrue(NodeUtil.isStatement(label));
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node script = new Node(Token.SCRIPT);
    Node block = new Node(Token.BLOCK);
    Node label = new Node(Token.LABEL);
    Node child = new Node(Token.NAME);
    label.addChildToBack(child);
    block.addChildToBack(label);
    script.addChildToBack(block);
    
    assertFalse(NodeUtil.isStatement(script));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(function);
    
    assertTrue(NodeUtil.isFunctionDeclaration(function));
  }
  
  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node expression = new Node(Token.EXPR_RESULT);
    expression.addChildToBack(function);
    
    assertFalse(NodeUtil.isFunctionDeclaration(expression));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    script.addChildToBack(function);
    script.addChildToBack(parent);
    parent.addChildToBack(function);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(function);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }
}
