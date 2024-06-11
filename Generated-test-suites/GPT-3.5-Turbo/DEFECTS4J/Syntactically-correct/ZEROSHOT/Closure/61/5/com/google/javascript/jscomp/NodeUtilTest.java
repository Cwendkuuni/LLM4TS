import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Before
  public void setUp() {
    // Set up any necessary objects before running the tests
  }
  
  @Test
  public void testIsForIn_true() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_true() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.IF);
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_true() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_true() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    node.setParent(grandParent);
    grandParent.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.IF);
    Node node = new Node(Token.FUNCTION);
    node.setParent(grandParent);
    grandParent.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}