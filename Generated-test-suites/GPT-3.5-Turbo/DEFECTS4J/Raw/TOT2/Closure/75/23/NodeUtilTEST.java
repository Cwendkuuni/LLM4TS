import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
    
    Node notForIn = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(notForIn));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
    
    Node notStatement = new Node(Token.IF, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(notStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    Node notFunctionDeclaration = new Node(Token.VAR, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    Node notHoistedFunction = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunction));
  }

}