import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));

    n.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
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
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    n = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(function);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));

    Node functionExpression = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(functionExpression);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));
  }
}