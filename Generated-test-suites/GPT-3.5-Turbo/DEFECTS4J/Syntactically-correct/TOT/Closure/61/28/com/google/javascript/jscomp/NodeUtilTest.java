import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.VAR));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));

    Node n2 = new Node(Token.FOR);
    n2.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node child = new Node(Token.IF);
    n.addChildToBack(child);

    assertTrue(NodeUtil.isStatement(child));

    Node n2 = new Node(Token.EXPR_RESULT);

    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(n));

    Node n2 = new Node(Token.EXPR_RESULT);
    Node child = new Node(Token.FUNCTION);
    n2.addChildToBack(child);

    assertFalse(NodeUtil.isFunctionDeclaration(child));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    Node n2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent2.addChildToBack(grandparent);
    grandparent.addChildToBack(n2);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}