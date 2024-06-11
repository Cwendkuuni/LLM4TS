import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.VALUE));
    n.addChildToBack(new Node(Token.NODE));
    Assert.assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.VALUE);
    parent.addChildToBack(n);
    Assert.assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(n);
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithoutParentFunction() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VALUE);
    parent.addChildToBack(n);
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementWithoutParent() {
    Node n = new Node(Token.BLOCK);
    NodeUtil.isStatement(n);
  }
}