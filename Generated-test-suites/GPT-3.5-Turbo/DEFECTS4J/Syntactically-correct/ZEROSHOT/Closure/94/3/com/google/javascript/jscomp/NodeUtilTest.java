import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child0 = new Node();
    Node child1 = new Node();
    Node child2 = new Node();
    n.addChildToBack(child0);
    n.addChildToBack(child1);
    n.addChildToBack(child2);

    boolean result = NodeUtil.isForIn(n);

    assertTrue(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node();
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    boolean result = NodeUtil.isStatement(n);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.addChildToBack(grandparent);
    grandparent.addChildToBack(n);
    n.setParent(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertTrue(result);
  }

}