import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
    Assert.assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.IF, new Node(), new Node());
    Assert.assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT, new Node());
    Node n = new Node(Token.LABEL, new Node());
    n.setParent(parent);
    Assert.assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.EXPR_RESULT, new Node());
    Node n = new Node(Token.FUNCTION, new Node());
    n.setParent(parent);
    Assert.assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT, new Node());
    Node n = new Node(Token.FUNCTION, new Node());
    n.setParent(parent);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node parent = new Node(Token.EXPR_RESULT, new Node());
    Node n = new Node(Token.STRING, new Node());
    n.setParent(parent);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node grandParent = new Node(Token.FUNCTION, new Node());
    Node parent = new Node(Token.SCRIPT, new Node());
    parent.setParent(grandParent);
    Node n = new Node(Token.FUNCTION, new Node());
    n.setParent(parent);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node grandParent = new Node(Token.SCRIPT, new Node());
    Node parent = new Node(Token.SCRIPT, new Node());
    parent.setParent(grandParent);
    Node n = new Node(Token.FUNCTION, new Node());
    n.setParent(parent);
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}