import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);

    assertTrue(nodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.IF);
    Node child1 = new Node();
    n.addChildToBack(child1);

    assertFalse(nodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node();
    Node parent = new Node(Token.BLOCK);
    n.setParent(parent);

    assertTrue(nodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node();
    Node parent = new Node(Token.EXPR_RESULT);
    n.setParent(parent);

    assertFalse(nodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    assertTrue(nodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.IF);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    assertFalse(nodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    assertTrue(nodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    Node grandParent = new Node(Token.SCRIPT);
    n.setParent(parent);
    parent.setParent(grandParent);

    assertFalse(nodeUtil.isHoistedFunctionDeclaration(n));
  }

}