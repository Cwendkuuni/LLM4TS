import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node(Token.EMPTY));
    n1.addChildToBack(new Node(Token.EMPTY));
    n1.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n1));
  }

  @Test
  public void testIsForIn_NotForInLoop() {
    Node n1 = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isForIn(n1));
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    n1.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isStatement(n1));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n1 = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isStatement(n1));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(n1));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n1 = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isFunctionDeclaration(n1));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToBack(new Node(Token.EMPTY));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(n1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToBack(new Node(Token.EMPTY));

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(n1);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
  }

}