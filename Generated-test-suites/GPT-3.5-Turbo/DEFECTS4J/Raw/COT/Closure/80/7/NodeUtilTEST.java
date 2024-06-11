
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));

    n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));

    n = new Node(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(n));

    n = new Node(Token.LABEL);
    assertTrue(NodeUtil.isStatement(n));

    n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
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

    n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(parent2);
    parent2.addChildToBack(n);
    n.setParent(parent2);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    n = new Node(Token.FUNCTION);
    parent1 = new Node(Token.BLOCK);
    parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(parent2);
    parent2.addChildToBack(n);
    n.setParent(parent2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

    n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}