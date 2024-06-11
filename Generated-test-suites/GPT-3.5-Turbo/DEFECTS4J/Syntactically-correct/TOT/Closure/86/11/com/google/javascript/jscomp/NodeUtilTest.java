import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node(Token.EXPR_RESULT));
    n1.addChildToBack(new Node(Token.EXPR_RESULT));
    n1.addChildToBack(new Node(Token.EXPR_RESULT));
    
    Node n2 = new Node(Token.FOR);
    n2.addChildToBack(new Node(Token.EXPR_RESULT));
    n2.addChildToBack(new Node(Token.EXPR_RESULT));
    
    assertFalse(NodeUtil.isForIn(n1)); // Expect false because child count is not 3
    assertTrue(NodeUtil.isForIn(n2)); // Expect true because child count is 3
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.BLOCK);
    Node n3 = new Node(Token.LABEL);

    assertTrue(NodeUtil.isStatement(n1)); // Expect true because parent type is SCRIPT
    assertTrue(NodeUtil.isStatement(n2)); // Expect true because parent type is BLOCK
    assertTrue(NodeUtil.isStatement(n3)); // Expect true because parent type is LABEL
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.EXPR_RESULT);

    // Set up parent relationship
    n2.addChildToFront(n1);
    n1.setParent(n2);

    assertTrue(NodeUtil.isFunctionDeclaration(n1)); // Expect true because type is FUNCTION and isStatement is true
    assertFalse(NodeUtil.isFunctionDeclaration(n2)); // Expect false because type is EXPR_RESULT
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.SCRIPT);

    // Set up parent relationship
    n2.addChildToBack(new Node(Token.BLOCK));
    n2.getFirstChild().addChildToFront(n1);
    n1.setParent(n2.getFirstChild());

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1)); // Expect true because of parent type
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2)); // Expect false because parent type is not SCRIPT
  }
}