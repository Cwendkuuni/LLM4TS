import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node testNode = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(testNode));
  }

  @Test
  public void testIsStatement() {
    Node testNode = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(testNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(testNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(testNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));
  }

  // Additional edge cases and exception handling scenarios can be added here

}