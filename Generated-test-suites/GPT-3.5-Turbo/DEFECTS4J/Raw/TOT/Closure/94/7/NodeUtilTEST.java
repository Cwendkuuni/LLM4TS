import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.ARRAYLIT), new Node(Token.BLOCK));
    Node node2 = new Node(Token.FOR, new Node(Token.NAME, "i"), new Node(Token.NAME, "array"), new Node(Token.BLOCK));
    Node node3 = new Node(Token.WHILE, new Node(Token.NAME, "i"), new Node(Token.ARRAYLIT), new Node(Token.BLOCK));

    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
    assertFalse(NodeUtil.isForIn(node3));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.FUNCTION);
    node3.setParent(new Node(Token.SCRIPT));
    Node node4 = new Node(Token.SCRIPT);
    node4.addChildToFront(new Node(Token.FUNCTION));
    node4.getChildAtIndex(0).setParent(new Node(Token.SCRIPT));
    Node node5 = new Node(Token.IF);
    node5.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertFalse(NodeUtil.isStatement(node3));
    assertTrue(NodeUtil.isStatement(node4));
    assertFalse(NodeUtil.isStatement(node5));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node function1 = new Node(Token.FUNCTION);
    function1.setParent(new Node(Token.SCRIPT));
    Node function2 = new Node(Token.FUNCTION);
    function2.setParent(new Node(Token.BLOCK));
    Node function3 = new Node(Token.IF);
    function3.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isFunctionDeclaration(function1));
    assertFalse(NodeUtil.isFunctionDeclaration(function2));
    assertFalse(NodeUtil.isFunctionDeclaration(function3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node function1 = new Node(Token.FUNCTION);
    function1.setParent(new Node(Token.SCRIPT));
    Node function2 = new Node(Token.FUNCTION);
    Node function3 = new Node(Token.FUNCTION);
    function3.setParent(new Node(Token.FUNCTION));
    Node function4 = new Node(Token.SCRIPT);
    function4.addChildToFront(new Node(Token.FUNCTION));
    function4.getChildAtIndex(0).setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function3));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function4));
  }
}