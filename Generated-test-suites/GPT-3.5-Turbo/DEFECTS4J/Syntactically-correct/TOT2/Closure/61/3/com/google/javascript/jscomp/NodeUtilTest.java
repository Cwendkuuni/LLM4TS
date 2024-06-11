import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(parent, Token.FUNCTION);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(parent, Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node node = new Node(parent, grandParent, Token.FUNCTION);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  // Additional test cases for edge cases and exceptions can be added here

}