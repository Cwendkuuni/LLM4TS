import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    n.addChildToBack(child1);
    n.addChildToBack(child2);

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node();

    n.addChildToBack(parent);

    assertTrue(NodeUtil.isStatement(parent));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.ADD);
    Node parent = new Node();

    n.addChildToBack(parent);

    assertFalse(NodeUtil.isStatement(parent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    n.addChildToBack(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();

    n.addChildToBack(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);

    n.addChildToBack(parent);
    parent.addChildToBack(grandParent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);

    n.addChildToBack(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}