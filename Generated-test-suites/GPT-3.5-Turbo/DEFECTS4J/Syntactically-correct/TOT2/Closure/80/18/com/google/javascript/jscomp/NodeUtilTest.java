import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    grandParent.addChildToBack(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStatementWithNoParent() {
    Node node = new Node(Token.NAME);
    NodeUtil.isStatement(node); // this should throw an IllegalArgumentException
  }

}