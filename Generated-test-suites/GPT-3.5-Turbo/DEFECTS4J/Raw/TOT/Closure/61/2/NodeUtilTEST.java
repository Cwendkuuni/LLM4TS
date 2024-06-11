import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_emptyNode() {
    Node n = new Node(Token.FOR);

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.ADD);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node n = new Node(Token.ADD);
    Node parent = new Node(Token.IF);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(n);
    n.setParent(parent1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(n);
    n.setParent(parent2);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}