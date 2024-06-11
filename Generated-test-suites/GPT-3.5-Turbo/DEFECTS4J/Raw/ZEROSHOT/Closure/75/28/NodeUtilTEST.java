import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.NAME);
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.SCRIPT));
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent2);
    parent2.setParent(parent1);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }

}