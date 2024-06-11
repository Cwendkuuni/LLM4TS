
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.RETURN);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.NAME);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(parent1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration1() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration2() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}

