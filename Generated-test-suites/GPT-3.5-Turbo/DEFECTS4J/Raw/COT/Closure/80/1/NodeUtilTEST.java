
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.addChildToFront(grandparent);
    grandparent.addChildToFront(node);
    node.setParent(grandparent);
    grandparent.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}
