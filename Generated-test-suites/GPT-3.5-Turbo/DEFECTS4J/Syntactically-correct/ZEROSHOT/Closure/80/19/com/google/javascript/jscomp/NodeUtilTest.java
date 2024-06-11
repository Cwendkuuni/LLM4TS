import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.WHILE);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);

    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isStatement(n);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.EMPTY);

    boolean result = NodeUtil.isStatement(n);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.EMPTY);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n = new Node(Token.OTHER);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertFalse(result);
  }

}