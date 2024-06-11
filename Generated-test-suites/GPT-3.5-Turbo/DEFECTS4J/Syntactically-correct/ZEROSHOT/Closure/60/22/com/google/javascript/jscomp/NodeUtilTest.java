import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    node1.addChildToFront(new Node(Token.NUMBER));
    node1.addChildToFront(new Node(Token.STRING));
    node1.addChildToFront(new Node(Token.STRING));

    Node node2 = new Node(Token.IF);
    node2.addChildToFront(new Node(Token.NUMBER));
    node2.addChildToFront(new Node(Token.STRING));
    node2.addChildToFront(new Node(Token.STRING));

    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    node1.addChildToFront(new Node(Token.NUMBER));
    node1.addChildToFront(new Node(Token.STRING));
    node1.addChildToFront(new Node(Token.STRING));

    Node node2 = new Node(Token.IF);
    node2.addChildToFront(new Node(Token.NUMBER));
    node2.addChildToFront(new Node(Token.STRING));
    node2.addChildToFront(new Node(Token.STRING));

    assertTrue(NodeUtil.isStatement(node1));
    assertFalse(NodeUtil.isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NUMBER));
    node1.addChildToFront(new Node(Token.STRING));
    node1.addChildToFront(new Node(Token.STRING));

    Node node2 = new Node(Token.IF);
    node2.addChildToFront(new Node(Token.NUMBER));
    node2.addChildToFront(new Node(Token.STRING));
    node2.addChildToFront(new Node(Token.STRING));

    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NUMBER));
    node1.addChildToFront(new Node(Token.STRING));
    node1.addChildToFront(new Node(Token.STRING));
    node1.setParent(new Node(Token.SCRIPT));

    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NUMBER));
    node2.addChildToFront(new Node(Token.STRING));
    node2.addChildToFront(new Node(Token.STRING));
    node2.setParent(new Node(Token.FUNCTION));
    node2.getParent().setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
  }
}