import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node n = new Node(Token.WHILE);
    n.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.VAR);
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
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}