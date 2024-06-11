import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.NAME, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node scriptNode = new Node(Token.SCRIPT, functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}