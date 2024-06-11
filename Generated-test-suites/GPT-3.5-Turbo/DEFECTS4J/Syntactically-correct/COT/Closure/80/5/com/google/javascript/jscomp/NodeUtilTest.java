
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test a node that represents a FOR-IN loop
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node());
    n1.addChildToBack(new Node());
    n1.addChildToBack(new Node());
    assertTrue(NodeUtil.isForIn(n1));

    // Test a node that does not represent a FOR-IN loop
    Node n2 = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    // Test a node that is used as a statement
    Node n1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n1));

    // Test a node that is not used as a statement
    Node n2 = new Node(Token.NUMBER);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test a node that is a function declaration
    Node n1 = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n1));

    // Test a node that is not a function declaration
    Node n2 = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test a node that is a hoisted function declaration
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToBack(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

    // Test a node that is not a hoisted function declaration
    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToBack(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }

}
