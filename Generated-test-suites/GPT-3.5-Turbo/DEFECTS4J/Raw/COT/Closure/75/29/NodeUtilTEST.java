
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_incompleteForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_script() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.BLOCK);
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.LABEL);
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_scriptRoot() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.EMPTY));
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionRoot() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.EMPTY));
    Node parent = new Node(Token.EMPTY);
    parent.addChildToFront(new Node(Token.FUNCTION));
    node.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}
