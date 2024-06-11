
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_ReturnsFalse() {
    Node node = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_ReturnsFalse() {
    Node node = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.ASSIGN, new Node(Token.NAME), new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.BLOCK);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}

