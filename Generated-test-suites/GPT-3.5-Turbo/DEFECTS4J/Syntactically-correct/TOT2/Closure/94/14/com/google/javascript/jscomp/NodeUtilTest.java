import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY),
        new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.WHILE, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_True() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node expressionNode = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
    scriptNode.addChildToBack(expressionNode);
    assertTrue(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsStatement_False() {
    Node expressionNode = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node expressionNode = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node functionNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}