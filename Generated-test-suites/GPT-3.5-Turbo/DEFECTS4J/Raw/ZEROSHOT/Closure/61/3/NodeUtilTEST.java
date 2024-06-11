import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.IF);
    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_true() {
    Node node = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);
    assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.STRING);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.STRING);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }

}