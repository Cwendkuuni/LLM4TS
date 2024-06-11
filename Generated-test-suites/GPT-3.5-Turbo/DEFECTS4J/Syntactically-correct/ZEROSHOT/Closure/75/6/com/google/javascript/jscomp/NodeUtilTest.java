
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    Assert.assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.NAME);
    
    Assert.assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    Assert.assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.NAME);
    
    Assert.assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node functionParent = new Node(Token.FUNCTION);
    Node scriptParent = new Node(Token.SCRIPT);
    functionParent.addChildToBack(node);
    scriptParent.addChildToBack(functionParent);
    node.setParent(functionParent);
    functionParent.setParent(scriptParent);
    
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}
