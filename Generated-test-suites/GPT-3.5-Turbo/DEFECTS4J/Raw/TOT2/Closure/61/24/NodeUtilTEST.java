import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node root = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION, root);
    root.addChildToBack(n);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION, script);
    script.addChildToBack(function);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  // Additional test cases for edge cases and error scenarios can be added here
}