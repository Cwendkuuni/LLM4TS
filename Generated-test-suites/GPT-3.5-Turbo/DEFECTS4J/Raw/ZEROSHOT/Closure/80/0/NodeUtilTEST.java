import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = createMockNode(Token.FOR, 3);
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsNotForIn() {
    Node n = createMockNode(Token.WHILE, 2);
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = createMockNode(Token.SCRIPT, 1);
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsNotStatement() {
    Node n = createMockNode(Token.EXPR_RESULT, 1);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = createMockNode(Token.FUNCTION, 1);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = createMockNode(Token.GETPROP, 1);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = createMockNode(Token.FUNCTION, 1);
    Node parent = createMockNode(Token.SCRIPT, 1);
    n.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = createMockNode(Token.FUNCTION, 1);
    Node parent = createMockNode(Token.IF, 2);
    parent.setParent(createMockNode(Token.SCRIPT, 2));
    n.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  private Node createMockNode(int type, int childCount) {
    Node node = new Node(type);
    for (int i = 0; i < childCount; i++) {
      node.addChildToBack(new Node(Token.EMPTY));
    }
    return node;
  }
}