
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, createNode(), createNode());
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = createNode(Token.SCRIPT);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.SCRIPT, createNode());
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = createNode(Token.VAR);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, createNode());
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = createNode(Token.SCRIPT);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, createNode());
    Node parent = createNode(Token.SCRIPT, node);
    node.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, createNode());
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  private Node createNode(int type, Node... children) {
    Node node = new Node(type);
    for (Node child : children) {
      node.addChildToBack(child);
      child.setParent(node);
    }
    return node;
  }
}
