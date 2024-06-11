import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    Node n2 = new Node(Token.FOR);
    n2.addChildToBack(new Node(Token.EMPTY));
    n2.addChildToBack(new Node(Token.EMPTY));
    n2.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n2));
    assertFalse(NodeUtil.isForIn(n1)); 
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.BLOCK);
    Node n3 = new Node(Token.LABEL);
    Node n4 = new Node(Token.EMPTY);
    Node n5 = new Node(Token.FUNCTION);
    Node n6 = new Node(Token.NAME);

    assertTrue(NodeUtil.isStatement(n1));
    assertTrue(NodeUtil.isStatement(n2));
    assertTrue(NodeUtil.isStatement(n3));
    assertFalse(NodeUtil.isStatement(n4));
    assertFalse(NodeUtil.isStatement(n5));
    assertFalse(NodeUtil.isStatement(n6));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.FOR);
    n2.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(n1));
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.SCRIPT);
    Node n3 = new Node(Token.FUNCTION);
    n3.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
  }
}