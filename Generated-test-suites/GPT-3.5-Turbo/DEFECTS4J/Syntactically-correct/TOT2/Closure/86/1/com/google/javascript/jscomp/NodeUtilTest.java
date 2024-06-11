import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.CONST);
    node.setParent(parent);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  // Additional test cases for edge cases and error scenarios could be added here

}