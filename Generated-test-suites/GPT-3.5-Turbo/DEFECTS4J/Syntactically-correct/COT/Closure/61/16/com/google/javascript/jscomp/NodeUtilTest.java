
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.LP));
    n.addChildToBack(new Node(Token.RP));
    assertTrue(NodeUtil.isForIn(n));

    Node n2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.VAR);
    assertTrue(NodeUtil.isStatement(n1));

    Node n2 = new Node(Token.ADD);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n1));

    Node n2 = new Node(Token.ADD);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

    Node n2 = new Node(Token.FUNCTION);
    n2.getParent().setType(Token.SCRIPT);
    n2.getParent().getParent().setType(Token.FUNCTION);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));

    Node n3 = new Node(Token.ADD);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
  }
}

