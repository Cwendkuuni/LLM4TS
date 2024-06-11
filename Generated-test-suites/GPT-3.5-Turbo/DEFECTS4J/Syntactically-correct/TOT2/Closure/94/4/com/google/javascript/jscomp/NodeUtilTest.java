import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE), new Node(Token.VAR));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION, parent, grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementWithNoParent() {
    Node n = new Node(Token.SCRIPT);
    NodeUtil.isStatement(n);
  }

}