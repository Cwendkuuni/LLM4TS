import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    node1.addChildToFront(new Node(Token.NAME));
    node1.addChildToFront(new Node(Token.NAME));
    node1.addChildToFront(new Node(Token.NAME));

    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));

    Node node3 = new Node(Token.IF);
    node3.addChildToFront(new Node(Token.NAME));
    node3.addChildToFront(new Node(Token.NAME));
    node3.addChildToFront(new Node(Token.NAME));

    Assert.assertTrue(NodeUtil.isForIn(node1));
    Assert.assertFalse(NodeUtil.isForIn(node2));
    Assert.assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    Node node4 = new Node(Token.IF);
    node4.addChildToFront(new Node(Token.NAME));
    node4.addChildToFront(new Node(Token.NAME));
    
    Assert.assertTrue(NodeUtil.isStatement(node1));
    Assert.assertTrue(NodeUtil.isStatement(node2));
    Assert.assertTrue(NodeUtil.isStatement(node3));
    Assert.assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    Node node3 = new Node(Token.SCRIPT);
    node3.addChildToFront(node1);
    Node node4 = new Node(Token.BLOCK);
    node4.addChildToFront(node2);

    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node3));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    Node node3 = new Node(Token.SCRIPT);
    node3.addChildToFront(node1);
    Node node4 = new Node(Token.FUNCTION);
    node4.addChildToFront(node2);

    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNoParent() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));

    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
  }
}