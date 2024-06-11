import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    assertTrue(util.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    assertFalse(util.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.SCRIPT);
    assertTrue(util.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FUNCTION);
    node.addChildToBack(new Node());
    assertFalse(util.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FUNCTION);
    assertTrue(util.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.SCRIPT);
    assertFalse(util.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FUNCTION);
    node.addChildToBack(new Node());
    Node parent1 = new Node();
    parent1.setType(Token.SCRIPT);
    parent1.addChildToBack(node);
    Node parent2 = new Node();
    parent2.setType(Token.FUNCTION);
    parent2.addChildToBack(parent1);
    assertTrue(util.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node();
    node.setType(Token.FUNCTION);
    node.addChildToBack(new Node());
    Node parent1 = new Node();
    parent1.setType(Token.BLOCK);
    parent1.addChildToBack(node);
    Node parent2 = new Node();
    parent2.setType(Token.FUNCTION);
    parent2.addChildToBack(parent1);
    assertFalse(util.isHoistedFunctionDeclaration(node));
  }

}