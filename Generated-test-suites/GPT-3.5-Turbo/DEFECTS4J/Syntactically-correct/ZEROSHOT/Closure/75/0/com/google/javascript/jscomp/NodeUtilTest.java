import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
      Node node = new Node(Token.FOR);
      Node child1 = new Node(Token.NAME);
      Node child2 = new Node(Token.GETPROP);
      Node child3 = new Node(Token.GETPROP);
      
      node.addChildToBack(child1);
      node.addChildToBack(child2);
      node.addChildToBack(child3);
      
      assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
      Node node = new Node(Token.WHILE);
      Node child1 = new Node(Token.NAME);
      Node child2 = new Node(Token.GETPROP);
      Node child3 = new Node(Token.GETPROP);
      
      node.addChildToBack(child1);
      node.addChildToBack(child2);
      node.addChildToBack(child3);
      
      assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
      Node node = new Node(Token.NAME);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToBack(node);
      node.setParent(parent);
      
      assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
      Node node = new Node(Token.NAME);
      Node parent = new Node(Token.EXPR_RESULT);
      parent.addChildToBack(node);
      node.setParent(parent);
      
      assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
      Node node = new Node(Token.FUNCTION);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToBack(node);
      node.setParent(parent);
      
      assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
      Node node = new Node(Token.FUNCTION);
      Node parent = new Node(Token.EXPR_RESULT);
      parent.addChildToBack(node);
      node.setParent(parent);
      
      assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
      Node node = new Node(Token.FUNCTION);
      Node parent = new Node(Token.SCRIPT);
      Node grandParent = new Node(Token.TYPEOF);
      
      grandParent.addChildToBack(parent);
      parent.addChildToBack(node);
      node.setParent(parent);
      parent.setParent(grandParent);
      
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
      Node node = new Node(Token.FUNCTION);
      Node parent = new Node(Token.SCRIPT);
      Node grandParent = new Node(Token.WHILE);
      
      grandParent.addChildToBack(parent);
      parent.addChildToBack(node);
      node.setParent(parent);
      parent.setParent(grandParent);
      
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}