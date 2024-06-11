import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.NAME, "arr"), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isForIn(node1));

    Node node2 = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.NAME, "arr"), new Node(Token.NAME, "object"));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(node1));

    Node node2 = new Node(Token.NAME, "i");
    assertFalse(NodeUtil.isStatement(node2));

    Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    Node node2 = new Node(Token.NAME, "func");
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    Node node3 = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN, new Node(Token.NAME, "func"), new Node(Token.NAME, "value")));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    node1.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

    Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    node2.getParent().setType(Token.FUNCTION);
    node2.getParent().getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

    Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    node3.getParent().setType(Token.FUNCTION);
    node3.getParent().getParent().setType(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

    Node node4 = new Node(Token.FUNCTION, new Node(Token.NAME, "func"), new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
}