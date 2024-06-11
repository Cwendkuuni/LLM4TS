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
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToFront(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToFront(node);
    grandParent.addChildToFront(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}