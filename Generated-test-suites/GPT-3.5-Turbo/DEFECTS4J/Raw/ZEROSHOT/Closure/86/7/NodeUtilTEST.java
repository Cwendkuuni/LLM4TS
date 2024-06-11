import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.VAR);
    Node child3 = new Node(Token.VAR);
    n.addChildToFront(child1);
    n.addChildToFront(child2);
    n.addChildToFront(child3);

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.VAR);
    n.addChildToFront(child1);
    n.addChildToFront(child2);

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    Node child = new Node(Token.VAR);
    n.addChildToFront(child);

    assertTrue(NodeUtil.isStatement(child));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.FUNCTION);
    Node child = new Node(Token.VAR);
    n.addChildToFront(child);

    assertFalse(NodeUtil.isStatement(child));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToFront(n);
    parent2.addChildToFront(parent1);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    grandparent.addChildToFront(parent);
    parent.addChildToFront(n);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}