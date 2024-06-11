import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(n));

    n.addChildToFront(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n));

    n.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n));

    n.addChildToFront(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(n));

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isStatement(n));

    parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isStatement(n));

    parent = new Node(Token.LABEL);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isStatement(n));

    parent = new Node(Token.ADD);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    parent = new Node(Token.LABEL);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));

    parent = new Node(Token.ADD);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    parent = new Node(Token.FUNCTION);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

    parent = new Node(Token.LABEL);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

    parent = new Node(Token.ADD);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}