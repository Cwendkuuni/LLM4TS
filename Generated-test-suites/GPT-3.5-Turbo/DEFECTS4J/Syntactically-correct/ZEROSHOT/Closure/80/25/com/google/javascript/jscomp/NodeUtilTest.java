import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node exprNode = new Node(Token.EXPR_RESULT);
    scriptNode.addChildToBack(exprNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(exprNode));
  }

  // Additional tests for other methods in NodeUtil class can be included here

}