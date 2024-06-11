import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToFront(new Node(Token.STRING));
    node.addChildToFront(new Node(Token.NUMBER));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.STRING);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING);
    parent.addChildToFront(node);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.FUNCTION);
    grandParent.addChildToFront(node);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(grandParent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}