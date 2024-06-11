import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_whenNodeIsForIn_returnTrue() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.EMPTY)); 
    n.addChildToBack(new Node(Token.BLOCK));

    boolean result = NodeUtil.isForIn(n);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_whenNodeIsNotForIn_returnFalse() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.PARAM_LIST));
    n.addChildToBack(new Node(Token.BLOCK));

    boolean result = NodeUtil.isForIn(n);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_whenNodeIsStatement_returnTrue() {
    Node n = new Node(Token.SCRIPT);

    boolean result = NodeUtil.isStatement(n);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_whenNodeIsNotStatement_returnFalse() {
    Node n = new Node(Token.NAME);

    boolean result = NodeUtil.isStatement(n);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_whenNodeIsFunctionDeclaration_returnTrue() {
    Node n = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_whenNodeIsNotFunctionDeclaration_returnFalse() {
    Node n = new Node(Token.NAME);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_whenNodeIsHoistedFunctionDeclaration_returnTrue() {
    Node n = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_whenNodeIsNotHoistedFunctionDeclaration_returnFalse() {
    Node n = new Node(Token.FUNCTION);

    Node parent = new Node(Token.IF);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertFalse(result);
  }
}