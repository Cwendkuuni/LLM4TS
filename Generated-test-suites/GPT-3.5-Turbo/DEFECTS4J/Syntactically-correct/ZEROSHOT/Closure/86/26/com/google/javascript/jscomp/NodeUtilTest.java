import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.BLOCK));
    node.addChildToFront(new Node(Token.BLOCK));
    node.addChildToFront(new Node(Token.BLOCK));

    boolean result = nodeUtil.isForIn(node);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.BLOCK));
    node.addChildToFront(new Node(Token.BLOCK));

    boolean result = nodeUtil.isForIn(node);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_True() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.BLOCK);
    node.addChildToFront(child);

    boolean result = nodeUtil.isStatement(child);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.FUNCTION);
    Node child = new Node(Token.BLOCK);
    node.addChildToFront(child);

    boolean result = nodeUtil.isStatement(child);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);

    boolean result = nodeUtil.isFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);

    boolean result = nodeUtil.isFunctionDeclaration(node);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToFront(node);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(parent1);

    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.BLOCK);
    parent1.addChildToFront(node);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(parent1);

    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    assertFalse(result);
  }
}