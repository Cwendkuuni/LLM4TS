import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, 
        new Node(Token.VAR),
        new Node(Token.TRUE),
        new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));

    node = new Node(Token.FUNCTION,
        new Node(Token.PARAM_LIST),
        new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.FUNCTION,
        new Node(Token.PARAM_LIST),
        new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(node));

    node = new Node(Token.ARRAYLIT, new Node(Token.TRUE));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION,
        new Node(Token.PARAM_LIST),
        new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node(Token.ARRAYLIT, new Node(Token.TRUE));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION,
        new Node(Token.PARAM_LIST),
        new Node(Token.EMPTY));
    node.setParent(new Node(Token.SCRIPT, node));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = new Node(Token.ARRAYLIT, new Node(Token.TRUE));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}