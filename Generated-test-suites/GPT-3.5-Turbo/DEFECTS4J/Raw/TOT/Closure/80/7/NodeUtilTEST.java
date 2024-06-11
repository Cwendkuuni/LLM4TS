import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @After
  public void tearDown() {
    nodeUtil = null;
  }

  @Test
  public void testIsForIn_WhenNodeIsForIn_ExpectTrue() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToFront(new Node(Token.ADD));
    node.addChildToBack(new Node(Token.NUMBER));
    Assert.assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ExpectFalse() {
    Node node = new Node(Token.WHILE);
    Assert.assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_WhenNodeIsStatement_ExpectTrue() {
    Node node = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ExpectFalse() {
    Node node = new Node(Token.ADD);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ExpectTrue() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ExpectFalse() {
    Node node = new Node(Token.ADD);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ExpectTrue() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ExpectFalse() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.ADD);
    parent.addChildToFront(node);
    node.setParent(parent);
    Assert.assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }
}