import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_True() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node parent = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT, parent);
    Node node = new Node(Token.FUNCTION, grandParent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}