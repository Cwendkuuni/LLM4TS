import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n));

    Node n2 = new Node(Token.IF, new Node(Token.NAME), new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));

    Node n2 = new Node(Token.IF);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    Node n2 = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    Node n2 = new Node(Token.IF);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}