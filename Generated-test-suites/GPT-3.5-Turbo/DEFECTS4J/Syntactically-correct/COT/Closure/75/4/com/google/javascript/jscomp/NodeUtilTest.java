
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test positive case
    Node node = new Node(Token.FOR, new Node[3]);
    assertEquals(true, NodeUtil.isForIn(node));

    // Test negative case
    node = new Node(Token.IF, new Node[2]);
    assertEquals(false, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    // Test positive case
    Node node = new Node(Token.SCRIPT, new Node[0]);
    assertEquals(true, NodeUtil.isStatement(node));

    // Test negative case
    node = new Node(Token.NAME, new Node[0]);
    assertEquals(false, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test positive case
    Node node = new Node(Token.FUNCTION, new Node[0]);
    node.setParent(new Node(Token.SCRIPT, new Node[0]));
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));

    // Test negative case
    node = new Node(Token.NAME, new Node[0]);
    node.setParent(new Node(Token.SCRIPT, new Node[0]));
    assertEquals(false, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test positive case
    Node node = new Node(Token.FUNCTION, new Node[0]);
    Node parent = new Node(Token.SCRIPT, new Node[0]);
    node.setParent(parent);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));

    // Test negative case
    node = new Node(Token.NAME, new Node[0]);
    node.setParent(new Node(Token.BLOCK, new Node[0]));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
  }

}
