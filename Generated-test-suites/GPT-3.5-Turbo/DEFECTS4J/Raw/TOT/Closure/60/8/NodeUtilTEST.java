import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);

    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);

    n.addChildToBack(child1);
    n.addChildToBack(child2);

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatementTrue() {
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);

    n.addChildToBack(parent);

    assertTrue(NodeUtil.isStatement(parent));
  }

  @Test
  public void testIsStatementFalse() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.NAME);

    n.addChildToBack(parent);

    assertFalse(NodeUtil.isStatement(parent));
  }

  @Test
  public void testIsFunctionDeclarationTrue() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    n.addChildToBack(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.LABEL);

    n.addChildToBack(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclarationTrue() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);

    parent1.addChildToBack(n);
    parent2.addChildToBack(parent1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    parent.addChildToBack(n);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}