import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node n = new Node(Token.WHILE, parent, new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node parent = new Node(Token.EXPR_VOID, new Node(Token.EMPTY));
    Node n = new Node(Token.WHILE, parent, new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_NotDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.EXPR_RESULT, n);
    n.setParent(parent);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent1 = new Node(Token.SCRIPT, n);
    n.setParent(parent1);
    Node parent2 = new Node(Token.FUNCTION, parent1);
    parent1.setParent(parent2);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent1 = new Node(Token.SCRIPT, n);
    n.setParent(parent1);
    Node parent2 = new Node(Token.SCRIPT, parent1);
    parent1.setParent(parent2);
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}