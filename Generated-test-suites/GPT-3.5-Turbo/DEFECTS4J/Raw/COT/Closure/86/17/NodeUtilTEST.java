import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    NodeUtil util = new NodeUtil();
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.STRING));
    node.addChildToBack(new Node(Token.BLOCK));
    assertTrue(util.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    NodeUtil util = new NodeUtil();
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(util.isStatement(node));

    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.STRING));
    assertFalse(util.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.STRING));
    assertTrue(util.isFunctionDeclaration(node));

    Node node2 = new Node(Token.STRING);
    assertFalse(util.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    NodeUtil util = new NodeUtil();
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.STRING));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    assertTrue(util.isHoistedFunctionDeclaration(node));

    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.STRING));
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node2);
    assertFalse(util.isHoistedFunctionDeclaration(node2));
  }

}