import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node testNode = new Node(Token.FOR);
    testNode.addChildToBack(new Node(Token.EMPTY));
    testNode.addChildToBack(new Node(Token.EMPTY));
    testNode.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(testNode));
  }

  @Test
  public void testIsForIn_notForInLoop() {
    Node testNode = new Node(Token.FOR);
    testNode.addChildToBack(new Node(Token.EMPTY));
    testNode.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(testNode));
  }

  @Test
  public void testIsStatement() {
    Node testNode = new Node(Token.SCRIPT);
    testNode.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isStatement(testNode.getFirstChild()));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node testNode = new Node(Token.FUNCTION);
    testNode.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isStatement(testNode.getFirstChild()));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(testNode);

    assertTrue(NodeUtil.isFunctionDeclaration(testNode));

    testNode.setParent(parent);
    assertFalse(NodeUtil.isFunctionDeclaration(testNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.addChildToBack(testNode);
    parent.addChildToBack(grandparent);
    grandparent.setParent(parent);
    testNode.setParent(grandparent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));

    parent.removeChild(grandparent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(testNode));
  }
}