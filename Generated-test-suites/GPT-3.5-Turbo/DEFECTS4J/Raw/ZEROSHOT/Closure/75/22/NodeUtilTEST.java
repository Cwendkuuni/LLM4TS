import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_True() {
    Node n = new Node(Token.FOR, 
            new Node(Token.EMPTY, 
              new Node(Token.EMPTY), 
              new Node(Token.EMPTY)), 
            new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_False() {
    Node n = new Node(Token.IF, 
            new Node(Token.EMPTY, 
              new Node(Token.EMPTY), 
              new Node(Token.EMPTY)), 
            new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement_True() {
    Node n = new Node(Token.ASSIGN, 
            new Node(Token.EMPTY), 
            new Node(Token.EMPTY));
    n.setParent(new Node(Token.BLOCK));

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_False() {
    Node n = new Node(Token.ASSIGN, 
            new Node(Token.EMPTY), 
            new Node(Token.EMPTY));
    n.setParent(new Node(Token.EXPR_RESULT));

    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node n = new Node(Token.FUNCTION, 
            new Node(Token.EMPTY), 
            new Node(Token.EMPTY));
    n.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node n = new Node(Token.FUNCTION, 
            new Node(Token.EMPTY), 
            new Node(Token.EMPTY));
    n.setParent(new Node(Token.EXPR_RESULT));

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.setParent(grandparent);
    Node n = new Node(Token.FUNCTION,
            new Node(Token.EMPTY),
            new Node(Token.EMPTY));
    n.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node parent = new Node(Token.SCRIPT);
    parent.setParent(new Node(Token.EXPR_RESULT));
    Node n = new Node(Token.FUNCTION,
            new Node(Token.EMPTY),
            new Node(Token.EMPTY));
    n.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}