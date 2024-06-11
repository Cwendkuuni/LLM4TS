import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.FOR);
    Node child2 = new Node(Token.FOR);
    Node child3 = new Node(Token.FOR);

    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertEquals(true, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInFalse() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.FOR);
    Node child2 = new Node(Token.FOR);

    node.addChildToBack(child1);
    node.addChildToBack(child2);

    assertEquals(false, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.FOR);

    node.addChildToBack(child);

    assertEquals(true, NodeUtil.isStatement(child));
  }

  @Test
  public void testIsStatementFalse() {
    Node node = new Node(Token.SCRIPT);
    Node child = new Node(Token.VAR);

    node.addChildToBack(child);

    assertEquals(false, NodeUtil.isStatement(child));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    parent.addChildToBack(node);

    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);

    parent.addChildToBack(node);

    assertEquals(false, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    parent.addChildToBack(node);

    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.FUNCTION);

    grandparent.addChildToBack(parent);
    parent.addChildToBack(node);

    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
  }
}