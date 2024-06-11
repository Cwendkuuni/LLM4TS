import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_noForLoop() {
    Node n = new Node(Token.WHILE);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);

    assertFalse(result);
  }

  @Test
  public void testIsForIn_incompleteForLoop() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_typical() {
    Node n = new Node(Token.SCRIPT);
    Node child = new Node(Token.FUNCTION);
    n.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_notStatement() {
    Node n = new Node(Token.BLOCK);
    Node child = new Node(Token.VAR);
    n.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.injectChildrenAfter(new Node(Token.VAR), n);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertFalse(result);
  }
}