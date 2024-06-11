import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node();
    node.setType(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(node);
    assertTrue(result);
  }

  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node();
    node.setType(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node();
    node.setParent(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node();
    node.setParent(new Node(Token.FUNCTION));
    boolean result = NodeUtil.isStatement(node);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToFront(parent2);
    node.setParent(parent2);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.BLOCK);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToFront(parent2);
    node.setParent(parent2);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }

}